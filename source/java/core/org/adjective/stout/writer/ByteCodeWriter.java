/* ------------------------------------------------------------------------
 * Copyright 2009 Tim Vernum
 * ------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ------------------------------------------------------------------------
 */

package org.adjective.stout.writer;

import java.io.PrintWriter;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.CheckClassAdapter;
import org.objectweb.asm.util.TraceClassVisitor;

import org.adjective.stout.core.AnnotationDescriptor;
import org.adjective.stout.core.ClassDescriptor;
import org.adjective.stout.core.Code;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.FieldDescriptor;
import org.adjective.stout.core.Instruction;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.MemberType;
import org.adjective.stout.core.MethodDescriptor;
import org.adjective.stout.core.Parameter;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.core.AnnotationDescriptor.Attribute;
import org.adjective.stout.core.ExecutionStack.Block;
import org.adjective.stout.core.UnresolvedType.Sort;
import org.adjective.stout.exception.StoutException;
import org.adjective.stout.exception.WriterException;
import org.adjective.stout.instruction.AbstractInstructionCollector;
import org.adjective.stout.operation.CodeStack;
import org.adjective.stout.operation.Variable;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ByteCodeWriter
{
    private boolean _trace = false;
    private boolean _check = false;

    public void setTrace(boolean trace)
    {
        _trace = trace;
    }

    public void setCheck(boolean check)
    {
        _check = check;
    }

    public byte[] write(ClassDescriptor cls)
    {
        begin(cls);
        ClassWriter writer = createClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        ClassVisitor cv = writer;
        if (_trace)
        {
            cv = new TraceClassVisitor(cv, new PrintWriter(System.err));
        }
        if (_check)
        {
            cv = new CheckClassAdapter(cv);
        }

        String signature = null; // @TODO
        cv.visit(Opcodes.V1_5, getModifierCode(cls.getModifiers(), cls.getSort()), cls.getInternalName(), signature,
                getInternalName(cls.getSuperClass()), getInterfaceNames(cls));

        if (cls.getSourceFile() != null)
        {
            cv.visitSource(cls.getSourceFile(), "");
        }

        UnresolvedType outer = cls.getOuterClass();
        if (outer != null)
        {
            cv.visitOuterClass(outer.getInternalName(), null, null);
        }

        for (AnnotationDescriptor annotation : cls.getAnnotations())
        {
            writeAnnotation(cv, annotation);
        }

        for (ClassDescriptor inner : cls.getInnerClasses())
        {
            String name = inner.getInternalName();
            String simpleName = name.substring(name.lastIndexOf('/') + 1);
            cv.visitInnerClass(name, cls.getInternalName(), simpleName, getModifierCode(inner.getModifiers(), inner.getSort()));
        }

        for (FieldDescriptor field : cls.getFields())
        {
            writeField(cv, field);
        }

        for (MethodDescriptor method : cls.getMethods())
        {
            writeMethod(cv, cls, method);
        }

        cv.visitEnd();

        end(cls);
        return writer.toByteArray();
    }

    protected ClassWriter createClassWriter(int flags)
    {
        return new ClassWriter(flags);
    }

    protected void begin(@SuppressWarnings("unused") ClassDescriptor cls)
    {
        // Override in sub-classes
    }

    protected void end(@SuppressWarnings("unused") ClassDescriptor cls)
    {
        // Override in sub-classes
    }

    private void writeAnnotation(ClassVisitor cv, AnnotationDescriptor annotation)
    {
        AnnotationVisitor av = cv.visitAnnotation(Type.getDescriptor(annotation.getType()), annotation.isRuntime());
        this.processAnnotation(annotation, av);
    }

    private void writeField(ClassVisitor cv, FieldDescriptor field)
    {
        String signature = null; // @TODO
        FieldVisitor fv = cv.visitField(getModifierCode(field.getModifiers(), MemberType.FIELD), field.getName(), field.getType().getDescriptor(),
                signature, null);

        for (AnnotationDescriptor annotation : field.getAnnotations())
        {
            AnnotationVisitor av = fv.visitAnnotation(Type.getDescriptor(annotation.getType()), annotation.isRuntime());
            processAnnotation(annotation, av);
        }

        fv.visitEnd();
    }

    private void writeMethod(ClassVisitor cv, ClassDescriptor cls, MethodDescriptor method)
    {
        String[] exceptions = new String[method.getExceptions().length];
        for (int i = 0; i < exceptions.length; i++)
        {
            exceptions[i] = getInternalName(method.getExceptions()[i]);
        }

        String signature = null; // @TODO
        final MethodVisitor mv = visitMethod(cv, method, exceptions, signature);

        for (AnnotationDescriptor annotation : method.getAnnotations())
        {
            AnnotationVisitor av = mv.visitAnnotation(Type.getDescriptor(annotation.getType()), annotation.isRuntime());
            processAnnotation(annotation, av);
        }

        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++)
        {
            Parameter parameter = parameters[i];
            AnnotationDescriptor[] annotations = parameter.getAnnotations();
            for (AnnotationDescriptor annotation : annotations)
            {
                AnnotationVisitor av = mv.visitParameterAnnotation(i, Type.getDescriptor(annotation.getType()), annotation.isRuntime());
                processAnnotation(annotation, av);
            }
        }

        Code body = method.getBody();
        if (method.getModifiers().contains(ElementModifier.ABSTRACT))
        {
            if (body != null)
            {
                throw new IllegalStateException("The abstract method " + method + " cannot have a body");
            }
        }
        else
        {
            if (body == null)
            {
                throw new IllegalStateException("The method " + method + " is not abstract, but does not have a body");
            }
            mv.visitCode();

            CodeStack stack = new CodeStack(cls, method, mv);
            Block block = stack.pushBlock();
            if (!isStatic(method))
            {
                block.declareVariable(new Variable("#this", cls));
            }

            for (Parameter parameter : parameters)
            {
                block.declareVariable(new Variable(parameter.getName(), parameter.getType()));
            }

            InstructionCollector collector = new AbstractInstructionCollector()
            {
                private int _line;

                public void add(Instruction instruction, int line)
                {
                    if (line != 0 && line != _line)
                    {
                        _line = line;
                        Label label = new Label();
                        mv.visitLabel(label);
                        mv.visitLineNumber(line, label);
                    }
                    instruction.accept(mv);
                }
            };
            try
            {
                body.getInstructions(stack, collector);
            }
            catch (StoutException e)
            {
                throw new WriterException("In class " + cls.getPackage() + "." + cls.getName() + ", cannot write method " + method.getName(), e);
            }
            stack.popBlock(block);
            stack.declareVariableInfo();
        }

        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    protected MethodVisitor visitMethod(ClassVisitor cv, MethodDescriptor method, String[] exceptions, String signature)
    {
        return cv.visitMethod(getModifierCode(method.getModifiers(), MemberType.METHOD), method.getName(), getMethodDescriptor(method), signature,
                exceptions);
    }

    private void processAnnotation(AnnotationDescriptor annotation, AnnotationVisitor av)
    {
        for (Attribute attribute : annotation.getAttributes())
        {
            processAttribute(av, attribute);
        }
        av.visitEnd();
    }

    private void processAttribute(AnnotationVisitor av, Attribute attribute)
    {
        Object value = attribute.getValue();
        Class< ? > valueType = value.getClass();
        if (valueType.isEnum())
        {
            av.visitEnum(attribute.getName(), Type.getDescriptor(valueType), ((Enum< ? >) value).name());
        }
        else if (isEnumArray(valueType))
        {
            String type = Type.getDescriptor(valueType.getComponentType());
            AnnotationVisitor array = av.visitArray(attribute.getName());
            Enum< ? >[] enums = (Enum< ? >[]) value;
            for (Enum< ? > e : enums)
            {
                array.visitEnum(null, type, e.name());
            }
            array.visitEnd();
        }
        else
        {
            av.visit(attribute.getName(), value);
        }
    }

    private boolean isEnumArray(Class< ? extends Object> valueType)
    {
        return valueType.isArray() && valueType.getComponentType().isEnum();
    }

    private boolean isStatic(MethodDescriptor method)
    {
        return method.getModifiers().contains(ElementModifier.STATIC);
    }

    private String getMethodDescriptor(MethodDescriptor method)
    {
        UnresolvedType[] parameterTypes = method.getParameterTypes();
        Type[] argumentTypes = new Type[parameterTypes.length];
        for (int i = 0; i < argumentTypes.length; i++)
        {
            argumentTypes[i] = Type.getType(parameterTypes[i].getDescriptor());
        }
        Type returnType = Type.getType(method.getReturnType().getRawClass());
        return Type.getMethodDescriptor(returnType, argumentTypes);
    }

    private String[] getInterfaceNames(ClassDescriptor cls)
    {
        UnresolvedType[] interfaces = cls.getInterfaces();
        String[] interfaceNames = new String[interfaces.length];
        for (int i = 0; i < interfaceNames.length; i++)
        {
            interfaceNames[i] = getInternalName(interfaces[i]);
        }
        return interfaceNames;
    }

    private String getInternalName(UnresolvedType cls)
    {
        if (cls == null)
        {
            return Type.getInternalName(Object.class);
        }
        else
        {
            return cls.getInternalName();
        }
    }

    private int getModifierCode(Set<ElementModifier> modifiers, Sort sort)
    {
        int code = (sort == Sort.INTERFACE ? Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT : 0);
        for (ElementModifier modifier : modifiers)
        {
            code |= modifier.getCode();
        }
        return code;
    }

    private int getModifierCode(Set<ElementModifier> modifiers, MemberType type)
    {
        int code = getModifierCode(modifiers, (Sort) null);
        int illegal = Opcodes.ACC_ANNOTATION | Opcodes.ACC_BRIDGE | Opcodes.ACC_ENUM | Opcodes.ACC_INTERFACE | Opcodes.ACC_SUPER;
        switch (type)
        {
            case FIELD:
                illegal |= Opcodes.ACC_ABSTRACT | Opcodes.ACC_NATIVE | Opcodes.ACC_STRICT | Opcodes.ACC_SYNCHRONIZED;
                break;
            case METHOD:
                if (isBitSet(Opcodes.ACC_ABSTRACT, code))
                {
                    illegal |= Opcodes.ACC_NATIVE | Opcodes.ACC_STRICT | Opcodes.ACC_SYNCHRONIZED | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL;
                }
                break;
        }
        if (isBitSet(illegal, code))
        {
            throw new IllegalStateException("Illegal combination of modifier codes: " + code + " (illegal codes: " + illegal + ")");
        }
        return code;
    }

    private boolean isBitSet(int bit, int code)
    {
        return (code & bit) != 0;
    }
}

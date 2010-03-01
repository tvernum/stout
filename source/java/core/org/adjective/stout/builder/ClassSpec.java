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

package org.adjective.stout.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Opcodes;

import org.adjective.stout.core.AnnotationDescriptor;
import org.adjective.stout.core.ClassDescriptor;
import org.adjective.stout.core.Code;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.FieldDescriptor;
import org.adjective.stout.core.MethodDescriptor;
import org.adjective.stout.core.Operation;
import org.adjective.stout.core.ParameterisedClass;
import org.adjective.stout.core.TypeParameter;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.ClassImpl;
import org.adjective.stout.impl.CodeImpl;
import org.adjective.stout.impl.ParameterisedClassImpl;
import org.adjective.stout.instruction.MethodInstruction;
import org.adjective.stout.operation.ReturnVoidOperation;
import org.adjective.stout.operation.ThisExpression;

import static org.adjective.stout.util.CollectionUtils.toArray;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ClassSpec implements ElementBuilder<ClassDescriptor>, UnresolvedType
{
    private static final List<ElementModifier> ALLOWED_INTERFACE_MODIFIERS = Arrays.asList( //
            ElementModifier.PUBLIC, ElementModifier.ABSTRACT, ElementModifier.INTERFACE);

    private static final List<ElementModifier> REQUIRED_INTERFACE_FIELD_MODIFIERS = Arrays.asList( //
            ElementModifier.PUBLIC, ElementModifier.STATIC, ElementModifier.FINAL);

    private static final List<ElementModifier> REQUIRED_INTERFACE_METHOD_MODIFIERS = Arrays.asList( //
            ElementModifier.PUBLIC, ElementModifier.ABSTRACT);

    public static final String CONSTRUCTOR_NAME = "<init>";

    private final Sort _sort;
    private final String _package;
    private final String _name;
    private String _source;
    private UnresolvedType _outerClass;
    private final Set<ElementModifier> _modifiers;
    private final List<TypeParameter> _parameters;
    private ParameterisedClass _superClass;
    private final List<UnresolvedType> _interfaces;
    private final List<AnnotationDescriptor> _annotations;
    private final List<ClassDescriptor> _innerClasses;
    private final List<FieldDescriptor> _fields;
    private final List<MethodDescriptor> _methods;
    private ElementModifier _defaultConstructorAccess;

    private ClassSpec(Sort sort, String pkg, String name)
    {
        _sort = sort;
        _package = pkg;
        _name = name;
        _modifiers = new HashSet<ElementModifier>();
        _parameters = new ArrayList<TypeParameter>();
        _interfaces = new ArrayList<UnresolvedType>();
        _annotations = new ArrayList<AnnotationDescriptor>();
        _innerClasses = new ArrayList<ClassDescriptor>();
        _fields = new ArrayList<FieldDescriptor>();
        _methods = new ArrayList<MethodDescriptor>();
        _defaultConstructorAccess = null;
    }

    public static ClassSpec newClass(String pkg, String name)
    {
        return new ClassSpec(Sort.CLASS, pkg, name);
    }

    public static ClassSpec newInterface(String pkg, String name)
    {
        return new ClassSpec(Sort.INTERFACE, pkg, name);
    }

    public ClassDescriptor create()
    {
        if (_defaultConstructorAccess != null)
        {
            addDefaultConstructor();
        }
        if(isInterface()) {
         return ClassImpl
         .newInterface(_modifiers, _package, _name, _source, _outerClass, toArray(_parameters, TypeParameter.class), //
                 toArray(_interfaces, UnresolvedType.class), toArray(_annotations, AnnotationDescriptor.class), //
                 toArray(_innerClasses, ClassDescriptor.class), toArray(_fields, FieldDescriptor.class), toArray(_methods, MethodDescriptor.class));
        }
        return ClassImpl
        .newClass(_modifiers, _package, _name, _source, _outerClass, toArray(_parameters, TypeParameter.class), _superClass, //
                toArray(_interfaces, UnresolvedType.class), toArray(_annotations, AnnotationDescriptor.class), //
                toArray(_innerClasses, ClassDescriptor.class), toArray(_fields, FieldDescriptor.class), toArray(_methods, MethodDescriptor.class));
    }

    private void addDefaultConstructor()
    {
        String superName = _superClass.getInternalName();

        Operation loadThis = ThisExpression.LOAD_THIS;
        Operation invokeSuper = new MethodInstruction(Opcodes.INVOKESPECIAL, superName, CONSTRUCTOR_NAME, "()V");
        Operation returnNothing = new ReturnVoidOperation();
        Code body = new CodeImpl(loadThis, invokeSuper, returnNothing);

        MethodSpec method = new MethodSpec(CONSTRUCTOR_NAME).withModifiers(_defaultConstructorAccess).withBody(body);
        _methods.add(0, method.create());
    }

    public ClassSpec withModifiers(ElementModifier... modifiers)
    {
        if (isInterface())
        {
            for (ElementModifier modifier : modifiers)
            {
                if (!ALLOWED_INTERFACE_MODIFIERS.contains(modifier))
                {
                    throw new IllegalArgumentException("The interface " + _name + " cannot have modifier " + modifier);
                }
            }
        }
        _modifiers.addAll(Arrays.asList(modifiers));
        return this;
    }

    public ClassSpec withTypeParameter(TypeParameter parameter)
    {
        _parameters.add(parameter);
        return this;
    }

    public ClassSpec withTypeParameter(ElementBuilder< ? extends TypeParameter> parameter)
    {
        return withTypeParameter(parameter.create());
    }

    public ClassSpec withSourceCode(String source)
    {
        _source = source;
        return this;
    }

    public ClassSpec withTypeParameters(TypeParameter... parameters)
    {
        _parameters.addAll(Arrays.asList(parameters));
        return this;
    }

    public ClassSpec withTypeParameters(ElementBuilder< ? extends TypeParameter>... parameters)
    {
        for (ElementBuilder< ? extends TypeParameter> builder : parameters)
        {
            withTypeParameter(builder);
        }
        return this;
    }

    public ClassSpec withSuperClass(ParameterisedClass superClass)
    {
        if (isInterface())
        {
            throw new IllegalArgumentException("The interface " + _name + " cannot have a superclass (" + superClass + ")");
        }
        _superClass = superClass;
        return this;
    }

    public ClassSpec withSuperClass(Class< ? > superClass)
    {
        return withSuperClass(new ParameterisedClassImpl(superClass));
    }

    public ClassSpec withSuperClass(ElementBuilder< ? extends ParameterisedClass> superClass)
    {
        return withSuperClass(superClass.create());
    }

    public ClassSpec withInterface(ParameterisedClass iface)
    {
        _interfaces.add(iface);
        return this;
    }

    public ClassSpec withInterface(Class< ? > iface)
    {
        return withInterface(new ParameterisedClassImpl(iface));
    }

    public ClassSpec withInterface(ElementBuilder< ? extends ParameterisedClass> iface)
    {
        return withInterface(iface.create());
    }

    public ClassSpec withInterfaces(ParameterisedClass... interfaces)
    {
        _interfaces.addAll(Arrays.asList(interfaces));
        return this;
    }

    public ClassSpec withInterfaces(Class< ? >... interfaces)
    {
        for (Class< ? > cls : interfaces)
        {
            this.withInterface(cls);
        }
        return this;
    }

    public ClassSpec withInterfaces(ElementBuilder< ? extends ParameterisedClass>... interfaces)
    {
        for (ElementBuilder< ? extends ParameterisedClass> builder : interfaces)
        {
            this.withInterface(builder);
        }
        return this;
    }

    public ClassSpec withAnnotation(ElementBuilder< ? extends AnnotationDescriptor> builder)
    {
        return withAnnotation(builder.create());
    }

    public ClassSpec withAnnotation(AnnotationDescriptor annotation)
    {
        _annotations.add(annotation);
        return this;
    }

    public ClassSpec withField(FieldDescriptor field)
    {
        if (isInterface())
        {
            Set<ElementModifier> modifiers = field.getModifiers();
            for (ElementModifier required : REQUIRED_INTERFACE_FIELD_MODIFIERS)
            {
                if (!modifiers.contains(required))
                {
                    throw new IllegalArgumentException("All fields in an interface (" + _name + ") must be " + required);
                }
            }
        }
        _fields.add(field);
        return this;
    }

    public ClassSpec withField(ElementBuilder< ? extends FieldDescriptor> field)
    {
        return withField(field.create());
    }

    public ClassSpec withMethod(MethodDescriptor method)
    {
        if (isInterface())
        {
            Set<ElementModifier> modifiers = method.getModifiers();
            for (ElementModifier required : REQUIRED_INTERFACE_METHOD_MODIFIERS)
            {
                if (!modifiers.contains(required))
                {
                    throw new IllegalArgumentException("All methods in an interface (" + _name + ") must be " + required);
                }
            }
        }
        _methods.add(method);
        return this;
    }

    public ClassSpec withMethod(ElementBuilder< ? extends MethodDescriptor> method)
    {
        return withMethod(method.create());
    }

    public ClassSpec withMethods(MethodDescriptor... methods)
    {
        if (isInterface())
        {
            for (MethodDescriptor method : methods)
            {
                Set<ElementModifier> modifiers = method.getModifiers();
                for (ElementModifier required : REQUIRED_INTERFACE_METHOD_MODIFIERS)
                {
                    if (!modifiers.contains(required))
                    {
                        throw new IllegalArgumentException("All methods in an interface (" + _name + ") must be " + required);
                    }
                }
            }
        }
        _methods.addAll(Arrays.asList(methods));
        return this;
    }

    public ClassSpec withMethods(ElementBuilder< ? extends MethodDescriptor>... methods)
    {
        for (ElementBuilder< ? extends MethodDescriptor> builder : methods)
        {
            withMethod(builder);
        }
        return this;

    }

    public ClassSpec withDefaultConstructor(ElementModifier access)
    {
        _defaultConstructorAccess = access;
        return this;
    }

    public String getPackage()
    {
        return _package;
    }

    public String getName()
    {
        return _name;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        if (_package != null)
        {
            builder.append(_package);
            builder.append('.');
        }
        builder.append(_name);
        if (_superClass != null)
        {
            builder.append(" extends ");
            builder.append(_superClass);
        }
        return builder.toString();
    }

    public String getInternalName()
    {
        return _package.replace('.', '/') + "/" + _name;
    }

    public String getDescriptor()
    {
        return "L" + getInternalName() + ";";
    }

    public Sort getSort()
    {
        return _sort;
    }

    private boolean isInterface()
    {
        return _sort == Sort.INTERFACE;
    }

    public boolean canAssignTo(UnresolvedType type)
    {
        if (getDescriptor().equals(type.getDescriptor()))
        {
            return true;
        }
        if (_superClass != null && _superClass.canAssignTo(type))
        {
            return true;
        }
        if (_interfaces != null)
        {
            for (UnresolvedType ifc : _interfaces)
            {
                if (ifc.canAssignTo(type))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public UnresolvedType getFieldType(String fieldName)
    {
        if (_fields != null)
        {
            for (FieldDescriptor field : _fields)
            {
                if (fieldName.equals(field.getName()))
                {
                    return field.getType();
                }
            }
        }
        return null;
    }

    public ClassSpec withOuterClass(UnresolvedType outer)
    {
        _outerClass = outer;
        return this;
    }

    public ClassSpec withInnerClasses(ClassDescriptor... inner)
    {
        if (isInterface())
        {
            throw new UnsupportedOperationException("Cannot have an inner class in interface " + _name);
        }
        _innerClasses.addAll(Arrays.asList(inner));
        return this;
    }

    public ClassSpec withInnerClass(ClassDescriptor inner)
    {
        if (isInterface())
        {
            throw new UnsupportedOperationException("Cannot have an inner class in interface " + _name);
        }
        _innerClasses.add(inner);
        return this;
    }

    public List<MethodDescriptor> getMethods()
    {
        return _methods;
    }

    public void withInterface(ClassDescriptor descriptor)
    {
        _interfaces.add(descriptor);
    }

}

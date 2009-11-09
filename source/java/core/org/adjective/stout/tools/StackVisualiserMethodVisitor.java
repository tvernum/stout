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

package org.adjective.stout.tools;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.util.AbstractVisitor;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
class StackVisualiserMethodVisitor extends AbstractVisitor implements MethodVisitor
{
    private static final Type OBJECT_TYPE = Type.getType(Object.class);

    class StackValue
    {
        public final String description;
        public final Type type;

        public StackValue(String valueDescription, Type valueType)
        {
            description = valueDescription;
            type = valueType;
        }

        public StackValue(String typeDescriptor)
        {
            this.type = Type.getType(typeDescriptor);
            this.description = type.getClassName();
        }

        public String toString()
        {
            return (this.type == null ? ' ' : this.type) + " : " + this.description;
        }
    }

    private final PrintStream _output;
    private final List<StackValue> _stack;

    public StackVisualiserMethodVisitor(PrintStream output)
    {
        _output = output;
        _stack = new ArrayList<StackValue>();
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible)
    {
        return null;
    }

    public AnnotationVisitor visitAnnotationDefault()
    {
        return null;
    }

    public void visitAttribute(Attribute attr)
    {
        // no-op
    }

    public void visitFrame(int type, int local, Object[] local2, int stack, Object[] stack2)
    {
        // no-op
    }

    public void visitCode()
    {
        _output.println("{ /*******/");
    }

    public void visitEnd()
    {
        _output.println("} /*******/");
    }

    private void print(int opcode, String string)
    {
        _output.println(OPCODES[opcode] + " " + string);
        for (StackValue value : _stack)
        {
            _output.println("\t\t== " + value);
        }
        _output.println();
    }

    private void push(String description, Type type)
    {
        StackValue value = new StackValue(description, type);
        push(value);
        if (type.getSize() == 2)
        {
            _stack.add(new StackValue("(.." + description + "..)", null));
        }
    }

    private void push(StackValue value)
    {
        _stack.add(value);
    }

    private void push(String description, String typeDescriptor)
    {
        push(description, Type.getType(typeDescriptor));
    }

    private void push(String description, Class< ? > type)
    {
        push(description, Type.getType(type));
    }

    private StackValue peek()
    {
        return peek(1);
    }

    private StackValue peek(int n)
    {
        int index = _stack.size() - n;
        if (index < 0)
        {
            return new StackValue("???", OBJECT_TYPE);
        }
        return _stack.get(index);
    }

    private StackValue pop()
    {
        return popValue(true);
    }

    private StackValue popValue(boolean checkDoubleWidth)
    {
        if (_stack.isEmpty())
        {
            error("Attempt to pop value from empty stack");
            return new StackValue("*DNE*", OBJECT_TYPE);
        }
        StackValue value = _stack.remove(_stack.size() - 1);
        _output.println("\t\t<< " + value);
        if (checkDoubleWidth && value.type == null)
        {
            error("Attempt to pop second half of double width value " + peek());
        }
        return value;
    }

    private void error(String message)
    {
        _output.println("*** ERROR *** " + message);
    }

    private StackValue pop(Type... types)
    {
        StackValue value = pop(types[0].getSize());
        checkType(value, types);
        return value;
    }

    /**
     * @return the last vaue pop'ed
     */
    private StackValue pop(int count)
    {
        if (count <= 0)
        {
            throw new IllegalArgumentException("Attempt to pop " + count + " values");
        }
        if (count == 1)
        {
            return popValue(true);
        }
        else if (count == 2)
        {
            popValue(false);
            return popValue(true);
        }
        else
        {
            pop(2);
            return pop(count - 2);
        }
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc)
    {
        String description = owner + "." + name;
        switch (opcode)
        {
            case Opcodes.GETFIELD:
                pop();
                push(description, desc);
            case Opcodes.PUTFIELD:
                pop(2);
                break;
            case Opcodes.GETSTATIC:
                push(description, desc);
                break;
            case Opcodes.PUTSTATIC:
                pop(1);
                break;
        }
        print(opcode, description);
    }

    public void visitIincInsn(int var, int increment)
    {
        print(Opcodes.IINC, Integer.toString(var) + " " + increment);
    }

    public void visitInsn(int opcode)
    {
        switch (opcode)
        {
            case Opcodes.NOP:
                break;
            case Opcodes.ACONST_NULL:
                push("null", Object.class);
                break;
            case Opcodes.ICONST_M1:
            case Opcodes.ICONST_0:
            case Opcodes.ICONST_1:
            case Opcodes.ICONST_2:
            case Opcodes.ICONST_3:
            case Opcodes.ICONST_4:
            case Opcodes.ICONST_5:
                push(Integer.toString(opcode - Opcodes.ICONST_0), Type.INT_TYPE);
                break;
            case Opcodes.LCONST_0:
            case Opcodes.LCONST_1:
                push(Integer.toString(opcode - Opcodes.LCONST_0), Type.LONG_TYPE);
                break;
            case Opcodes.FCONST_0:
            case Opcodes.FCONST_1:
            case Opcodes.FCONST_2:
                push(Integer.toString(opcode - Opcodes.FCONST_0), Type.FLOAT_TYPE);
                break;
            case Opcodes.DCONST_0:
            case Opcodes.DCONST_1:
                push(Integer.toString(opcode - Opcodes.DCONST_0), Type.DOUBLE_TYPE);
                break;
            case Opcodes.IALOAD:
            case Opcodes.LALOAD:
            case Opcodes.FALOAD:
            case Opcodes.DALOAD:
            case Opcodes.AALOAD:
            case Opcodes.BALOAD:
            case Opcodes.CALOAD:
            case Opcodes.SALOAD:
                {
                    Type opType = getType(Opcodes.IALOAD, opcode);
                    StackValue idx = pop(Type.INT_TYPE);
                    StackValue arr = popArray(opType);
                    push(arr.description + "[" + idx.description + "]", opType);
                }
                break;
            case Opcodes.IASTORE:
            case Opcodes.LASTORE:
            case Opcodes.FASTORE:
            case Opcodes.DASTORE:
            case Opcodes.AASTORE:
            case Opcodes.BASTORE:
            case Opcodes.CASTORE:
            case Opcodes.SASTORE:
                {
                    Type opType = getType(Opcodes.IASTORE, opcode);
                    pop(opType);
                    pop(Type.INT_TYPE);
                    popArray(opType);
                }
                break;
            case Opcodes.POP:
                pop();
                break;
            case Opcodes.POP2:
                pop(2);
                break;
            case Opcodes.DUP:
                push(peek());
                break;
            case Opcodes.DUP2:
                push(peek(2));
                push(peek(1));
                break;
            case Opcodes.DUP_X1:
                {
                    StackValue a = pop();
                    StackValue b = pop();
                    push(a);
                    push(b);
                    push(a);
                }
                break;
            case Opcodes.DUP_X2:
                {
                    StackValue a = pop();
                    StackValue b = pop();
                    StackValue c = pop();
                    push(a);
                    push(c);
                    push(b);
                    push(a);
                }
                break;
            case Opcodes.DUP2_X1:
                {
                    StackValue a = popValue(false);
                    StackValue b = pop();
                    StackValue c = pop();
                    push(b);
                    push(a);
                    push(c);
                    push(b);
                    push(a);
                }
            case Opcodes.DUP2_X2:
                {
                    StackValue a = popValue(false);
                    StackValue b = pop();
                    StackValue c = popValue(false);
                    StackValue d = pop();
                    push(b);
                    push(a);
                    push(d);
                    push(c);
                    push(b);
                    push(a);
                }
                break;
            case Opcodes.SWAP:
                {
                    StackValue a = pop();
                    StackValue b = pop();
                    push(a);
                    push(b);
                }
                break;
            case Opcodes.IADD:
            case Opcodes.LADD:
            case Opcodes.FADD:
            case Opcodes.DADD:
                math(Opcodes.IADD, opcode, "+");
                break;
            case Opcodes.ISUB:
            case Opcodes.LSUB:
            case Opcodes.FSUB:
            case Opcodes.DSUB:
                math(Opcodes.ISUB, opcode, "-");
                break;
            case Opcodes.IMUL:
            case Opcodes.LMUL:
            case Opcodes.FMUL:
            case Opcodes.DMUL:
                math(Opcodes.IMUL, opcode, "*");
                break;
            case Opcodes.IDIV:
            case Opcodes.LDIV:
            case Opcodes.FDIV:
            case Opcodes.DDIV:
                math(Opcodes.IDIV, opcode, "/");
                break;
            case Opcodes.IREM:
            case Opcodes.LREM:
            case Opcodes.FREM:
            case Opcodes.DREM:
                math(Opcodes.IREM, opcode, "%");
                break;
            case Opcodes.IAND:
            case Opcodes.LAND:
                math(Opcodes.IAND, opcode, "&");
                break;
            case Opcodes.IOR:
            case Opcodes.LOR:
                math(Opcodes.IOR, opcode, "|");
                break;
            case Opcodes.IXOR:
            case Opcodes.LXOR:
                math(Opcodes.IXOR, opcode, "^");
                break;
            case Opcodes.INEG:
            case Opcodes.LNEG:
            case Opcodes.FNEG:
            case Opcodes.DNEG:
                {
                    Type type = getType(Opcodes.INEG, opcode);
                    StackValue a = pop(type);
                    push("-" + a.description, type);
                }
                break;
            case Opcodes.ISHL:
            case Opcodes.LSHL:
                {
                    Type type = getType(Opcodes.ISHL, opcode);
                    StackValue n = pop(Type.INT_TYPE);
                    StackValue a = pop(type);
                    push(a.description + "<<" + n.description, type);
                }
                break;
            case Opcodes.ISHR:
            case Opcodes.LSHR:
                {
                    Type type = getType(Opcodes.ISHR, opcode);
                    StackValue n = pop(Type.INT_TYPE);
                    StackValue a = pop(type);
                    push(a.description + ">>" + n.description, type);
                }
                break;
            case Opcodes.IUSHR:
            case Opcodes.LUSHR:
                {
                    Type type = getType(Opcodes.IUSHR, opcode);
                    StackValue n = pop(Type.INT_TYPE);
                    StackValue a = pop(type);
                    push(a.description + ">>>" + n.description, type);
                }
            case Opcodes.LCMP:
                {
                    StackValue a = pop(Type.LONG_TYPE);
                    StackValue b = pop(Type.LONG_TYPE);
                    push(a.description + " cmp " + b.description + " {-1|0|1}", Type.LONG_TYPE);
                }
                break;
            case Opcodes.I2L:
            case Opcodes.I2F:
            case Opcodes.I2D:
            case Opcodes.L2I:
            case Opcodes.L2F:
            case Opcodes.L2D:
            case Opcodes.F2I:
            case Opcodes.F2L:
            case Opcodes.F2D:
            case Opcodes.D2I:
            case Opcodes.D2L:
            case Opcodes.D2F:
            case Opcodes.I2B:
            case Opcodes.I2C:
            case Opcodes.I2S:
                cast(opcode);
                break;
            case Opcodes.ARETURN:
            case Opcodes.ATHROW:
                popObject();
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode " + OPCODES[opcode]);
        }
        print(opcode, "");
        /* 
                *        FCMPL, FCMPG, DCMPL, DCMPG, IRETURN, LRETURN,
                *        FRETURN, DRETURN, ARETURN, RETURN, ARRAYLENGTH, ATHROW,
                *        MONITORENTER, or MONITOREXIT
          */

    }

    private StackValue popObject()
    {
        StackValue pop = pop();
        if (!pop.type.getDescriptor().startsWith("L"))
        {
            error("Expected object on stack, but was " + pop);
        }
        return pop;
    }

    private void cast(int opcode)
    {
        String mnemonic = OPCODES[opcode];
        Type from = getType(mnemonic.charAt(0));
        Type to = getType(mnemonic.charAt(2));
        String pop = pop(from).description;
        push(pop, to);
    }

    private Type getType(char ch)
    {
        switch (ch)
        {
            case 'I':
                return Type.INT_TYPE;
            case 'L':
                return Type.LONG_TYPE;
            case 'F':
                return Type.FLOAT_TYPE;
            case 'D':
                return Type.DOUBLE_TYPE;
            case 'B':
                return Type.BYTE_TYPE;
            case 'C':
                return Type.CHAR_TYPE;
            case 'S':
                return Type.SHORT_TYPE;
        }
        throw new IllegalArgumentException("Bad type mnemonic " + ch);
    }

    private StackValue popArray(Type elementType)
    {
        StackValue arr = pop();
        if (arr.type.getDimensions() == 0)
        {
            error("Attempt to load an element into the non-array " + arr);
        }
        else
        {
            checkType(arr.type.getElementType(), arr, elementType);
        }
        return arr;
    }

    private void checkType(StackValue value, Type... types)
    {
        checkType(value.type, value, types);
    }

    private void checkType(Type stackType, StackValue stackItem, Type... allowed)
    {
        for (Type required : allowed)
        {

            if (required.equals(stackType))
            {
                return;
            }
            if (required.getSort() == Type.OBJECT && Object.class.getName().equals(required.getClassName()))
            {
                return;
            }
            if (required.getSort() == Type.OBJECT && Object.class.getName().equals(stackType.getClassName()))
            {
                return;
            }
        }
        error("Incorrect type '" + stackType + "' (at item " + stackItem + ") Expected: " + Arrays.toString(allowed));
    }

    private Type getType(int base, int opcode)
    {
        int t = opcode - base;
        switch (t)
        {
            case 0:
                return Type.INT_TYPE;
            case 1:
                return Type.LONG_TYPE;
            case 2:
                return Type.FLOAT_TYPE;
            case 3:
                return Type.DOUBLE_TYPE;
            case 4:
                return OBJECT_TYPE;
            case 5:
                return Type.BYTE_TYPE;
            case 6:
                return Type.CHAR_TYPE;
            case 7:
                return Type.SHORT_TYPE;
        }
        throw new IllegalArgumentException("Opcode " + OPCODES[opcode] + " is not an extension of " + OPCODES[base]);
    }

    private void math(int base, int opcode, String operator)
    {
        Type type = getType(base, opcode);
        StackValue a = pop(type);
        StackValue b = pop(type);
        push(a.description + operator + b.description, type);
    }

    public void visitIntInsn(int opcode, int operand)
    {
        throw new IllegalArgumentException("Unsupported opcode " + OPCODES[opcode]);
    }

    public void visitJumpInsn(int opcode, Label label)
    {
        switch (opcode)
        {
            case Opcodes.IFEQ:
                pop(Type.INT_TYPE, Type.BOOLEAN_TYPE);
                break;
            case Opcodes.GOTO:
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode " + OPCODES[opcode]);
        }
        print(opcode, label.toString());
    }

    public void visitLabel(Label label)
    {
        _output.println("-=[ " + label + " ]=-");
        // No-op
    }

    public void visitLdcInsn(Object cst)
    {
        Type type;
        if (cst instanceof String)
        {
            type = Type.getType(String.class);
        }
        else if (cst instanceof Long)
        {
            type = Type.LONG_TYPE;
        }
        else
        {
            error("Unknown LDC type " + cst.getClass());
            type = Type.INT_TYPE;
        }
        push(cst.toString(), type);
        print(Opcodes.LDC, cst.toString());
    }

    public void visitLineNumber(int line, Label start)
    {
        // No-op
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
    {
        // No-op
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
    {
        throw new IllegalArgumentException("Unsupported instruction - SWITCH ");
    }

    public void visitMaxs(int maxStack, int maxLocals)
    {
        // No-op
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc)
    {

        Type[] argumentTypes = Type.getArgumentTypes(desc);
        Type returnType = Type.getReturnType(desc);

        StringBuilder args = new StringBuilder();

        for (int i = argumentTypes.length; i > 0; i--)
        {
            args.append(pop(argumentTypes[i - 1]).description);
            args.append(",");
        }
        switch (opcode)
        {
            case Opcodes.INVOKESTATIC:
                break;
            case Opcodes.INVOKEINTERFACE:
            case Opcodes.INVOKEVIRTUAL:
            case Opcodes.INVOKESPECIAL:
                pop(Type.getObjectType(owner));
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode " + OPCODES[opcode]);
        }

        String description = owner + "." + name + "(" + args + ")";
        if (!returnType.equals(Type.VOID_TYPE))
        {
            push(description, returnType);
        }
        print(opcode, description + " {" + desc + "}");
    }

    public void visitMultiANewArrayInsn(String desc, int dims)
    {
        throw new IllegalArgumentException("Unsupported - Multi New Array " + desc);
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible)
    {
        return null;
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels)
    {
        throw new IllegalArgumentException("Unsupported instruction - SWITCH");
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
    {
        throw new IllegalArgumentException("Unsupported instruction - Try/Catch");
    }

    public void visitTypeInsn(int opcode, String type)
    {
        String description = type.replace('/', '.');
        switch (opcode)
        {
            case Opcodes.NEW:
                push(description, Type.getObjectType(type));
                break;
            case Opcodes.ANEWARRAY:
                StackValue size = pop(Type.INT_TYPE);
                push(description + "[" + size.description + "]", arrayOf(Type.getObjectType(type)));
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode " + OPCODES[opcode]);
        }
        print(opcode, description);
    }

    private Type arrayOf(Type element)
    {
        return Type.getType("[" + element.getDescriptor());
    }

    public void visitVarInsn(int opcode, int var)
    {
        String description = (var == 0 ? "this" : "v" + var);
        switch (opcode)
        {
            case Opcodes.ISTORE:
            case Opcodes.LSTORE:
            case Opcodes.FSTORE:
            case Opcodes.DSTORE:
            case Opcodes.ASTORE:
                {
                    Type type = getType(Opcodes.ISTORE, opcode);
                    pop(type);
                }
                break;
            case Opcodes.ILOAD:
            case Opcodes.LLOAD:
            case Opcodes.FLOAD:
            case Opcodes.DLOAD:
            case Opcodes.ALOAD:
                {
                    Type type = getType(Opcodes.ILOAD, opcode);
                    push(description, type);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported opcode " + OPCODES[opcode]);
        }
        print(opcode, description);
    }

}

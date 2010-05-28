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

package org.adjective.stout.operation;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.Instruction;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.ArrayType;
import org.adjective.stout.instruction.GenericInstruction;
import org.adjective.stout.instruction.IntInstruction;
import org.adjective.stout.instruction.TypeInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class CreateArrayExpression extends SmartExpression
{
    private static final GenericInstruction DUPLICATE = new GenericInstruction(Opcodes.DUP);
    private final ExtendedType _componentType;
    private final Expression[] _elements;

    public CreateArrayExpression(ExtendedType componentType, Expression[] elements)
    {
        _componentType = componentType;
        _elements = elements;
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return new ArrayType(_componentType);
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        addInstruction(collector, ConstantIntegerExpression.getInstruction(_elements.length));

        Instruction store;
        Class< ? > componentClass = _componentType.getRawClass();
        if (componentClass.isPrimitive())
        {
            addInstruction(collector, new IntInstruction(Opcodes.NEWARRAY, getTypeCode(componentClass)));
            store = new GenericInstruction(Type.getType(componentClass).getOpcode(Opcodes.IASTORE));
        }
        else
        {
            addInstruction(collector, new TypeInstruction(Opcodes.ANEWARRAY, _componentType.getInternalName()));
            store = new GenericInstruction(Opcodes.AASTORE);
        }

        for (int i = 0; i < _elements.length; i++)
        {
            addInstruction(collector, DUPLICATE);
            addInstruction(collector, ConstantIntegerExpression.getInstruction(i));
            _elements[i].getInstructions(stack, collector);
            addInstruction(collector, store);
        }

    }

    public static int getTypeCode(Class< ? > type)
    {
        if (type == Character.TYPE)
        {
            return Opcodes.T_CHAR;
        }
        if (type == Byte.TYPE)
        {
            return Opcodes.T_BYTE;
        }
        if (type == Integer.TYPE)
        {
            return Opcodes.T_INT;
        }
        if (type == Boolean.TYPE)
        {
            return Opcodes.T_BOOLEAN;
        }
        if (type == Short.TYPE)
        {
            return Opcodes.T_SHORT;
        }
        if (type == Long.TYPE)
        {
            return Opcodes.T_LONG;
        }
        if (type == Float.TYPE)
        {
            return Opcodes.T_FLOAT;
        }
        if (type == Double.TYPE)
        {
            return Opcodes.T_DOUBLE;
        }
        throw new IllegalArgumentException("Not a primitive " + type);
    }
}

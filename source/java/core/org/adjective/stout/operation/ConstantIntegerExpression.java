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

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.Instruction;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.ParameterisedClassImpl;
import org.adjective.stout.instruction.GenericInstruction;
import org.adjective.stout.instruction.IntInstruction;
import org.adjective.stout.instruction.LoadConstantInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ConstantIntegerExpression extends SmartExpression implements ElementBuilder<Expression>
{
    private static final ParameterisedClassImpl INT_TYPE = new ParameterisedClassImpl(Integer.TYPE);
    public static final ConstantIntegerExpression ZERO = new ConstantIntegerExpression(0);
    public static final ConstantIntegerExpression ONE = new ConstantIntegerExpression(1);

    private final int _value;

    public ConstantIntegerExpression(int value)
    {
        _value = value;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        addInstruction(collector, getInstruction(_value));
    }

    public static Instruction getInstruction(int value)
    {
        if (value >= -1 && value <= 5)
        {
            return new GenericInstruction(Opcodes.ICONST_0 + value);
        }
        if (value >= -128 && value < 127)
        {
            return new IntInstruction(Opcodes.BIPUSH, value);
        }
        if (value >= -32768 && value < 32767)
        {
            return new IntInstruction(Opcodes.SIPUSH, value);
        }
        return new LoadConstantInstruction(new Integer(value));
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return INT_TYPE;
    }

}

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

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.Operation;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.instruction.JumpInstruction;
import org.adjective.stout.instruction.TypeDescriptor;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class JumpOperation extends SmartOperation implements Operation
{
    private final int _op;
    private final Expression _expression;
    private final Label _label;

    public JumpOperation(int op, Expression expression, Label label)
    {
        _op = op;
        _expression = expression;
        _label = label;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        switch (_op)
        {
            case Opcodes.IFEQ:
            case Opcodes.IFNE:
            case Opcodes.IFLT:
            case Opcodes.IFGE:
            case Opcodes.IFGT:
            case Opcodes.IFLE:
                checkExpression(TypeDescriptor.INT, stack);
                break;
            case Opcodes.IFNULL:
            case Opcodes.IFNONNULL:
                checkExpression(TypeDescriptor.OBJECT, stack);
                break;
            default:
                throw new IllegalStateException("Do not know how to handle jump opcode " + getOpcodeDescription(_op));
        }
        _expression.getInstructions(stack, collector);
        collector.add(new JumpInstruction(_op, _label));
    }

    private void checkExpression(TypeDescriptor opType, ExecutionStack stack)
    {
        UnresolvedType expressionType = _expression.getExpressionType(stack);
        if (!opType.isCompatible(expressionType))
        {
            throw new OperationException("For the opcode "
                    + getOpcodeDescription(_op)
                    + " the provide expression must be "
                    + opType
                    + " but "
                    + expressionType
                    + " was provided");
        }
    }

}

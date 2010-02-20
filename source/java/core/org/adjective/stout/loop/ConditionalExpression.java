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

package org.adjective.stout.loop;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.Operation;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.instruction.JumpInstruction;
import org.adjective.stout.instruction.LabelInstruction;
import org.adjective.stout.operation.Expression;
import org.adjective.stout.operation.SmartExpression;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ConditionalExpression extends SmartExpression implements Expression
{
    private final Condition _condition;
    private final Expression _whenTrue;
    private final Expression _whenFalse;

    public ConditionalExpression(Condition condition, Expression whenTrue, Expression whenFalse)
    {
        _condition = condition;
        _whenTrue = whenTrue;
        _whenFalse = whenFalse;
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        // Take a best guess...
        return _whenTrue.getExpressionType(stack);
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        Label falseLabel = new Label();
        Label endLabel = new Label();
        Operation condition = _condition.jumpWhenFalse(falseLabel);
        condition.getInstructions(stack, collector);
        _whenTrue.getInstructions(stack, collector);
        addInstruction(collector, new JumpInstruction(Opcodes.GOTO, endLabel));
        addInstruction(collector, new LabelInstruction(falseLabel));
        _whenFalse.getInstructions(stack, collector);
        addInstruction(collector, new LabelInstruction(endLabel));
    }
}

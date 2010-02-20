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
import org.adjective.stout.core.ExecutionStack.Block;
import org.adjective.stout.instruction.JumpInstruction;
import org.adjective.stout.instruction.LabelInstruction;
import org.adjective.stout.operation.SmartStatement;
import org.adjective.stout.operation.Statement;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ForLoop extends SmartStatement
{
    private final Statement _initialiser;
    private final Condition _condition;
    private final Statement _increment;
    private final Statement[] _body;

    public ForLoop(Statement initialiser, Condition condition, Statement increment, Statement[] body)
    {
        _initialiser = initialiser;
        _condition = condition;
        _increment = increment;
        _body = body;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        Block block1 = stack.pushBlock();
        _initialiser.getInstructions(stack, collector);

        Label startLoop = new Label();
        addInstruction(collector,new LabelInstruction(startLoop));

        Label endLoop = new Label();
        _condition.jumpWhenFalse(endLoop).getInstructions(stack, collector);
        
        Label nextLoop = new Label();
        Block block2 = stack.pushBlock(nextLoop, endLoop);

        for (Statement stmt : _body)
        {
            stmt.getInstructions(stack, collector);
        }
        stack.popBlock(block2);

        addInstruction(collector,new LabelInstruction(nextLoop));
        _increment.getInstructions(stack, collector);
        addInstruction(collector,new JumpInstruction(Opcodes.GOTO, startLoop));
        
        addInstruction(collector,new LabelInstruction(endLoop));
        stack.popBlock(block1);
    }

}

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
import org.adjective.stout.core.ExecutionStack.Block;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.instruction.JumpInstruction;
import org.adjective.stout.operation.SmartStatement;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class BreakStatement extends SmartStatement
{
    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        for (Block block : stack.blocks())
        {
            Label label = block.breakLabel();
            if (label != null)
            {
                collector.add(new JumpInstruction(Opcodes.GOTO, label));
                return;
            }
        }
        throw new OperationException("No label to break to");
    }
    
    public Operation[] getChildren()
    {
        return NO_CHILDREN;
    }

}

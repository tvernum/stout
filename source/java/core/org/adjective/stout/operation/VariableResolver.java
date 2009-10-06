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

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.ExecutionStack.Block;
import org.adjective.stout.core.ExecutionStack.LocalVariable;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class VariableResolver
{
    public static class StackVariable
    {
        public final LocalVariable variable;
        public final int index;

        public StackVariable(LocalVariable var, int idx)
        {
            this.variable = var;
            this.index = idx;
        }
    }

    private final ExecutionStack _stack;

    public VariableResolver(ExecutionStack stack)
    {
        _stack = stack;
    }

    public StackVariable findVariable(String name)
    {
        for (Block block : _stack.blocks())
        {
            LocalVariable variable = block.getVariable(name);
            if (variable != null)
            {
                return new StackVariable(variable, block.getIndexInFrame(variable));
            }
        }
        return null;
    }

}

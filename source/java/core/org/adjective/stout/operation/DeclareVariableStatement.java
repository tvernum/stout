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
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.ExecutionStack.LocalVariable;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class DeclareVariableStatement extends SmartStatement implements LocalVariable
{
    private final ExtendedType _type;
    private final String _name;

    public DeclareVariableStatement(ExtendedType type, String name)
    {
        _type = type;
        _name = name;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        stack.currentBlock().declareVariable(this);
    }

    public String name()
    {
        return _name;
    }

    public ExtendedType type()
    {
        return _type;
    }

    public String toString()
    {
        return String.valueOf(_type) + ' ' + _name;
    }

}

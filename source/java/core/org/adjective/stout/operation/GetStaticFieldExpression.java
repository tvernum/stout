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

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.instruction.FieldInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class GetStaticFieldExpression extends SmartExpression
{
    private final UnresolvedType _from;
    private final String _name;
    private final UnresolvedType _type;

    public GetStaticFieldExpression(UnresolvedType from, String name, UnresolvedType type)
    {
        _from = from;
        _name = name;
        _type = type;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        addInstruction(collector,new FieldInstruction(Opcodes.GETSTATIC, _from.getInternalName(), _name, _type.getDescriptor()));
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return _type;
    }
}

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

package org.adjective.stout.instruction;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.util.AbstractVisitor;

import org.adjective.stout.core.Instruction;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class TypeInstruction extends AbstractInstruction implements Instruction
{
    private final String _type;

    public TypeInstruction(int opCode, String type)
    {
        super(opCode);
        _type = type;
    }

    public TypeInstruction(int opCode, UnresolvedType type)
    {
        this(opCode, type.getInternalName());
    }

    public void accept(MethodVisitor visitor)
    {
        visitor.visitTypeInsn(getOpCode(), _type);
    }

    public String toString()
    {
        return getClass().getSimpleName() + ' ' + AbstractVisitor.OPCODES[getOpCode()] + ' ' + _type;
    }

}

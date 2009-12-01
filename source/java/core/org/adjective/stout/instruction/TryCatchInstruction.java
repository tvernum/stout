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

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import org.adjective.stout.core.Instruction;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class TryCatchInstruction extends AbstractInstruction implements Instruction
{
    private final Label _start;
    private final Label _end;
    private final UnresolvedType _type;
    private final Label _handler;

    public TryCatchInstruction(Label start, Label end, UnresolvedType type, Label handler)
    {
        // There is no "try-catch" opcode, the handler table is encoded in the method header 
        super(0);
        _start = start;
        _end = end;
        _type = type;
        _handler = handler;
    }

    public void accept(MethodVisitor visitor)
    {
        visitor.visitTryCatchBlock(_start, _end, _handler, _type.getInternalName());
    }
}

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
import org.objectweb.asm.Opcodes;

import org.adjective.stout.core.Instruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class LabelInstruction extends AbstractInstruction implements Instruction
{
    private final Label _label;

    public LabelInstruction(Label label)
    {
        super(Opcodes.NOP);
        _label = label;
    }

    public void accept(MethodVisitor visitor)
    {
        visitor.visitLabel(_label);
    }

}

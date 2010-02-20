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
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.instruction.JumpInstruction;
import org.adjective.stout.instruction.LabelInstruction;
import org.adjective.stout.instruction.TryCatchInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class TryCatch extends SmartStatement
{
    private final Statement _body;
    private final Catch[] _catches;

    public static class Catch
    {
        final UnresolvedType _type;
        final Statement _code;

        public Catch(UnresolvedType type, Statement code)
        {
            _type = type;
            _code = code;
        }
    }

    public TryCatch(Statement body, Catch[] catches)
    {
        _body = body;
        _catches = catches;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        Label start = new Label();
        Label end = new Label();
        Label[] handlers = new Label[_catches.length];

        for (int i = 0; i < handlers.length; i++)
        {
            handlers[i] = new Label();
            addInstruction(collector,new TryCatchInstruction(start, end, _catches[i]._type, handlers[i]));
        }

        addInstruction(collector,new LabelInstruction(start));
        _body.getInstructions(stack, collector);
        addInstruction(collector,new LabelInstruction(end));

        Label done = new Label();
        addInstruction(collector,new JumpInstruction(Opcodes.GOTO, done));

        for (int i = 0; i < handlers.length; i++)
        {
            addInstruction(collector,new LabelInstruction(handlers[i]));
            _catches[i]._code.getInstructions(stack, collector);
            addInstruction(collector,new JumpInstruction(Opcodes.GOTO, done));
        }

        addInstruction(collector,new LabelInstruction(done));
    }

}

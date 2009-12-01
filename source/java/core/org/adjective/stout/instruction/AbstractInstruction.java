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

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.Instruction;
import org.adjective.stout.core.InstructionCollector;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public abstract class AbstractInstruction implements Instruction
{
//    public static class PopPush implements StackChange
//    {
//        private static final OperandType[] NOTHING = new OperandType[0];
//
//        private OperandType[] _pop = NOTHING;
//        private OperandType[] _push = NOTHING;
//
//        public OperandType[] pop()
//        {
//            return _pop;
//        }
//
//        public OperandType[] push()
//        {
//            return _push;
//        }
//
//        public PopPush pop(OperandType... pop)
//        {
//            if (_pop.length == 0)
//            {
//                _pop = pop;
//            }
//            else
//            {
//                _pop = add(_pop, pop);
//            }
//            return this;
//        }
//
//        private OperandType[] add(OperandType[] existing, OperandType[] add)
//        {
//            if (add.length == 0)
//            {
//                return existing;
//            }
//            OperandType[] resize = new OperandType[existing.length + add.length];
//            System.arraycopy(existing, 0, resize, 0, existing.length);
//            System.arraycopy(add, 0, resize, existing.length, add.length);
//            return resize;
//        }
//
//        public PopPush push(OperandType... push)
//        {
//            if (_push.length == 0)
//            {
//                _push = push;
//            }
//            else 
//            {
//                _push = add(_push, push);
//            }
//            return this;
//        }
//
//        public PopPush pop0()
//        {
//            return this;
//        }
//
//        public PopPush push0()
//        {
//            return this;
//        }
//
//        public StackChange push(String descriptor)
//        {
//            OperandType op = getOperandType(descriptor);
//            return push(op);
//        }
//
//        private OperandType getOperandType(String descriptor)
//        {
//            char ch = descriptor.charAt(0);
//            OperandType op = OperandType.forDescriptor(ch);
//            return op;
//        }
//
//        public PopPush pop(String descriptor)
//        {
//            return pop(getOperandType(descriptor));
//        }
//    }
//
    private final int _opCode;

    public AbstractInstruction(int opCode)
    {
        _opCode = opCode;
    }

    public int getOpCode()
    {
        return _opCode;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        collector.add(this);
    }

//    protected PopPush pop(OperandType... pop)
//    {
//        return new PopPush().pop(pop);
//    }
//    protected PopPush pop(String descriptor)
//    {
//        return new PopPush().pop(descriptor);
//    }

}

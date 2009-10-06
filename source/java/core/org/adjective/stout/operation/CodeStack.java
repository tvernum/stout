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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import org.adjective.stout.core.ClassDescriptor;
import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.MethodDescriptor;
import org.adjective.stout.core.MethodSignature;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class CodeStack implements ExecutionStack
{
    public class CodeBlock implements Block
    {
        private final int _startIndex;
        private final List<LocalVariable> _vars;
        private final Label _continueLabel;
        private final Label _breakLabel;

        public CodeBlock(int startIndex, Label continueLabel, Label breakLabel)
        {
            _startIndex = startIndex;
            _continueLabel = continueLabel;
            _breakLabel = breakLabel;
            _vars = new LinkedList<LocalVariable>();
        }

        public int declareVariable(LocalVariable var)
        {
            _vars.add(var);
            int index = _vars.size();
            Type varType = Type.getType(var.type().getDescriptor());
            if (varType.getSize() == 2)
            {
                _vars.add(null);
            }
            return index;
        }

        public int getIndexInFrame(LocalVariable var)
        {
            return _startIndex + _vars.indexOf(var);
        }

        public LocalVariable getVariable(String name)
        {
            for (LocalVariable var : _vars)
            {
                if (var.name().equals(name))
                {
                    return var;
                }
            }
            return null;
        }

        public Collection< ? extends LocalVariable> getVariables()
        {
            return Collections.unmodifiableCollection(_vars);
        }

        public int getNextIndex()
        {
            return _startIndex + _vars.size();
        }

        public Label breakLabel()
        {
            return _breakLabel;
        }

        public Label continueLabel()
        {
            return _continueLabel;
        }

    }

    private final ClassDescriptor _cls;
    private final MethodDescriptor _method;
    private final List<CodeBlock> _blocks;

    public CodeStack(ClassDescriptor cls, MethodDescriptor method)
    {
        _cls = cls;
        _method = method;
        _blocks = new ArrayList<CodeBlock>();
    }

    public Iterable< ? extends Block> blocks()
    {
        return _blocks;
    }

    public CodeBlock currentBlock()
    {
        return _blocks.get(0);
    }

    public ClassDescriptor currentClass()
    {
        return _cls;
    }

    public MethodSignature currentMethod()
    {
        return _method;
    }

    public void popBlock(Block block)
    {
        if (block != currentBlock())
        {
            throw new IllegalArgumentException("Attempt to pop block that is not the current block");
        }
        _blocks.remove(0);
    }

    public Block pushBlock()
    {
        return this.pushBlock(null, null);
    }

    public Block pushBlock(Label continueLabel, Label breakLabel)
    {
        int index = _blocks.isEmpty() ? 0 : currentBlock().getNextIndex();
        CodeBlock block = new CodeBlock(index, continueLabel, breakLabel);
        _blocks.add(0, block);
        return block;
    }

}

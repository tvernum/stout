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

import org.objectweb.asm.Type;

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.ParameterisedClass;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.ParameterisedClassImpl;
import org.adjective.stout.instruction.LoadConstantInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ConstantClassExpression extends SmartExpression implements Expression
{
    private static final ParameterisedClass CLASS_TYPE = new ParameterisedClassImpl(Class.class);

    private final UnresolvedType _class;

    public ConstantClassExpression(UnresolvedType cls)
    {
        _class = cls;
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return CLASS_TYPE;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        new LoadConstantInstruction(Type.getType(_class.getDescriptor())).getInstructions(stack, collector);
    }

}

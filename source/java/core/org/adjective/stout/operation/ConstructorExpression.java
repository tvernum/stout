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

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.builder.MethodSpec;
import org.adjective.stout.core.ConstructorSignature;
import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.ConstructorSignatureImpl;
import org.adjective.stout.instruction.GenericInstruction;
import org.adjective.stout.instruction.MethodInstruction;
import org.adjective.stout.instruction.TypeInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ConstructorExpression extends SmartExpression implements ElementBuilder<Expression>
{
    private final ConstructorSignature _constructor;
    private final Expression[] _expressions;

    public ConstructorExpression(ConstructorSignature constructor, Expression... expressions)
    {
        _constructor = constructor;
        _expressions = expressions;
    }

    public ConstructorExpression(UnresolvedType type, MethodSpec constructor, Expression... expressions)
    {
        this(new ConstructorSignatureImpl(type, constructor.getParameterTypes()), expressions);
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        addInstruction(collector,new TypeInstruction(Opcodes.NEW, _constructor.getType().getInternalName()));
        addInstruction(collector,new GenericInstruction(Opcodes.DUP));
        for (Expression expression : _expressions)
        {
            expression.getInstructions(stack, collector);
        }
        String descriptor = MethodInstruction.getMethodDescriptor(Void.TYPE, _constructor.getParameterTypes());
        addInstruction(collector,new MethodInstruction(Opcodes.INVOKESPECIAL, _constructor.getType().getInternalName(), "<init>", descriptor));
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return _constructor.getType();
    }

}

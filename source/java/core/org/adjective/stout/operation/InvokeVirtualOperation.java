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

import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.Operation;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.core.UnresolvedType.Sort;
import org.adjective.stout.instruction.MethodInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class InvokeVirtualOperation implements Operation
{
    private final Expression _target;
    private final UnresolvedType _targetType;
    private final MethodSignature _method;
    private final Expression[] _arguments;

    public InvokeVirtualOperation(Expression target, UnresolvedType targetType, MethodSignature method, Expression... arguments)
    {
        if (method.getModifiers().contains(ElementModifier.STATIC))
        {
            throw new IllegalArgumentException("The static method " + method.getName() + " cannot have a virtual invocation");
        }
        _target = target;
        _targetType = targetType;
        _method = method;
        _arguments = arguments;
        if (targetType == null && target != null)
        {
            throw new NullPointerException("Target type cannot be null when target expression is specified");
        }
    }

    public boolean hasReturnValue()
    {
        return getReturnType().getRawClass() != Void.TYPE;
    }

    public ExtendedType getReturnType()
    {
        return _method.getReturnType();
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        if (_target == null)
        {
            ThisExpression.INSTANCE.getInstructions(stack, collector);
        }
        else
        {
            _target.getInstructions(stack, collector);
        }

        for (Expression expression : _arguments)
        {
            expression.getInstructions(stack, collector);
        }
        UnresolvedType targetType = _targetType == null ? stack.currentClass() : _targetType;
        int opCode = targetType.getSort() == Sort.INTERFACE ? Opcodes.INVOKEINTERFACE : Opcodes.INVOKEVIRTUAL;
        collector.add(new MethodInstruction(opCode, targetType.getInternalName(), _method));
    }

}

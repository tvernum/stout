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
import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.instruction.MethodInstruction;
import org.adjective.stout.instruction.VarInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class InvokeSuperConstructorStatement extends SmartStatement implements ElementBuilder<Statement>
{
    public static final String CONSTRUCTOR_NAME = "<init>";

    private final MethodSignature _method;
    private final Expression[] _arguments;

    public InvokeSuperConstructorStatement(MethodSignature method, Expression[] arguments)
    {
        if (method.getReturnType().getRawClass() != Void.TYPE)
        {
            throw new IllegalArgumentException("Constructors may not have a return type (they must return void)");
        }
        if (!CONSTRUCTOR_NAME.equals(method.getName()))
        {
            throw new IllegalArgumentException("Constructors must be named '" + CONSTRUCTOR_NAME + "'");
        }
        if (arguments.length != method.getParameterTypes().length)
        {
            throw new IllegalArgumentException("Incorrect number of arguments to constructor " + method);
        }
        _method = method;
        _arguments = arguments;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        addInstruction(collector,new VarInstruction(Opcodes.ALOAD, 0));
        for (int i = 0; i < _arguments.length; i++)
        {
            Expression expr = _arguments[i];
            UnresolvedType expressionType = expr.getExpressionType(stack);
            UnresolvedType parameterType = _method.getParameterTypes()[i];
            if (!expressionType.canAssignTo(parameterType))
            {
                throw new OperationException("Cannot assign expression "
                        + expr
                        + " ("
                        + expressionType
                        + ") to parameter "
                        + i
                        + "("
                        + parameterType
                        + ") of "
                        + _method);
            }
            expr.getInstructions(stack, collector);
        }
        String superName = stack.currentClass().getSuperClass().getInternalName();
        new MethodInstruction(Opcodes.INVOKESPECIAL, superName, _method).getInstructions(stack, collector);
    }
}

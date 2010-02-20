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
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.instruction.GenericInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ReturnObjectStatement extends SmartStatement implements ElementBuilder<Statement>
{
    private final Expression _object;

    public ReturnObjectStatement(Expression object)
    {
        if (object == null)
        {
            throw new NullPointerException();
        }
        _object = object;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        checkReturnType(stack);
        _object.getInstructions(stack, collector);
        addInstruction(collector,new GenericInstruction(Opcodes.ARETURN));
    }

    private void checkReturnType(ExecutionStack stack)
    {
        MethodSignature method = stack.currentMethod();
        ExtendedType returnType = method.getReturnType();
        UnresolvedType objectType = _object.getExpressionType(stack);
        if (returnType.getSort() == UnresolvedType.Sort.PRIMITIVE)
        {
            throw new OperationException("Attempt to return object from a method (" + method + ") that returns a primitive (" + returnType + ")");
        }
        if (objectType.getSort() == UnresolvedType.Sort.PRIMITIVE)
        {
            throw new OperationException("Attempt to return primitive " + objectType + " as an Object");
        }
        if (objectType.getDescriptor().equals(returnType.getDescriptor()))
        {
            return;
        }
        if (!objectType.canAssignTo(returnType))
        {
            throw new OperationException("Attempt to return type "
                    + objectType
                    + " ("
                    + _object
                    + ") in method ("
                    + method
                    + ") returning "
                    + returnType);
        }
    }

}

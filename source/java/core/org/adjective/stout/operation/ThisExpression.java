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

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.instruction.VarInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ThisExpression extends SmartExpression
{
    public static final ThisExpression INSTANCE = new ThisExpression();
    public static final VarInstruction LOAD_THIS = new VarInstruction(Opcodes.ALOAD, 0);

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        MethodSignature method = stack.currentMethod();
        if (method.getModifiers().contains(ElementModifier.STATIC))
        {
            throw new OperationException("Cannot access 'this' inside a static method");
        }
        addInstruction(collector,LOAD_THIS);
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return stack.currentClass();
    }

}

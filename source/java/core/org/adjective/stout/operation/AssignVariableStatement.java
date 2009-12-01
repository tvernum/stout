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
import org.objectweb.asm.Type;

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.instruction.VarInstruction;
import org.adjective.stout.operation.VariableResolver.StackVariable;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class AssignVariableStatement extends SmartStatement
{
    private final String _name;
    private final Expression _expression;

    public AssignVariableStatement(String name, Expression expression)
    {
        _name = name;
        _expression = expression;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        StackVariable var = new VariableResolver(stack).findVariable(_name);
        if (var == null)
        {
            throw new OperationException("No such variable " + _name + " on stack");
        }
        getInstructions(stack, var, collector);
    }

    private void getInstructions(ExecutionStack stack, StackVariable var, InstructionCollector collector)
    {
        _expression.getInstructions(stack, collector);
        String descriptor = var.variable.type().getDescriptor();
        int opcode = Type.getType(descriptor).getOpcode(Opcodes.ISTORE);
        collector.add(new VarInstruction(opcode, var.index));
    }
}

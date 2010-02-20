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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.core.ExecutionStack.Block;
import org.adjective.stout.core.ExecutionStack.LocalVariable;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.instruction.VarInstruction;
import org.adjective.stout.operation.VariableResolver.StackVariable;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class LoadVariableExpression extends SmartExpression implements ElementBuilder<Expression>, Assignable
{
    private final String _name;

    public LoadVariableExpression(String name)
    {
        _name = name;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        StackVariable variable = getVariable(stack);
        String descriptor = variable.variable.type().getDescriptor();
        int opcode = Type.getType(descriptor).getOpcode(Opcodes.ILOAD);
        addInstruction(collector,new VarInstruction(opcode, variable.index));
    }

    private StackVariable getVariable(ExecutionStack stack)
    {
        StackVariable variable = new VariableResolver(stack).findVariable(_name);
        if (variable == null)
        {
            throw new OperationException("No such variable " + _name + ". Variables are: " + getVariableNames(stack));
        }
        return variable;
    }

    private Collection<String> getVariableNames(ExecutionStack stack)
    {
        Set<String> names = new HashSet<String>();
        for (Block block : stack.blocks())
        {
            for (LocalVariable variable : block.getVariables())
            {
                names.add(variable.name());
            }
        }
        return names;
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return getVariable(stack).variable.type();
    }

    public String getVariableName()
    {
        return _name;
    }

    public Statement assign(Expression value)
    {
        return new AssignVariableStatement(_name, value);
    }

    public String toString()
    {
        return getClass().getSimpleName() + "{" + _name + "}";
    }

}

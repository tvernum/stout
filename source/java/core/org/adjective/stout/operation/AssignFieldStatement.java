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
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.instruction.FieldInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class AssignFieldStatement extends SmartStatement implements ElementBuilder<Statement>
{
    private final UnresolvedType _owner;
    private final Expression _target;
    private final String _fieldName;
    private final UnresolvedType _fieldType;
    private final Expression _expression;

    public AssignFieldStatement(UnresolvedType owner, Expression target, String fieldName, UnresolvedType fieldType, Expression expression)
    {
        _owner = owner;
        _target = target;
        _fieldName = fieldName;
        _fieldType = fieldType;
        _expression = expression;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        UnresolvedType owner = _owner;
        if (owner == null)
        {
            owner = stack.currentClass();
        }
        UnresolvedType type = _fieldType;
        if (type == null)
        {
            type = owner.getFieldType(_fieldName);
            if (type == null)
            {
                throw new OperationException("No such field " + _fieldName + " in " + owner);
            }
        }

        if (_target == null)
        {
            addInstruction(collector,ThisExpression.LOAD_THIS);
        }
        else
        {
            _target.getInstructions(stack, collector);
        }
        _expression.getInstructions(stack, collector);
        addInstruction(collector,new FieldInstruction(Opcodes.PUTFIELD, owner.getInternalName(), _fieldName, type.getDescriptor()));
    }
}

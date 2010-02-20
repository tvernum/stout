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

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.instruction.EmptyInstruction;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class LineNumberExpression implements Expression
{
    private final Expression _expression;
    private final int _line;

    public LineNumberExpression(Expression expression, int line)
    {
        _expression = expression;
        _line = line;
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return _expression.getExpressionType(stack);
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        collector.add(EmptyInstruction.INSTANCE, _line);
        _expression.getInstructions(stack, collector);
    }

}

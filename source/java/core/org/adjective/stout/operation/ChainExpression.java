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
import org.adjective.stout.core.Operation;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ChainExpression extends SmartExpression implements Expression
{
    private final Operation[] _first;
    private final Operation[] _second;
    private final Expression _expression;

    public ChainExpression(Expression expression, Statement... statements)
    {
        _expression = expression;
        _first = new Operation[] { expression };
        _second = statements;
    }

    public ChainExpression(Statement[] statementArray, Expression expression)
    {
        _expression = expression;
        _first = statementArray;
        _second = new Operation[] { expression };
    }

    public UnresolvedType getExpressionType(ExecutionStack stack)
    {
        return _expression.getExpressionType(stack);
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        for (Operation op : _first)
        {
            op.getInstructions(stack, collector);
        }
        for (Operation op : _second)
        {
            op.getInstructions(stack, collector);
        }
    }

}

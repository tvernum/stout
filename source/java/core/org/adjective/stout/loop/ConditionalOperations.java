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

package org.adjective.stout.loop;

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.operation.Expression;
import org.adjective.stout.operation.Statement;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ConditionalOperations
{
    public ForLoopSpec forLoop()
    {
        return new ForLoopSpec();
    }

    public WhileLoopSpec whileLoop()
    {
        return new WhileLoopSpec();
    }

    public ElementBuilder<Condition> always()
    {
        return AlwaysCondition.INSTANCE;
    }

    public ElementBuilder<Condition> expression(ElementBuilder<Expression> variable)
    {
        return expression(variable.create());
    }

    public ElementBuilder<Condition> expression(Expression expression)
    {
        return new ExpressionCondition(expression);
    }

    public DoWhileLoopSpec doWhile()
    {
        return new DoWhileLoopSpec();
    }

    public IterableLoopSpec iterable()
    {
        return new IterableLoopSpec();
    }

    public IfElseSpec ifElse()
    {
        return new IfElseSpec();
    }

    public ElementBuilder< ? extends Statement> breakLoop()
    {
        return new BreakStatement();
    }

    public ElementBuilder< ? extends Statement> continueLoop()
    {
        return new ContinueStatement();
    }

    public Expression conditional(Condition condition, Expression whenTrue, Expression whenFalse)
    {
        return new ConditionalExpression(condition, whenTrue, whenFalse);
    }

    public Condition isNull(Expression expr)
    {
        return new IsNullCondition(expr);
    }

}

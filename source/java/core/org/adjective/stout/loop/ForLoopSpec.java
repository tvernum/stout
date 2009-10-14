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

import java.util.List;

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.operation.EmptyStatement;
import org.adjective.stout.operation.Statement;
import org.adjective.stout.operation.StatementOperations;

import static org.adjective.stout.operation.StatementOperations.toStatementArray;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ForLoopSpec implements ElementBuilder<ForLoop>
{
    private Statement _initialiser;
    private Condition _condition;
    private Statement _increment;
    private Statement[] _body;

    public ForLoopSpec()
    {
        _initialiser = EmptyStatement.INSTANCE;
        _condition = AlwaysCondition.INSTANCE;
        _increment = EmptyStatement.INSTANCE;
        _body = new Statement[0];
    }

    public ForLoop create()
    {
        return new ForLoop(_initialiser, _condition, _increment, _body);
    }

    public ForLoopSpec withInitialiser(ElementBuilder<Statement> initialiser)
    {
        return withInitialiser(initialiser.create());
    }

    public ForLoopSpec withInitialiser(Statement initialiser)
    {
        _initialiser = initialiser;
        return this;
    }

    public ForLoopSpec withCondition(ElementBuilder<Condition> condition)
    {
        return withCondition(condition.create());
    }

    public ForLoopSpec withCondition(Condition condition)
    {
        _condition = condition;
        return this;
    }

    public ForLoopSpec withIncrement(ElementBuilder<Statement> increment)
    {
        return withIncrement(increment.create());
    }

    public ForLoopSpec withIncrement(Statement increment)
    {
        _increment = increment;
        return this;
    }

    public ForLoopSpec withBody(ElementBuilder<Statement>... body)
    {
        return withBody(toStatementArray(body));
    }

    public ForLoopSpec withBody(Statement... body)
    {
        _body = body;
        return this;
    }

    public ForLoopSpec withBody(List<ElementBuilder< ? extends Statement>> body)
    {
        return withBody(StatementOperations.toStatementArray(body));
    }

}

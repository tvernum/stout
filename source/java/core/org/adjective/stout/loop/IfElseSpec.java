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

import java.util.Collection;

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.operation.Statement;
import org.adjective.stout.operation.StatementOperations;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class IfElseSpec implements ElementBuilder<IfElse>
{
    private Condition _condition;
    private Statement[] _true;
    private Statement[] _false;

    public IfElse create()
    {
        return new IfElse(_condition, _true, _false);
    }

    public IfElseSpec withCondition(ElementBuilder<Condition> condition)
    {
        return withCondition(condition.create());
    }

    public IfElseSpec withCondition(Condition condition)
    {
        _condition = condition;
        return this;
    }

    public IfElseSpec whenTrue(Collection< ? extends ElementBuilder< ? extends Statement>> body)
    {
        return whenTrue(StatementOperations.toStatementArray(body));
    }

    public IfElseSpec whenTrue(Statement... body)
    {
        _true = body;
        return this;
    }

    public IfElseSpec whenFalse(Collection< ? extends ElementBuilder< ? extends Statement>> body)
    {
        return whenFalse(StatementOperations.toStatementArray(body));
    }

    public IfElseSpec whenFalse(Statement[] body)
    {
        _false = body;
        return this;
    }
}

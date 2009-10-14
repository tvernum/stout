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
public class DoWhileLoopSpec implements ElementBuilder<DoWhileLoop>
{
    private Statement[] _body;
    private Condition _condition;

    public DoWhileLoop create()
    {
        return new DoWhileLoop(_condition, _body);
    }

    public DoWhileLoopSpec withCondition(ElementBuilder<Condition> condition)
    {
        return withCondition(condition.create());
    }

    public DoWhileLoopSpec withCondition(Condition condition)
    {
        _condition = condition;
        return this;
    }

    public DoWhileLoopSpec withBody(Collection< ? extends ElementBuilder< ? extends Statement>> body)
    {
        return withBody(StatementOperations.toStatementArray(body));
    }

    public DoWhileLoopSpec withBody(Statement[] body)
    {
        _body = body;
        return this;
    }
}

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
import org.adjective.stout.operation.Expression;
import org.adjective.stout.operation.Statement;
import org.adjective.stout.operation.StatementOperations;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class IterableLoopSpec implements ElementBuilder<IterableLoop>
{
    private Statement[] _body;
    private Expression _iterable;
    private String _iteratorName;
    private String _valueName;

    public IterableLoop create()
    {
        return new IterableLoop(_iterable, _iteratorName, _valueName, _body);
    }

    public IterableLoopSpec withIterable(Expression iterable)
    {
        _iterable = iterable;
        return this;
    }

    public IterableLoopSpec withIteratorName(Expression iterable)
    {
        _iterable = iterable;
        return this;
    }

    public IterableLoopSpec withBody(Collection< ? extends ElementBuilder< ? extends Statement>> body)
    {
        return withBody(StatementOperations.toStatementArray(body));
    }

    public IterableLoopSpec withBody(Statement... body)
    {
        _body = body;
        return this;
    }

    public IterableLoopSpec withVariableName(String var)
    {
        _valueName = var;
        return this;
    }
}

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

import java.util.ArrayList;
import java.util.List;

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.operation.TryCatch.Catch;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class TryCatchSpec implements ElementBuilder<TryCatch>
{
    private final Statement _body;
    private final List<TryCatch.Catch> _catches;

    public TryCatchSpec(Statement body)
    {
        _body = body;
        _catches = new ArrayList<TryCatch.Catch>(3);
    }

    public TryCatch create()
    {
        Catch[] array = _catches.toArray(new TryCatch.Catch[_catches.size()]);
        return new TryCatch(_body, array);
    }

    public TryCatchSpec on(UnresolvedType exception, ElementBuilder<Statement> body)
    {
        TryCatch.Catch c = new TryCatch.Catch(exception, body.create());
        _catches.add(c);
        return this;
    }

    public TryCatchSpec on(UnresolvedType exception, ElementBuilder<Statement>... body)
    {
        return on(exception, VM.Statement.block(body));
    }

}

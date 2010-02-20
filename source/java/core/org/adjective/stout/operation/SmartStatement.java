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

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.core.Operation;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public abstract class SmartStatement extends AbstractOperation implements ElementBuilder<Statement>, Statement
{
    protected static final Operation[] NO_CHILDREN = new Operation[0];

    public Statement create()
    {
        return this;
    }
}

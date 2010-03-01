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

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.core.Operation;
import org.adjective.stout.operation.Expression;
import org.adjective.stout.operation.JumpOperation;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class IsNullCondition extends SmartCondition implements ElementBuilder<Condition>
{
    private final Expression _expression;

    public IsNullCondition(Expression expression)
    {
        _expression = expression;
    }

    public Operation jumpWhenFalse(Label label)
    {
        return new JumpOperation(Opcodes.IFNONNULL, _expression, label);
    }

    public Operation jumpWhenTrue(Label label)
    {
        return new JumpOperation(Opcodes.IFNULL, _expression, label);
    }

}

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

package org.adjective.stout.impl;

import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.ParameterisedClass.TypeSpecification;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class TypeSpecificationImpl implements TypeSpecification
{
    private final Bound _bound;
    private final ExtendedType _type;

    public TypeSpecificationImpl(Bound bound, ExtendedType type)
    {
        _bound = bound;
        _type = type;
    }

    public TypeSpecificationImpl(Bound bound, Class< ? > type)
    {
        this(bound, new ParameterisedClassImpl(type));
    }

    public TypeSpecificationImpl(Class< ? > type)
    {
        this(Bound.DIRECT, type);
    }

    public Bound getBound()
    {
        return _bound;
    }

    public ExtendedType getType()
    {
        return _type;
    }

}

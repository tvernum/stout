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

import org.adjective.stout.core.ConstructorSignature;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ConstructorSignatureImpl implements ConstructorSignature
{
    private final UnresolvedType _type;
    private final UnresolvedType[] _parameterTypes;

    public ConstructorSignatureImpl(Class< ? > type, Class< ? >... parameterTypes)
    {
        this(new ParameterisedClassImpl(type), ParameterisedClassImpl.getArray(parameterTypes));
    }

    public ConstructorSignatureImpl(UnresolvedType type, UnresolvedType[] parameterTypes)
    {
        _type = type;
        _parameterTypes = parameterTypes;
    }

    public UnresolvedType[] getParameterTypes()
    {
        return _parameterTypes;
    }

    public UnresolvedType getType()
    {
        return _type;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(_type);
        if (_parameterTypes.length == 0)
        {
            builder.append("( )");
        }
        else
        {
            builder.append('(');
            for (UnresolvedType arg : _parameterTypes)
            {
                builder.append(arg);
                builder.append(',');
            }
            builder.setCharAt(builder.length() - 1, ')');
        }
        return builder.toString();
    }

}

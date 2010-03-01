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

import java.util.List;
import java.util.Set;

import org.adjective.stout.core.AnnotationDescriptor;
import org.adjective.stout.core.Code;
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.MethodDescriptor;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.Parameter;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class MethodImpl implements MethodDescriptor
{
    private final Set<ElementModifier> _modifiers;
    private final String _name;
    private final ExtendedType _returnType;
    private final Parameter[] _parameters;
    private final AnnotationDescriptor[] _annotations;
    private final Code _body;
    private final ExtendedType[] _exceptions;

    public MethodImpl(Set<ElementModifier> modifiers, String name, ExtendedType returnType, List<Parameter> parameters,
            List<AnnotationDescriptor> annotations, ExtendedType[] exceptions, Code body)
    {
        _modifiers = modifiers;
        _name = name;
        _returnType = returnType;
        _parameters = parameters.toArray(new Parameter[parameters.size()]);
        _annotations = annotations.toArray(new AnnotationDescriptor[annotations.size()]);
        _exceptions = exceptions;
        _body = body;
    }

    public Set<ElementModifier> getModifiers()
    {
        return _modifiers;
    }

    public String getName()
    {
        return _name;
    }

    public ExtendedType getReturnType()
    {
        return _returnType;
    }

    public Parameter[] getParameters()
    {
        return _parameters;
    }

    public AnnotationDescriptor[] getAnnotations()
    {
        return _annotations;
    }

    public Code getBody()
    {
        return _body;
    }

    public UnresolvedType[] getParameterTypes()
    {
        UnresolvedType[] types = new UnresolvedType[_parameters.length];
        for (int i = 0; i < types.length; i++)
        {
            types[i] = _parameters[i].getType();
        }
        return types;
    }

    public ExtendedType[] getExceptions()
    {
        return _exceptions;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(_returnType);
        builder.append(' ');
        builder.append(_name);
        if (_parameters.length > 0)
        {
            builder.append('(');
            for (Parameter param : _parameters)
            {
                builder.append(param.getType());
                builder.append(' ');
                builder.append(param.getName());
                builder.append(',');
            }
            builder.setCharAt(builder.length() - 1, ')');
        }
        else
        {
            builder.append("( )");
        }
        return builder.toString();
    }

}

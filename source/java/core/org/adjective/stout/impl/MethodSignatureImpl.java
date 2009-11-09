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

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class MethodSignatureImpl implements MethodSignature
{
    private final Set<ElementModifier> _modifiers;
    private final String _name;
    private final UnresolvedType[] _parameters;
    private final ExtendedType _returnType;

    public MethodSignatureImpl(Set<ElementModifier> modifiers, ExtendedType returnType, String name, UnresolvedType[] parameters)
    {
        _modifiers = modifiers;
        _returnType = returnType;
        _name = name;
        _parameters = parameters;
    }

    public MethodSignatureImpl(Method method)
    {
        _modifiers = new HashSet<ElementModifier>();
        for (ElementModifier modifier : ElementModifier.values())
        {
            int code = modifier.getCode();
            if ((method.getModifiers() & code) == code)
            {
                _modifiers.add(modifier);
            }
        }
        _returnType = new ParameterisedClassImpl(method.getReturnType());
        _name = method.getName();
        _parameters = ParameterisedClassImpl.getArray(method.getParameterTypes());
    }

    public Set<ElementModifier> getModifiers()
    {
        return _modifiers;
    }

    public String getName()
    {
        return _name;
    }

    public UnresolvedType[] getParameterTypes()
    {
        return _parameters;
    }

    public ExtendedType getReturnType()
    {
        return _returnType;
    }

}

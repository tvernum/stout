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

package org.adjective.stout.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.adjective.stout.core.AnnotationDescriptor;
import org.adjective.stout.core.Code;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.MethodDescriptor;
import org.adjective.stout.core.Parameter;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.CodeImpl;
import org.adjective.stout.impl.MethodImpl;
import org.adjective.stout.impl.ParameterisedClassImpl;
import org.adjective.stout.operation.Statement;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class MethodSpec implements ElementBuilder<MethodDescriptor>
{
    private final String _name;
    private final Set<ElementModifier> _modifiers;
    private final List<AnnotationDescriptor> _annotations;
    private ExtendedType _returnType;
    private List<Parameter> _parameters;
    private Code _body;

    public MethodSpec(String name)
    {
        _name = name;
        _modifiers = new HashSet<ElementModifier>();
        _annotations = new ArrayList<AnnotationDescriptor>();
        _returnType = new ParameterisedClassImpl(Void.TYPE);
        _parameters = Collections.<Parameter> emptyList();
    }

    public MethodDescriptor create()
    {
        ExtendedType[] exceptions = new ExtendedType[0]; // @TODO
        return new MethodImpl(_modifiers, _name, _returnType, _parameters, _annotations, exceptions, _body);
    }

    public MethodSpec withModifiers(ElementModifier... modifiers)
    {
        return withModifiers(Arrays.asList(modifiers));
    }

    public MethodSpec withModifiers(Collection<ElementModifier> modifiers)
    {
        _modifiers.addAll(modifiers);
        return this;
    }

    public MethodSpec withReturnType(ExtendedType type)
    {
        _returnType = type;
        return this;
    }

    public MethodSpec withReturnType(Class< ? > type)
    {
        return withReturnType(new ParameterisedClassImpl(type));
    }

    public MethodSpec withParameters(ElementBuilder< ? extends Parameter>... parameters)
    {
        List<Parameter> list = new ArrayList<Parameter>();
        for (ElementBuilder< ? extends Parameter> builder : parameters)
        {
            list.add(builder.create());
        }
        return withParameters(list);
    }

    public MethodSpec withParameters(Parameter... parameters)
    {
        return withParameters(Arrays.asList(parameters));
    }

    public MethodSpec withParameters(List<Parameter> parameters)
    {
        _parameters = new ArrayList<Parameter>(parameters);
        return this;
    }

    public UnresolvedType[] getParameterTypes()
    {
        UnresolvedType[] types = new UnresolvedType[_parameters.size()];
        for (int i = 0; i < types.length; i++)
        {
            types[i] = _parameters.get(i).getType();
        }
        return types;
    }

    public MethodSpec withAnnotation(ElementBuilder< ? extends AnnotationDescriptor> builder)
    {
        return withAnnotation(builder.create());
    }

    public MethodSpec withAnnotation(AnnotationDescriptor annotation)
    {
        _annotations.add(annotation);
        return this;
    }

    public MethodSpec withBody(ElementBuilder< ? extends Code> body)
    {
        return withBody(body.create());
    }

    public MethodSpec withBody(Code body)
    {
        _body = body;
        return this;
    }

    public MethodSpec withBody(Statement... operations)
    {
        Code code = new CodeImpl(operations);
        return withBody(code);
    }

    public MethodSpec withBody(ElementBuilder< ? extends Statement>... builders)
    {
        Statement[] operations = new Statement[builders.length];
        for (int i = 0; i < operations.length; i++)
        {
            operations[i] = builders[i].create();
        }
        return withBody(operations);
    }

    public MethodSpec withBody(List< ? extends ElementBuilder< ? extends Statement>> builders)
    {
        Statement[] operations = new Statement[builders.size()];
        for (int i = 0; i < operations.length; i++)
        {
            operations[i] = builders.get(i).create();
        }
        return withBody(operations);
    }
}

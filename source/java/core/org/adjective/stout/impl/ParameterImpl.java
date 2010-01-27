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

import java.util.Arrays;

import org.adjective.stout.core.AnnotationDescriptor;
import org.adjective.stout.core.Parameter;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ParameterImpl implements Parameter
{
    private final String _name;
    private final UnresolvedType _type;
    private final AnnotationDescriptor[] _annotations;

    public ParameterImpl(String name, UnresolvedType type, AnnotationDescriptor[] annotations)
    {
        _name = name;
        _type = type;
        _annotations = annotations;
    }

    public AnnotationDescriptor[] getAnnotations()
    {
        return _annotations;
    }

    public String getName()
    {
        return _name;
    }

    public UnresolvedType getType()
    {
        return _type;
    }

    public String toString()
    {
        return Arrays.toString(_annotations) + ' ' + _type + ' ' + _name;
    }
}

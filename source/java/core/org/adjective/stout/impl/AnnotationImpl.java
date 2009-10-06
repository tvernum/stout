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

import java.lang.annotation.Annotation;

import org.adjective.stout.core.AnnotationDescriptor;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class AnnotationImpl implements AnnotationDescriptor
{
    private final Class< ? extends Annotation> _type;
    private final Attribute[] _attributes;
    private final boolean _runtime;

    public AnnotationImpl(Class< ? extends Annotation> type, Attribute[] attributes, boolean runtime)
    {
        _type = type;
        _attributes = attributes;
        _runtime = runtime;
    }

    public Attribute[] getAttributes()
    {
        return _attributes;
    }

    public Class< ? extends Annotation> getType()
    {
        return _type;
    }

    public boolean isRuntime()
    {
        return _runtime;
    }

}

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
import java.util.HashSet;
import java.util.List;

import org.adjective.stout.core.AnnotationDescriptor;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.FieldDescriptor;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.FieldImpl;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class FieldSpec implements ElementBuilder<FieldDescriptor>
{
    private final String _name;
    private UnresolvedType _type;
    private final List<AnnotationDescriptor> _annotations;

    public FieldSpec(String name)
    {
        _name = name;
        _annotations = new ArrayList<AnnotationDescriptor>();
    }

    public FieldDescriptor create()
    {
        return new FieldImpl(new HashSet<ElementModifier>(), _type, _name, new AnnotationDescriptor[0]);
    }

    public FieldSpec withType(UnresolvedType type)
    {
        _type = type;
        return this;
    }

    public FieldSpec withAnnotation(ElementBuilder< ? extends AnnotationDescriptor> builder)
    {
        return withAnnotation(builder.create());
    }

    public FieldSpec withAnnotation(AnnotationDescriptor annotation)
    {
        _annotations.add(annotation);
        return this;
    }

}

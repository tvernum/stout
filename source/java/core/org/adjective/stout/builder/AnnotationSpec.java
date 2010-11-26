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

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.adjective.stout.core.AnnotationDescriptor;
import org.adjective.stout.core.AnnotationDescriptor.Attribute;
import org.adjective.stout.exception.BuilderException;
import org.adjective.stout.impl.AnnotationImpl;
import org.adjective.stout.impl.AttributeImpl;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class AnnotationSpec implements ElementBuilder<AnnotationDescriptor>
{
    private final Class< ? extends Annotation> _type;
    private final List<Attribute> _attributes;
    private boolean _runtime;

    public AnnotationSpec(Class< ? extends Annotation> type)
    {
        _type = type;
        _runtime = isRuntimeAnnotation(type);

        Method[] methods = _type.getMethods();
        _attributes = new ArrayList<Attribute>(methods.length);
        for (Method method : methods)
        {
            Object defaultValue = method.getDefaultValue();
            if (defaultValue != null)
            {
                withAttribute(method.getName(), defaultValue);
            }
        }
    }

    public static boolean isRuntimeAnnotation(Class< ? extends Annotation> type)
    {
        Retention retention = type.getAnnotation(Retention.class);
        if (retention != null)
        {
            return retention.value() == RetentionPolicy.RUNTIME;
        }
        return false;
    }

    public AnnotationDescriptor create()
    {
        return new AnnotationImpl(_type, _attributes.toArray(new Attribute[0]), _runtime);
    }

    public AnnotationSpec withAttribute(String name, Object value)
    {
        if (value == null)
        {
            value = getDefaultValue(name);
        }
        else
        {
            verifyAttributeExists(name);
        }
        _attributes.add(new AttributeImpl(name, value));
        return this;
    }

    private Object getDefaultValue(String name)
    {
        try
        {
            Method method = getMethod(name);
            return method.getDefaultValue();
        }
        catch (RuntimeException e)
        {
            throw e;
        }
    }

    private void verifyAttributeExists(String name)
    {
        getMethod(name);
    }

    private Method getMethod(String name)
    {
        try
        {
            return _type.getMethod(name);
        }
        catch (NoSuchMethodException e)
        {
            throw new BuilderException("The annotation " + _type + " does have an attribute '" + name + "'");
        }
    }

    public AnnotationSpec withRuntimeVisibility(boolean runtime)
    {
        _runtime = runtime;
        return this;
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append("{ type:");
        builder.append(_type);
        builder.append("; runtime:");
        builder.append(_runtime);
        builder.append("; attributes:[");
        for (AnnotationDescriptor.Attribute attribute : this._attributes)
        {
            builder.append(attribute.getName());
            builder.append('=');
            builder.append(attribute.getValue());
            builder.append(',');
        }
        builder.append("]}");
        return builder.toString();
    }

}

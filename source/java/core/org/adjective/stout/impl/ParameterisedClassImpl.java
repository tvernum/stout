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

import org.objectweb.asm.Type;

import org.adjective.stout.core.ParameterisedClass;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ParameterisedClassImpl implements ParameterisedClass
{
    private final Class< ? > _class;
    private final TypeSpecification[] _parameters;

    public ParameterisedClassImpl(Class< ? > cls)
    {
        this(cls, new TypeSpecification[0]);
    }

    public ParameterisedClassImpl(Class< ? > cls, TypeSpecification... typeSpecifications)
    {
        _class = cls;
        _parameters = typeSpecifications;
    }

    public ParameterisedClassImpl(Class< ? > cls, Class< ? >... typeSpecifications)
    {
        this(cls, toTypeSpec(typeSpecifications));
    }

    private static TypeSpecification[] toTypeSpec(Class< ? >[] cls)
    {
        TypeSpecificationImpl[] spec = new TypeSpecificationImpl[cls.length];
        for (int i = 0; i < cls.length; i++)
        {
            spec[i] = new TypeSpecificationImpl(cls[i]);
        }
        return spec;
    }

    public Class< ? > getRawClass()
    {
        return _class;
    }

    public TypeSpecification[] getTypeParameters()
    {
        return _parameters;
    }

    public String getInternalName()
    {
        return Type.getInternalName(_class);
    }

    public String getDescriptor()
    {
        return Type.getDescriptor(_class);
    }

    public static ParameterisedClassImpl[] getArray(Class< ? >[] cls)
    {
        ParameterisedClassImpl[] pci = new ParameterisedClassImpl[cls.length];
        for (int i = 0; i < cls.length; i++)
        {
            pci[i] = new ParameterisedClassImpl(cls[i]);
        }
        return pci;
    }

    public Sort getSort()
    {
        if (_class.isAnnotation())
        {
            return Sort.ANNOTATION;
        }
        if (_class.isArray())
        {
            return Sort.ARRAY;
        }
        if (_class.isPrimitive())
        {
            return Sort.PRIMITIVE;
        }
        if (_class.isEnum())
        {
            return Sort.ENUM;
        }
        if (_class.isInterface())
        {
            return Sort.INTERFACE;
        }
        return Sort.CLASS;
    }

    public String toString()
    {
        if (_parameters.length == 0)
        {
            return _class.getName();
        }
        else
        {
            StringBuilder builder = new StringBuilder();
            builder.append(_class.getName());
            builder.append("<");
            for (TypeSpecification type : _parameters)
            {
                builder.append(type);
                builder.append(',');
            }
            builder.setCharAt(builder.length() - 1, '>');
            return builder.toString();
        }
    }

    public boolean canAssignTo(UnresolvedType type)
    {
        return canAssign(_class, type);
    }

    private boolean canAssign(Class< ? > cls, UnresolvedType type)
    {
        if (cls == null)
        {
            return false;
        }
        if (Type.getDescriptor(cls).equals(type.getDescriptor()))
        {
            return true;
        }
        if (canAssign(cls.getSuperclass(), type))
        {
            return true;
        }
        for (Class< ? > ifc : cls.getInterfaces())
        {
            if (canAssign(ifc, type))
            {
                return true;
            }
        }
        return false;
    }

    public UnresolvedType getFieldType(String fieldName)
    {
        try
        {
            return new ParameterisedClassImpl(_class.getField(fieldName).getType());
        }
        catch (SecurityException e)
        {
            return null;
        }
        catch (NoSuchFieldException e)
        {
            return null;
        }
    }
}

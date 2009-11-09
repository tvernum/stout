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

import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ArrayType implements UnresolvedType
{
    private final UnresolvedType _componentType;

    public ArrayType(UnresolvedType componentType)
    {
        _componentType = componentType;
    }

    public String getDescriptor()
    {
        return "[" + _componentType.getDescriptor();
    }

    public String getInternalName()
    {
        return getDescriptor();
    }

    public Sort getSort()
    {
        return Sort.ARRAY;
    }

    public boolean canAssignTo(UnresolvedType type)
    {
        if (isObject(type))
        {
            return true;
        }
        if (type.getSort() != Sort.ARRAY)
        {
            return false;
        }
        if (_componentType.getSort() == Sort.PRIMITIVE)
        {
            return this.getDescriptor().equals(type.getDescriptor());
        }
        // @TODO - Not really true, but a good start..
        return true;
    }

    private boolean isObject(UnresolvedType type)
    {
        return Type.getInternalName(Object.class).equals(type.getInternalName());
    }

    public UnresolvedType getFieldType(String fieldName)
    {
        return null;
    }

}

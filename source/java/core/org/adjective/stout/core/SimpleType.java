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

package org.adjective.stout.core;

import org.adjective.stout.operation.UnknownType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class SimpleType implements UnresolvedType
{
    private final Sort _sort;
    private final String _name;

    public SimpleType(Sort sort, String pkg, String name)
    {
        this(sort, pkg + "." + name);
    }

    public SimpleType(Sort sort, String fqName)
    {
        _sort = sort;
        _name = fqName;
    }

    public boolean canAssignTo(UnresolvedType type)
    {
        return type.getInternalName().equals(this.getInternalName());
    }

    public String getInternalName()
    {
        return _name.replace('.', '/');
    }

    public String getDescriptor()
    {
        return "L" + getInternalName() + ";";
    }

    public UnresolvedType getFieldType(String fieldName)
    {
        return UnknownType.INSTANCE;
    }

    public Sort getSort()
    {
        return _sort;
    }

}

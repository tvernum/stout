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

package org.adjective.stout.instruction;

import org.objectweb.asm.Type;

import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public enum TypeDescriptor
{
    VOID('V'), BOOLEAN('Z'), CHAR('C'), BYTE('B'), SHORT('S'), INT('I'), FLOAT('F'), LONG('J'), DOUBLE('D'), ARRAY('['), OBJECT('L');

    private final char _code;

    private TypeDescriptor(char code)
    {
        _code = code;
    }

    public char getCode()
    {
        return _code;
    }

    public boolean isType(Type type)
    {
        return type.getDescriptor().charAt(0) == _code;
    }

    public boolean isType(UnresolvedType type)
    {
        return type.getDescriptor().charAt(0) == _code;
    }

    public boolean isCompatible(UnresolvedType type)
    {
        if (isType(type))
        {
            return true;
        }
        switch (this)
        {
            case BOOLEAN:
            case BYTE:
            case SHORT:
            case INT:
                switch (type.getDescriptor().charAt(0))
                {
                    case 'Z':
                    case 'B':
                    case 'S':
                    case 'I':
                        return true;
                }
                break;
        }
        return false;
    }
}

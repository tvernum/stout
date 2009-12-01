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

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public enum OperandType
{
    VOID(0, 0, 'V'), INT(1, 1, 'I'), LONG(2, 2, 'J'), FLOAT(3, 1, 'F'), DOUBLE(4, 2, 'D'), OBJECT(5, 1, 'L'), BYTE(6, 1, 'B'), CHAR(7, 1, 'C'),
    SHORT(8, 1, 'S'), BOOLEAN(1, 1, 'Z'), ARRAY(5, 1, '[');

    private final int _opCodeOffset;
    private final int _size;
    private final char _descriptorChar;

    private OperandType(int code, int size, char descriptorChar)
    {
        _opCodeOffset = code;
        _size = size;
        _descriptorChar = descriptorChar;
    }

    public int getOpCodeOffset()
    {
        return _opCodeOffset;
    }

    public int getSize()
    {
        return _size;
    }

    public char getDescriptorChar()
    {
        return _descriptorChar;
    }

    public static OperandType forDescriptor(char ch)
    {
        for (OperandType type : values())
        {
            if (type.getDescriptorChar() == ch)
            {
                return type;
            }
        }
        return null;
    }
}

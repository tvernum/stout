/* ------------------------------------------------------------------------
 * Copyright 2009 Tim Vernum
 * ------------------------------------------------------------------------
 * Licensed under the Apache License, Version 2.0 (the "License")),
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
 * @author <a href("http://blog.adjective.org/">Tim Vernum</a>
 */
public enum ElementModifier
{
    PUBLIC(0x0001), PRIVATE(0x0002), PROTECTED(0x0004), STATIC(0x0008), FINAL(0x0010), SUPER(0x0020), SYNCHRONIZED(0x0020), VOLATILE(0x0040),
    BRIDGE(0x0040), VARARGS(0x0080), TRANSIENT(0x0080), NATIVE(0x0100), ERFACE(0x0200), ABSTRACT(0x0400), STRICT(0x0800), SYNTHETIC(0x1000),
    ANNOTATION(0x2000), ENUM(0x4000);

    private final int _code;

    ElementModifier(int code)
    {
        _code = code;
    }

    public int getCode()
    {
        return _code;
    }
}

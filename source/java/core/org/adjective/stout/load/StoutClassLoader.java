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

package org.adjective.stout.load;

import org.adjective.stout.core.ClassDescriptor;
import org.adjective.stout.writer.ByteCodeWriter;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class StoutClassLoader extends ClassLoader
{
    public StoutClassLoader()
    {
        super();
    }

    public StoutClassLoader(ClassLoader parent)
    {
        super(parent);
    }

    public Class< ? > defineClass(ClassDescriptor cls)
    {
        byte[] bytes = new ByteCodeWriter().write(cls);
        return defineClass(null, bytes, 0, bytes.length);
    }

}

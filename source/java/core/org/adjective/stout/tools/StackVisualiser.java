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

package org.adjective.stout.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.objectweb.asm.ClassReader;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class StackVisualiser 
{
    public static void main(String[] args) throws IOException
    {
        if (args.length != 2)
        {
            throw new RuntimeException("Usage: <java> " + StackVisualiser.class.getName() + " <class-file> <method>");
        }

        File classFile = new File(args[0]);
        String method = args[1];

        InputStream input = new FileInputStream(classFile);
        ClassReader reader = new ClassReader(input);
        reader.accept(new StackVisualiserClassVisitor(method, System.out), ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
    }
}

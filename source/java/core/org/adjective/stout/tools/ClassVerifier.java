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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.SimpleVerifier;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ClassVerifier
{
    public static void main(String[] args) throws IOException
    {
        if (args.length != 2)
        {
            throw new RuntimeException("Usage: <java> " + ClassVerifier.class.getName() + " <class-file> <method>");
        }

        File classFile = new File(args[0]);
        String methodName = args[1];

        if (classFile.isDirectory())
        {
            for (File file : classFile.listFiles())
            {
                if (file.getName().endsWith(".class"))
                {
                    System.err.println("-=[ " + file + "]=-");
                    execute(file, methodName);
                }
            }
        }
        else
        {
            execute(classFile, methodName);
        }
    }

    @SuppressWarnings("unchecked")
    private static void execute(File classFile, String methodName) throws FileNotFoundException, IOException
    {
        InputStream input = new FileInputStream(classFile);
        ClassReader reader = new ClassReader(input);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        List<MethodNode> methods = node.methods;
        for (MethodNode method : methods)
        {
            if (method.name.equals(methodName) || "*".equals(methodName))
            {
                verify(node.name, method);
            }
        }
    }

    public static void verify(String owner, MethodNode mn)
    {
        Analyzer a = new Analyzer(new SimpleVerifier());
        try
        {
            System.err.print("Method " + mn.name + (mn.signature == null ? "" : " " + mn.signature) );
            a.analyze(owner, mn);
            System.err.println(" : OK");
        }
        catch (AnalyzerException e)
        {
            System.err.println(" : ERROR");
            e.printStackTrace();
        }
        catch (VerifyError e)
        {
            System.err.println(" : ERROR");
            e.printStackTrace();
        }
    }

}

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

import java.io.PrintStream;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
class StackVisualiserClassVisitor implements ClassVisitor
{
    private final String _method;
    private final PrintStream _output;
    private String _name;
    private String _superName;

    public StackVisualiserClassVisitor(String method, PrintStream output)
    {
        _method = method;
        _output = output;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
    {
        _name = name;
        _superName = superName;
        _output.print("Class: " + name + " ; Extends: " + superName);
        if (interfaces.length > 0)
        {
            _output.print(" ; Implements ");
            for (String ifc : interfaces)
            {
                _output.print(ifc);
                _output.print(" ");
            }
        }
        _output.println();
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible)
    {
        return null;
    }

    public void visitAttribute(Attribute attr)
    {
        // No-op
    }

    public void visitEnd()
    {
        _output.println("---------------");
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
    {
        return null;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access)
    {
        // no-op
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    {
        if (_method.equals(name))
        {
            _output.print("Method: " + Type.getReturnType(desc) + " " + name + "( ");
            Type[] arguments = Type.getArgumentTypes(desc);
            for (Type type : arguments)
            {
                _output.print(type);
                _output.print(" ");
            }
            _output.println(")");
            Type thisType = Type.getType("L" + _name + ";");
            Type superType = Type.getType("L" + _superName + ";");
            return new StackVisualiserMethodVisitor(_output, thisType, superType, (access & Opcodes.ACC_STATIC) > 0, arguments);
        }
        return null;
    }

    public void visitOuterClass(String owner, String name, String desc)
    {
        // No-op
    }

    public void visitSource(String source, String debug)
    {
        // No-op
    }
}

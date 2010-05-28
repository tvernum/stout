/* ------------------------------------------------------------------------
 * Copyright 2010 Tim Vernum
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

package org.adjective.stout.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public abstract class AbstractClassVisitor implements ClassVisitor
{
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
    {
        // Stub
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible)
    {
        // Stub
        return null;
    }

    public void visitAttribute(Attribute attr)
    {
        // Stub
    }

    public void visitEnd()
    {
        // Stub
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
    {
        // Stub
        return null;
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access)
    {
        // Stub
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    {
        // Stub
        return null;
    }

    public void visitOuterClass(String owner, String name, String desc)
    {
        // Stub
    }

    public void visitSource(String source, String debug)
    {
        // Stub
    }

}

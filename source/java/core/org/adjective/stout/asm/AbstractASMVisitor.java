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
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public abstract class AbstractASMVisitor extends AbstractClassVisitor implements MethodVisitor, FieldVisitor, AnnotationVisitor
{
    public AnnotationVisitor visitAnnotationDefault()
    {
        return this;
    }

    public void visitCode()
    {
        // Stub
    }

    public void visitFieldInsn(int opcode, String owner, String name, String desc)
    {
        // Stub
    }

    public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack)
    {
        // Stub
    }

    public void visitIincInsn(int var, int increment)
    {
        // Stub
    }

    public void visitInsn(int opcode)
    {
        // Stub
    }

    public void visitIntInsn(int opcode, int operand)
    {
        // Stub
    }

    public void visitJumpInsn(int opcode, Label label)
    {
        // Stub
    }

    public void visitLabel(Label label)
    {
        // Stub
    }

    public void visitLdcInsn(Object cst)
    {
        // Stub
    }

    public void visitLineNumber(int line, Label start)
    {
        // Stub
    }

    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index)
    {
        // Stub
    }

    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels)
    {
        // Stub
    }

    public void visitMaxs(int maxStack, int maxLocals)
    {
        // Stub
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc)
    {
        // Stub
    }

    public void visitMultiANewArrayInsn(String desc, int dims)
    {
        // Stub
    }

    public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible)
    {
        return this;
    }

    public void visitTableSwitchInsn(int min, int max, Label dflt, Label[] labels)
    {
        // Stub
    }

    public void visitTryCatchBlock(Label start, Label end, Label handler, String type)
    {
        // Stub
    }

    public void visitTypeInsn(int opcode, String type)
    {
        // Stub
    }

    public void visitVarInsn(int opcode, int var)
    {
        // Stub
    }

    public void visit(String name, Object value)
    {
        // Stub
    }

    public AnnotationVisitor visitAnnotation(String name, String desc)
    {
        return this;
    }

    public AnnotationVisitor visitArray(String name)
    {
        return this;
    }

    public void visitEnum(String name, String desc, String value)
    {
        // Stub
    }
    
    public AnnotationVisitor visitAnnotation(String desc, boolean visible)
    {
        return this;
    }
    
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value)
    {
        return this;
    }
    
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
    {
        return this;
    }
    
}

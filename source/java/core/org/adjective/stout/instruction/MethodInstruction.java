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

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class MethodInstruction extends AbstractInstruction
{
    private final String _declaringType;
    private final String _name;
    private final String _descriptor;

    public MethodInstruction(int opCode, String declaringType, MethodSignature method)
    {
        super(opCode);
        _declaringType = declaringType;
        _name = method.getName();
        _descriptor = getMethodDescriptor(method.getReturnType().getRawClass(), method.getParameterTypes());
    }

    public static String getMethodDescriptor(Class< ? > returnClass, UnresolvedType[] parameterTypes)
    {
        Type returnType = Type.getType(returnClass);
        Type[] argumentTypes = getArgumentTypes(parameterTypes);
        return Type.getMethodDescriptor(returnType, argumentTypes);
    }

    public MethodInstruction(int opCode, String declaringType, String name, String descriptor)
    {
        super(opCode);
        _declaringType = declaringType;
        _name = name;
        _descriptor = descriptor;
    }

    public void accept(MethodVisitor visitor)
    {
        visitor.visitMethodInsn(getOpCode(), _declaringType, _name, _descriptor);
    }

    private static Type[] getArgumentTypes(UnresolvedType[] parameterTypes)
    {
        Type[] argumentTypes = new Type[parameterTypes.length];
        for (int i = 0; i < argumentTypes.length; i++)
        {
            argumentTypes[i] = Type.getType(parameterTypes[i].getDescriptor());
        }
        return argumentTypes;
    }
}

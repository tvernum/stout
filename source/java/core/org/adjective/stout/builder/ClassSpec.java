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

package org.adjective.stout.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Opcodes;

import org.adjective.stout.core.ClassDescriptor;
import org.adjective.stout.core.Code;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.FieldDescriptor;
import org.adjective.stout.core.MethodDescriptor;
import org.adjective.stout.core.Operation;
import org.adjective.stout.core.ParameterisedClass;
import org.adjective.stout.core.TypeParameter;
import org.adjective.stout.impl.ClassImpl;
import org.adjective.stout.impl.CodeImpl;
import org.adjective.stout.impl.ParameterisedClassImpl;
import org.adjective.stout.instruction.MethodInstruction;
import org.adjective.stout.instruction.VarInstruction;
import org.adjective.stout.operation.ReturnVoidOperation;

import static org.adjective.stout.util.CollectionUtils.toArray;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ClassSpec implements ElementBuilder<ClassDescriptor>
{
    private static final String CONSTRUCTOR_NAME = "<init>";
    private final String _package;
    private final String _name;
    private final Set<ElementModifier> _modifiers;
    private final List<TypeParameter> _parameters;
    private ParameterisedClass _superClass;
    private final List<ParameterisedClass> _interfaces;
    private final List<FieldDescriptor> _fields;
    private final List<MethodDescriptor> _methods;
    private ElementModifier _defaultConstructorAccess;

    public ClassSpec(String pkg, String name)
    {
        _package = pkg;
        _name = name;
        _modifiers = new HashSet<ElementModifier>();
        _parameters = new ArrayList<TypeParameter>();
        _interfaces = new ArrayList<ParameterisedClass>();
        _fields = new ArrayList<FieldDescriptor>();
        _methods = new ArrayList<MethodDescriptor>();
        _defaultConstructorAccess = null;
    }

    public ClassDescriptor create()
    {
        if (_defaultConstructorAccess != null)
        {
            addDefaultConstructor();
        }
        return new ClassImpl(_modifiers, _package, _name, toArray(_parameters, TypeParameter.class), // 
                _superClass, toArray(_interfaces, ParameterisedClass.class), //
                toArray(_fields, FieldDescriptor.class), toArray(_methods, MethodDescriptor.class));
    }

    private void addDefaultConstructor()
    {
        String superName = _superClass.getInternalName();

        Operation loadThis = new VarInstruction(Opcodes.ALOAD, 0);
        Operation invokeSuper = new MethodInstruction(Opcodes.INVOKESPECIAL, superName, CONSTRUCTOR_NAME, "()V");
        Operation returnNothing = new ReturnVoidOperation();
        Code body = new CodeImpl(loadThis, invokeSuper, returnNothing);

        MethodSpec method = new MethodSpec(CONSTRUCTOR_NAME).withModifiers(_defaultConstructorAccess).withBody(body);
        _methods.add(0, method.create());
    }

    public ClassSpec withModifiers(ElementModifier... modifiers)
    {
        _modifiers.addAll(Arrays.asList(modifiers));
        return this;
    }

    public ClassSpec withTypeParameter(TypeParameter parameter)
    {
        _parameters.add(parameter);
        return this;
    }

    public ClassSpec withTypeParameter(ElementBuilder< ? extends TypeParameter> parameter)
    {
        return withTypeParameter(parameter.create());
    }

    public ClassSpec withTypeParameters(TypeParameter... parameters)
    {
        _parameters.addAll(Arrays.asList(parameters));
        return this;
    }

    public ClassSpec withTypeParameters(ElementBuilder< ? extends TypeParameter>... parameters)
    {
        for (ElementBuilder< ? extends TypeParameter> builder : parameters)
        {
            withTypeParameter(builder);
        }
        return this;
    }

    public ClassSpec withSuperClass(ParameterisedClass superClass)
    {
        _superClass = superClass;
        return this;
    }

    public ClassSpec withSuperClass(Class< ? > superClass)
    {
        return withSuperClass(new ParameterisedClassImpl(superClass));
    }

    public ClassSpec withSuperClass(ElementBuilder< ? extends ParameterisedClass> superClass)
    {
        return withSuperClass(superClass.create());
    }

    public ClassSpec withInterface(ParameterisedClass iface)
    {
        _interfaces.add(iface);
        return this;
    }

    public ClassSpec withInterface(Class< ? > iface)
    {
        return withInterface(new ParameterisedClassImpl(iface));
    }

    public ClassSpec withInterface(ElementBuilder< ? extends ParameterisedClass> iface)
    {
        return withInterface(iface.create());
    }

    public ClassSpec withInterfaces(ParameterisedClass... interfaces)
    {
        _interfaces.addAll(Arrays.asList(interfaces));
        return this;
    }

    public ClassSpec withInterfaces(Class< ? >... interfaces)
    {
        for (Class< ? > cls : interfaces)
        {
            this.withInterface(cls);
        }
        return this;
    }

    public ClassSpec withInterfaces(ElementBuilder< ? extends ParameterisedClass>... interfaces)
    {
        for (ElementBuilder< ? extends ParameterisedClass> builder : interfaces)
        {
            this.withInterface(builder);
        }
        return this;
    }

    public ClassSpec withField(FieldDescriptor field)
    {
        _fields.add(field);
        return this;
    }

    public ClassSpec withField(ElementBuilder< ? extends FieldDescriptor> field)
    {
        return withField(field.create());
    }

    public ClassSpec withMethod(MethodDescriptor method)
    {
        _methods.add(method);
        return this;
    }

    public ClassSpec withMethod(ElementBuilder< ? extends MethodDescriptor> method)
    {
        return withMethod(method.create());
    }

    public ClassSpec withMethods(MethodDescriptor... method)
    {
        _methods.addAll(Arrays.asList(method));
        return this;
    }

    public ClassSpec withMethods(ElementBuilder< ? extends MethodDescriptor>... methods)
    {
        for (ElementBuilder< ? extends MethodDescriptor> builder : methods)
        {
            withMethod(builder);
        }
        return this;

    }

    public ClassSpec withDefaultConstructor(ElementModifier access)
    {
        _defaultConstructorAccess = access;
        return this;
    }
}
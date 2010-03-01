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

package org.adjective.stout.impl;

import java.util.Set;

import org.adjective.stout.core.AnnotationDescriptor;
import org.adjective.stout.core.ClassDescriptor;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.FieldDescriptor;
import org.adjective.stout.core.MethodDescriptor;
import org.adjective.stout.core.ParameterisedClass;
import org.adjective.stout.core.TypeParameter;
import org.adjective.stout.core.UnresolvedType;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ClassImpl implements ClassDescriptor
{
    private final Sort _sort;
    private final Set<ElementModifier> _modifiers;
    private final String _package;
    private final String _name;
    private final UnresolvedType _outerClass;
    private final TypeParameter[] _parameters;
    private final ParameterisedClass _superClass;
    private final UnresolvedType[] _interfaces;
    private final AnnotationDescriptor[] _annotations;
    private final ClassDescriptor[] _innerClasses;
    private final FieldDescriptor[] _fields;
    private final MethodDescriptor[] _methods;
    private final String _source;

    private ClassImpl(Sort sort, Set<ElementModifier> modifiers, String pkg, String name, String source, UnresolvedType outerClass,
            TypeParameter[] parameters, ParameterisedClass superClass, UnresolvedType[] interfaces, AnnotationDescriptor[] annotations,
            ClassDescriptor[] innerClasses, FieldDescriptor[] fields, MethodDescriptor[] methods)
    {
        _sort = sort;
        _modifiers = modifiers;
        _package = pkg;
        _name = name;
        _source = source;
        _outerClass = outerClass;
        _parameters = parameters;
        _superClass = superClass;
        _interfaces = interfaces;
        _annotations = annotations;
        _innerClasses = innerClasses;
        _fields = fields;
        _methods = methods;
    }

    public static ClassImpl newClass(Set<ElementModifier> modifiers, String pkg, String name, String source, UnresolvedType outerClass,
            TypeParameter[] parameters, ParameterisedClass superClass, UnresolvedType[] interfaces, AnnotationDescriptor[] annotations,
            ClassDescriptor[] innerClasses, FieldDescriptor[] fields, MethodDescriptor[] methods)
    {
        return new ClassImpl(Sort.CLASS, modifiers, pkg, name, source, outerClass, parameters, superClass, interfaces, annotations, innerClasses,
                fields, methods);
    }

    public static ClassImpl newInterface(Set<ElementModifier> modifiers, String pkg, String name, String source, UnresolvedType outerClass,
            TypeParameter[] parameters, UnresolvedType[] interfaces, AnnotationDescriptor[] annotations, ClassDescriptor[] innerClasses,
            FieldDescriptor[] fields, MethodDescriptor[] methods)
    {
        return new ClassImpl(Sort.INTERFACE, modifiers, pkg, name, source, outerClass, parameters, null, interfaces, annotations, innerClasses,
                fields, methods);
    }

    public Set<ElementModifier> getModifiers()
    {
        return _modifiers;
    }

    public String getPackage()
    {
        return _package;
    }

    public String getName()
    {
        return _name;
    }

    public TypeParameter[] getParameters()
    {
        return _parameters;
    }

    public ParameterisedClass getSuperClass()
    {
        return _superClass;
    }

    public UnresolvedType[] getInterfaces()
    {
        return _interfaces;
    }

    public FieldDescriptor[] getFields()
    {
        return _fields;
    }

    public MethodDescriptor[] getMethods()
    {
        return _methods;
    }

    public String getInternalName()
    {
        return _package.replace('.', '/') + "/" + _name;
    }

    public String getDescriptor()
    {
        return "L" + getInternalName() + ";";
    }

    public Sort getSort()
    {
        return _sort;
    }

    public boolean canAssignTo(UnresolvedType type)
    {
        if (getDescriptor().equals(type.getDescriptor()))
        {
            return true;
        }
        if (_superClass.canAssignTo(type))
        {
            return true;
        }
        for (UnresolvedType ifc : _interfaces)
        {
            if (ifc.canAssignTo(type))
            {
                return true;
            }
        }
        return false;
    }

    public UnresolvedType getFieldType(String fieldName)
    {
        for (FieldDescriptor field : _fields)
        {
            if (fieldName.equals(field.getName()))
            {
                return field.getType();
            }
        }
        return null;
    }

    public AnnotationDescriptor[] getAnnotations()
    {
        return _annotations;
    }

    public ClassDescriptor[] getInnerClasses()
    {
        return _innerClasses;
    }

    public UnresolvedType getOuterClass()
    {
        return _outerClass;
    }

    public String toString()
    {
        return _name;
    }

    public String getSourceFile()
    {
        return _source;
    }
}

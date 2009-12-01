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

package org.adjective.stout.test;

import java.lang.reflect.Method;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

import org.adjective.stout.builder.ClassSpec;
import org.adjective.stout.builder.MethodSpec;
import org.adjective.stout.core.ClassDescriptor;
import org.adjective.stout.core.ConstructorSignature;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.impl.ConstructorSignatureImpl;
import org.adjective.stout.impl.MethodSignatureImpl;
import org.adjective.stout.impl.ParameterisedClassImpl;
import org.adjective.stout.load.StoutClassLoader;
import org.adjective.stout.loop.ConditionalOperations;
import org.adjective.stout.operation.ExpressionOperations;
import org.adjective.stout.operation.StatementOperations;
import org.adjective.stout.operation.VM;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class IntegrationTest1
{
    @SuppressWarnings("unchecked")
    @Test
    public void simpleClass1() throws Exception
    {
        StatementOperations stmt = new StatementOperations();
        ExpressionOperations expr = new ExpressionOperations();
        ConditionalOperations loop = new ConditionalOperations();

        // @TODO utility methods to get method signatures
        //        MethodSignature m_i = new MethodSignatureImpl(Collections.singleton(ElementModifier.PUBLIC), new ParameterisedClassImpl(Integer.TYPE), "m_i",
        //                new ExtendedType[0]);
        MethodSignature m_ii = new MethodSignatureImpl(Collections.singleton(ElementModifier.PUBLIC), new ParameterisedClassImpl(Integer.TYPE),
                "m_ii", new ExtendedType[] { new ParameterisedClassImpl(Integer.TYPE) });
        MethodSignature m_print = new MethodSignatureImpl(Collections.singleton(ElementModifier.PROTECTED), new ParameterisedClassImpl(Void.TYPE),
                "m_print", new ExtendedType[] { new ParameterisedClassImpl(String.class) });
        ConstructorSignature newStringBuilder = new ConstructorSignatureImpl(StringBuilder.class, String.class);

        MethodSignature stringBuilderAppendInt = new MethodSignatureImpl(StringBuilder.class.getMethod("append", Integer.TYPE));

        String packageName = "org.adjective.stout.test";
        String className = "SimpleClass1";

        String methodName = "foo";
        ClassDescriptor cls =
        /*    */new ClassSpec(packageName, className)
        /*      */.withModifiers(ElementModifier.PUBLIC)
        /*      */.withSuperClass(BaseClass.class)
        /*      */.withDefaultConstructor(ElementModifier.PUBLIC)
        /*      */.withMethod(
        /*         */new MethodSpec(methodName)
        /*           */.withModifiers(ElementModifier.PUBLIC)
        /*           */.withReturnType(Void.TYPE)
        /*           */.withBody(
        /*              */stmt.declareVariable(Integer.TYPE, "i"),
        /*              */stmt.assignVariable("i",
        /*                */expr.callInherited(m_ii, expr.constant(7))
        /*              */),
        /*              */stmt.declareVariable(StringBuilder.class, "sb"),
        /*              */stmt.assignVariable("sb",
        /*                */expr.construct(
        /*                  */newStringBuilder,
        /*                  */expr.constant("a")
        /*                */)
        /*              */),
        /*              */stmt.callMethod(
        /*                */expr.variable("sb"),
        /*                */StringBuilder.class,
        /*                */stringBuilderAppendInt,
        /*                */expr.variable("i")
        /*              */),
        /*              */stmt.declareVariable(Boolean.TYPE, "first"),
        /*              */stmt.assignVariable("first",
        /*                */expr.constant(true)
        /*              */),
        /*              */loop.forLoop()
        /*                */.withInitialiser(stmt.empty())
        /*                */.withCondition(loop.expression(expr.variable("first")))
        /*                */.withIncrement(stmt.assignVariable("first", expr.constant(false)))
        /*                */.withBody(
        /*                   */stmt.callInherited(m_print, expr.constant("for loop"))
        /*                */),
        /*              */stmt.returnVoid()
        /*           */)
        /*      */).create();
        StoutClassLoader loader = new StoutClassLoader();
        Class< ? > loaded = loader.defineClass(cls);

        // Check loaded correctly...
        Assert.assertNotNull(loaded);
        Assert.assertEquals(packageName + "." + className, loaded.getName());

        // Check method exists
        Method[] loadedMethods = loaded.getDeclaredMethods();
        Assert.assertEquals(1, loadedMethods.length);
        Assert.assertEquals(methodName, loadedMethods[0].getName());

        Object instance = loaded.newInstance();
        Assert.assertNotNull(instance);
        loadedMethods[0].invoke(instance);

        Assert.assertTrue(instance instanceof BaseClass);
        Assert.assertEquals(5, ((BaseClass) instance).m_ii(5));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void simpleTryCatch() throws Exception
    {
        String packageName = "org.adjective.stout.test";
        String className = "SimpleClass2";

        String methodName = "foo";
        ClassDescriptor cls =
        /*    */new ClassSpec(packageName, className)
        /*      */.withModifiers(ElementModifier.PUBLIC)
        /*      */.withSuperClass(BaseClass.class)
        /*      */.withDefaultConstructor(ElementModifier.PUBLIC)
        /*      */.withMethod(
        /*         */new MethodSpec(methodName)
        /*           */.withModifiers(ElementModifier.PUBLIC)
        /*           */.withReturnType(Void.TYPE)
        /*           */.withBody(
        /*              */VM.Statement.attempt(
        /*                */VM.Statement.ignore(VM.Expression.thisObject())
        /*              */).on(new ParameterisedClassImpl(NullPointerException.class), VM.Statement.returnVoid()),
        /*              */VM.Statement.returnVoid()
        /*           */)
        /*       */).create();

        StoutClassLoader loader = new StoutClassLoader();
        Class< ? > loaded = loader.defineClass(cls);

        // Check loaded correctly...
        Assert.assertNotNull(loaded);
        Assert.assertEquals(packageName + "." + className, loaded.getName());

        // Check method exists
        Method[] loadedMethods = loaded.getDeclaredMethods();
        Assert.assertEquals(1, loadedMethods.length);
        Assert.assertEquals(methodName, loadedMethods[0].getName());

        Object instance = loaded.newInstance();
        Assert.assertNotNull(instance);
        loadedMethods[0].invoke(instance);
    }
}

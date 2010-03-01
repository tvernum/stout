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

package org.adjective.stout.operation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.Label;

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.core.ConstructorSignature;
import org.adjective.stout.core.ElementModifier;
import org.adjective.stout.core.ExtendedType;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.MethodSignatureImpl;
import org.adjective.stout.impl.ParameterisedClassImpl;

import static org.adjective.stout.operation.ExpressionOperations.toExpressionArray;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class StatementOperations
{
    public static Statement[] toStatementArray(ElementBuilder<Statement>[] builders)
    {
        Statement[] statements = new Statement[builders.length];
        for (int i = 0; i < builders.length; i++)
        {
            statements[i] = builders[i].create();
        }
        return statements;
    }

    public static Statement[] toStatementArray(Collection< ? extends ElementBuilder< ? extends Statement>> builders)
    {
        int i = 0;
        Statement[] statements = new Statement[builders.size()];
        for (ElementBuilder< ? extends Statement> builder : builders)
        {
            statements[i] = builder.create();
            i++;
        }
        return statements;
    }

    public ElementBuilder<Statement> declareVariable(ExtendedType type, CharSequence name)
    {
        return new DeclareVariableStatement(type, name.toString());
    }

    public ElementBuilder<Statement> declareVariable(ElementBuilder< ? extends ExtendedType> type, CharSequence name)
    {
        return declareVariable(type.create(), name);
    }

    public ElementBuilder<Statement> declareVariable(Class< ? > type, CharSequence name)
    {
        return new DeclareVariableStatement(new ParameterisedClassImpl(type), name.toString());
    }

    public ElementBuilder<Statement> assignVariable(CharSequence name, Expression expression)
    {
        return new AssignVariableStatement(name.toString(), expression);
    }

    public ElementBuilder<Statement> assignField(CharSequence name, Expression expression)
    {
        return new AssignFieldStatement(null, null, name.toString(), null, expression);
    }

    public ElementBuilder<Statement> assignField(CharSequence name, ElementBuilder< ? extends Expression> expression)
    {
        return assignField(name, expression.create());
    }

    public ElementBuilder<Statement> assignVariable(CharSequence name, ElementBuilder< ? extends Expression> expression)
    {
        return assignVariable(name, expression.create());
    }

    public ElementBuilder<Statement> returnVoid()
    {
        return new ReturnVoidOperation();
    }

    public ElementBuilder<Statement> ignore(ElementBuilder< ? extends Expression> expression)
    {
        return ignore(expression.create());
    }

    public ElementBuilder<Statement> ignore(Expression expression)
    {
        return new IgnoreValueStatement(expression);
    }

    public ElementBuilder<Statement> empty()
    {
        return new EmptyStatement();
    }

    public ElementBuilder<Statement> callMethod(ElementBuilder< ? extends Expression> target, Class< ? > targetClass, MethodSignature method,
            ElementBuilder< ? extends Expression>... arguments)
    {
        return callMethod(target.create(), targetClass, method, toExpressionArray(arguments));
    }

    public ElementBuilder<Statement> callMethod(Expression target, Class< ? > targetClass, MethodSignature method, Expression... arguments)
    {
        return callMethod(target, new ParameterisedClassImpl(targetClass), method, arguments);
    }

    public ElementBuilder<Statement> callMethod(Expression target, UnresolvedType targetClass, MethodSignature method, Expression... arguments)
    {
        return new InvokeVirtualStatement(target, targetClass, method, arguments);
    }

    public ElementBuilder<Statement> callInherited(MethodSignature method, ElementBuilder< ? extends Expression>... arguments)
    {
        return callInherited(method, toExpressionArray(arguments));
    }

    public ElementBuilder<Statement> callInherited(MethodSignature method, Expression... arguments)
    {
        return new InvokeVirtualStatement(method, arguments);
    }

    public ElementBuilder<Statement> returnObject(ElementBuilder< ? extends Expression> object)
    {
        return returnObject(object.create());
    }

    public ElementBuilder<Statement> returnObject(Expression object)
    {
        return new ReturnObjectStatement(object);
    }

    public ElementBuilder<Statement> superConstructor()
    {
        return superConstructor(new Class[0]);
    }

    public ElementBuilder<Statement> superConstructor(Class< ? >[] parameterTypes, Expression... arguments)
    {
        ExtendedType[] parameters = ParameterisedClassImpl.getArray(parameterTypes);
        return superConstructor(parameters, arguments);
    }

    public ElementBuilder<Statement> superConstructor(UnresolvedType[] parameters, Expression... arguments)
    {
        Set<ElementModifier> access = Collections.singleton(ElementModifier.PROTECTED);
        ParameterisedClassImpl returnType = new ParameterisedClassImpl(Void.TYPE);
        String name = InvokeSuperConstructorStatement.CONSTRUCTOR_NAME;
        MethodSignatureImpl signature = new MethodSignatureImpl(access, returnType, name, parameters);
        return superConstructor(signature, arguments);
    }

    public ElementBuilder<Statement> superConstructor(MethodSignature method, Expression... arguments)
    {
        return new InvokeSuperConstructorStatement(method, arguments);
    }

    public ElementBuilder<Statement> superConstructor(ConstructorSignature signature, Expression... arguments)
    {
        return superConstructor(signature.getParameterTypes(), arguments);
    }

    public ElementBuilder<Statement> block(List<ElementBuilder< ? extends Statement>> statements)
    {
        return new BlockStatement(toStatementArray(statements));
    }

    public ElementBuilder<Statement> block(ElementBuilder<Statement>... statements)
    {
        return new BlockStatement(toStatementArray(statements));
    }

    public TryCatchSpec attempt(ElementBuilder<Statement>... body)
    {
        return attempt(block(body).create());
    }

    public TryCatchSpec attempt(Statement body)
    {
        return new TryCatchSpec(body);
    }

    public ElementBuilder<Statement> callStatic(UnresolvedType targetClass, MethodSignature method, Expression... arguments)
    {
        return new InvokeStaticStatement(targetClass, method, arguments);
    }

    public ElementBuilder<Statement> Goto(Label label)
    {
        return new GotoStatement(label);
    }

}

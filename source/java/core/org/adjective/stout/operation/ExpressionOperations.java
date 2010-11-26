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

import java.util.List;

import org.adjective.stout.builder.ElementBuilder;
import org.adjective.stout.builder.MethodSpec;
import org.adjective.stout.core.ConstructorSignature;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.UnresolvedType;
import org.adjective.stout.impl.ParameterisedClassImpl;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class ExpressionOperations
{
    public Expression callInherited(MethodSignature method, Expression... expressions)
    {
        return new InvokeVirtualExpression(method, expressions);
    }

    public Expression callInherited(MethodSignature method, ElementBuilder< ? extends Expression>... builders)
    {
        return callInherited(method, toExpressionArray(builders));
    }

    public Expression callStatic(UnresolvedType type, MethodSignature method, Expression... expressions)
    {
        return new InvokeStaticExpression(type, method, expressions);
    }

    public Expression callStatic(UnresolvedType type, MethodSignature method, ElementBuilder< ? extends Expression>... builders)
    {
        return callStatic(type, method, toExpressionArray(builders));
    }

    public Expression callStatic(Class< ? > type, MethodSignature method, Expression... expressions)
    {
        return callStatic(new ParameterisedClassImpl(type), method, expressions);
    }

    public static Expression[] toExpressionArray(ElementBuilder< ? extends Expression>... builders)
    {
        Expression[] expressions = new Expression[builders.length];
        for (int i = 0; i < expressions.length; i++)
        {
            expressions[i] = builders[i].create();
        }
        return expressions;
    }

    public Expression constant(int value)
    {
        return new ConstantIntegerExpression(value);
    }

    public Expression constant(String string)
    {
        return new ConstantValueExpression<String>(string);
    }

    public Expression constant(boolean value)
    {
        return value ? ConstantIntegerExpression.ONE : ConstantIntegerExpression.ZERO;
    }

    public Expression constant(char value)
    {
        return new ConstantIntegerExpression(value);
    }

    public Expression constant(long l)
    {
        // @TODO - use lconst opcode
        return new ConstantValueExpression<Long>(l);
    }

    public Expression constant(double d)
    {
        return new ConstantValueExpression<Double>(d);
    }

    public Expression nullObject()
    {
        return new NullExpression();
    }

    public Expression construct(ConstructorSignature constructor, Expression... expressions)
    {
        return new ConstructorExpression(constructor, expressions);
    }

    public Expression construct(UnresolvedType type, MethodSpec constructor, Expression... expressions)
    {
        return new ConstructorExpression(type, constructor, expressions);
    }

    public Expression variable(String name)
    {
        return new LoadVariableExpression(name);
    }

    public Expression callMethod(ElementBuilder< ? extends Expression> target, Class< ? > targetClass, MethodSignature method,
            ElementBuilder< ? extends Expression>... arguments)
    {
        return callMethod(target.create(), new ParameterisedClassImpl(targetClass), method, toExpressionArray(arguments));
    }

    public Expression callMethod(Expression target, Class< ? > targetClass, MethodSignature method, Expression... arguments)
    {
        return callMethod(target, new ParameterisedClassImpl(targetClass), method, arguments);
    }

    public Expression callMethod(Expression target, UnresolvedType targetClass, MethodSignature method, Expression... arguments)
    {
        return new InvokeVirtualExpression(target, targetClass, method, arguments);
    }

    public Expression getEnum(Enum< ? > value)
    {
        return new GetEnumValueExpression(value);
    }

    public Expression getStaticField(Class< ? > from, String name, Class< ? > type)
    {
        return getStaticField(new ParameterisedClassImpl(from), name, new ParameterisedClassImpl(type));
    }

    public Expression getStaticField(UnresolvedType from, String name, UnresolvedType type)
    {
        return new GetStaticFieldExpression(from, name, type);
    }

    public Expression getField(Expression target, Class< ? > from, String name, Class< ? > type)
    {
        return getField(target, new ParameterisedClassImpl(from), name, new ParameterisedClassImpl(type));
    }

    public Expression getField(Expression target, UnresolvedType from, String name, UnresolvedType type)
    {
        return new GetFieldExpression(target, from, name, type);
    }

    public Expression getField(String name, UnresolvedType type)
    {
        return new GetFieldExpression(name, type);
    }

    public Expression array(Class< ? > componentType, Expression[] elements)
    {
        return new CreateArrayExpression(new ParameterisedClassImpl(componentType), elements);
    }

    public Expression chain(Expression expression, ElementBuilder<Statement>... statements)
    {
        return chain(expression, StatementOperations.toStatementArray(statements));
    }

    public Expression chain(Expression expression, Statement... statements)
    {
        return new ChainExpression(expression, statements);
    }

    public Expression chain(List<ElementBuilder< ? extends Statement>> statements, Expression expression)
    {
        return new ChainExpression(StatementOperations.toStatementArray(statements), expression);
    }

    public Expression thisObject()
    {
        return ThisExpression.INSTANCE;
    }

    public Expression classObject(UnresolvedType cls)
    {
        return new ConstantClassExpression(cls);
    }

    public Expression cast(UnresolvedType type, Expression expression)
    {
        return new CastExpression(type, expression);
    }

    public Expression cast(Class< ? > type, Expression expression)
    {
        return cast(new ParameterisedClassImpl(type), expression);
    }

    public Expression pop()
    {
        return new PopExpression();
    }

}

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

package org.adjective.stout.loop;

import java.util.Iterator;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

import org.adjective.stout.core.ExecutionStack;
import org.adjective.stout.core.InstructionCollector;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.core.ExecutionStack.Block;
import org.adjective.stout.impl.ParameterisedClassImpl;
import org.adjective.stout.instruction.GenericInstruction;
import org.adjective.stout.instruction.JumpInstruction;
import org.adjective.stout.instruction.LabelInstruction;
import org.adjective.stout.operation.AssignVariableStatement;
import org.adjective.stout.operation.DeclareVariableStatement;
import org.adjective.stout.operation.DuplicateStackExpression;
import org.adjective.stout.operation.EmptyExpression;
import org.adjective.stout.operation.Expression;
import org.adjective.stout.operation.InvokeVirtualExpression;
import org.adjective.stout.operation.LoadVariableExpression;
import org.adjective.stout.operation.SmartStatement;
import org.adjective.stout.operation.Statement;
import org.adjective.stout.operation.VM;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class IterableLoop extends SmartStatement
{
    private static final MethodSignature ITERATOR_METHOD = VM.Method.find(Iterable.class, "iterator");
    private static final MethodSignature HAS_NEXT_METHOD = VM.Method.find(Iterator.class, "hasNext");
    private static final MethodSignature GET_NEXT_METHOD = VM.Method.find(Iterator.class, "next");

    private final Statement[] _body;
    private final Expression _iterable;
    private final String _iteratorName;
    private final String _valueName;

    public IterableLoop(Expression iterable, String iteratorName, String valueName, Statement[] body)
    {
        _iterable = iterable;
        _iteratorName = iteratorName == null ? "itr$" + System.identityHashCode(this) : iteratorName;
        _valueName = valueName;
        _body = body;
    }

    public void getInstructions(ExecutionStack stack, InstructionCollector collector)
    {
        Block block1 = stack.pushBlock();

        Expression iterator = new InvokeVirtualExpression(_iterable, new ParameterisedClassImpl(Iterable.class), ITERATOR_METHOD);

        DeclareVariableStatement declareIterator = new DeclareVariableStatement(new ParameterisedClassImpl(Iterator.class), _iteratorName);
        AssignVariableStatement assignIterator = new AssignVariableStatement(_iteratorName, iterator);
        declareIterator.getInstructions(stack, collector);
        assignIterator.getInstructions(stack, collector);

        Label nextLoop = new Label();
        addInstruction(collector,new LabelInstruction(nextLoop));

        Label popLoop = new Label();
        Label endLoop = new Label();

        new LoadVariableExpression(_iteratorName).getInstructions(stack, collector);
        Expression hasNext = new InvokeVirtualExpression(new DuplicateStackExpression(), new ParameterisedClassImpl(Iterator.class), HAS_NEXT_METHOD);
        new ExpressionCondition(hasNext).jumpWhenFalse(popLoop).getInstructions(stack, collector);

        Expression next = new InvokeVirtualExpression(EmptyExpression.INSTANCE, new ParameterisedClassImpl(Iterator.class), GET_NEXT_METHOD);

        new DeclareVariableStatement(new ParameterisedClassImpl(Object.class), _valueName).getInstructions(stack, collector);
        new AssignVariableStatement(_valueName, next).getInstructions(stack, collector);

        Block block2 = stack.pushBlock(nextLoop, endLoop);
        for (Statement stmt : _body)
        {
            stmt.getInstructions(stack, collector);
        }
        stack.popBlock(block2);

        addInstruction(collector,new JumpInstruction(Opcodes.GOTO, nextLoop));
        addInstruction(collector,new LabelInstruction(popLoop));
        addInstruction(collector,new GenericInstruction(Opcodes.POP));
        addInstruction(collector,new LabelInstruction(endLoop));
        stack.popBlock(block1);
    }

}

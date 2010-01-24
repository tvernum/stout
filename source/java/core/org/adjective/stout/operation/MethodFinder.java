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

import java.lang.reflect.Method;

import org.adjective.stout.core.ConstructorSignature;
import org.adjective.stout.core.MethodSignature;
import org.adjective.stout.exception.OperationException;
import org.adjective.stout.impl.ConstructorSignatureImpl;
import org.adjective.stout.impl.MethodSignatureImpl;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class MethodFinder
{
    public ConstructorSignature constructor(Class< ? > type, Class< ? >... parameters)
    {
        return new ConstructorSignatureImpl(type, parameters);
    }

    public MethodSignature find(Class< ? > declaring, String name, Class< ? >... parameterTypes)
    {
        return findMethod(declaring, declaring, name, parameterTypes);
    }

    private MethodSignature findMethod(Class< ? > declaring, Class< ? > search, String name, Class< ? >... parameterTypes)
    {
        try
        {
            Method method = search.getDeclaredMethod(name, parameterTypes);
            return new MethodSignatureImpl(method);
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        catch (NoSuchMethodException e)
        {
            if (search == Object.class)
            {
                throw new OperationException("Cannot find method " + name + " in " + declaring, e);
            }
            else
            {
                return findMethod(declaring, search.getSuperclass(), name, parameterTypes);
            }
        }
        catch (Exception e)
        {
            throw new OperationException("Cannot find method " + name + " in " + search, e);
        }
    }

}

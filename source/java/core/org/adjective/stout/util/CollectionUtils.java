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

package org.adjective.stout.util;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * @author <a href="http://blog.adjective.org/">Tim Vernum</a>
 */
public class CollectionUtils
{

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> collection, Class<T> type)
    {
        T[] array = (T[]) Array.newInstance(type, collection.size());
        return collection.toArray(array);
    }

}

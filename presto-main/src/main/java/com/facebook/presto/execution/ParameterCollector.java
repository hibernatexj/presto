/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.facebook.presto.execution;

import com.facebook.presto.sql.tree.DefaultTraversalVisitor;
import com.facebook.presto.sql.tree.Parameter;

import java.util.ArrayList;
import java.util.List;

public class ParameterCollector
        extends DefaultTraversalVisitor<Void, Void>
{
    private final List<Parameter> parameters = new ArrayList<>();

    public int getParameterCount()
    {
        return parameters.size();
    }

    @Override
    public Void visitParameter(Parameter node, Void context)
    {
        parameters.add(node);
        return null;
    }
}

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
package com.facebook.presto.sql.planner.assertions;

import com.facebook.presto.Session;
import com.facebook.presto.metadata.Metadata;
import com.facebook.presto.metadata.TableMetadata;
import com.facebook.presto.sql.planner.plan.PlanNode;
import com.facebook.presto.sql.planner.plan.TableScanNode;
import com.google.common.base.MoreObjects;

import static java.util.Objects.requireNonNull;

final class TableScanMatcher
        implements Matcher
{
    private final String expectedTableName;

    TableScanMatcher(String expectedTableName)
    {
        this.expectedTableName = requireNonNull(expectedTableName, "expectedTableName is null");
    }

    @Override
    public boolean matches(PlanNode node, Session session, Metadata metadata, ExpressionAliases expressionAliases)
    {
        if (node instanceof TableScanNode) {
            TableScanNode tableScanNode = (TableScanNode) node;
            TableMetadata tableMetadata = metadata.getTableMetadata(session, tableScanNode.getTable());
            String actualTableName = tableMetadata.getTable().getTableName();
            if (expectedTableName.equalsIgnoreCase(actualTableName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                .add("expectedTableName", expectedTableName)
                .toString();
    }
}

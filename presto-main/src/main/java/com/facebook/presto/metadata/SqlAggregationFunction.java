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
package com.facebook.presto.metadata;

import com.facebook.presto.operator.aggregation.AggregationCompiler;
import com.facebook.presto.operator.aggregation.AggregationCompiler.BindableAggregationFunction;
import com.facebook.presto.operator.aggregation.InternalAggregationFunction;
import com.facebook.presto.spi.type.TypeManager;
import com.facebook.presto.spi.type.TypeSignature;
import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.facebook.presto.metadata.FunctionKind.AGGREGATE;
import static com.facebook.presto.metadata.FunctionKind.APPROXIMATE_AGGREGATE;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public abstract class SqlAggregationFunction
        implements SqlFunction
{
    private final Signature signature;

    public static List<SqlAggregationFunction> createByAnnotations(Class<?> aggregationDefinition)
    {
        ImmutableList.Builder<SqlAggregationFunction> builder = ImmutableList.builder();
        for (BindableAggregationFunction bindableFunction : AggregationCompiler.generateBindableAggregationFunctions(aggregationDefinition)) {
            builder.add(new AnnotationBasedSqlAggregationFunction(bindableFunction));
        }

        return builder.build();
    }

    protected SqlAggregationFunction(BindableAggregationFunction bindableAggregationFunction)
    {
        requireNonNull(bindableAggregationFunction, "bindableAggregationFunction is null");
        this.signature = bindableAggregationFunction.getSignature();
    }

    protected SqlAggregationFunction(
            String name,
            List<TypeVariableConstraint> typeVariableConstraints,
            List<LongVariableConstraint> longVariableConstraints,
            TypeSignature returnType,
            List<TypeSignature> argumentTypes)
    {
        this(name, typeVariableConstraints, longVariableConstraints, returnType, argumentTypes, AGGREGATE);
    }

    protected SqlAggregationFunction(
            String name,
            List<TypeVariableConstraint> typeVariableConstraints,
            List<LongVariableConstraint> longVariableConstraints,
            TypeSignature returnType,
            List<TypeSignature> argumentTypes,
            FunctionKind kind)
    {
        requireNonNull(name, "name is null");
        requireNonNull(typeVariableConstraints, "typeVariableConstraints is null");
        requireNonNull(longVariableConstraints, "longVariableConstraints is null");
        requireNonNull(returnType, "returnType is null");
        requireNonNull(argumentTypes, "argumentTypes is null");
        checkArgument(kind == AGGREGATE || kind == APPROXIMATE_AGGREGATE, "kind must be an aggregate");
        this.signature = new Signature(
                name,
                kind,
                ImmutableList.copyOf(typeVariableConstraints),
                ImmutableList.copyOf(longVariableConstraints),
                returnType,
                ImmutableList.copyOf(argumentTypes),
                false);
    }

    @Override
    public final Signature getSignature()
    {
        return signature;
    }

    @Override
    public SignatureBinder getSignatureBinder(TypeManager typeManager, boolean allowCoercion)
    {
        return new DefaultSignatureBinder(typeManager, getSignature(), allowCoercion);
    }

    @Override
    public boolean isHidden()
    {
        return false;
    }

    @Override
    public boolean isDeterministic()
    {
        return true;
    }

    public abstract InternalAggregationFunction specialize(BoundVariables boundVariables, int arity, TypeManager typeManager, FunctionRegistry functionRegistry);

    public static class AnnotationBasedSqlAggregationFunction
            extends SqlAggregationFunction
    {
        private final BindableAggregationFunction aggregationSignature;

        protected AnnotationBasedSqlAggregationFunction(BindableAggregationFunction aggregationSignature)
        {
            super(aggregationSignature);
            this.aggregationSignature = aggregationSignature;
        }

        @Override
        public String getDescription()
        {
            return aggregationSignature.getDescription();
        }

        @Override
        public InternalAggregationFunction specialize(BoundVariables boundVariables, int arity, TypeManager typeManager, FunctionRegistry functionRegistry)
        {
            return aggregationSignature.specialize(boundVariables, typeManager, arity);
        }
    }
}

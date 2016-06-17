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
package com.facebook.presto.operator.aggregation;

import com.facebook.presto.operator.aggregation.state.DigestAndPercentileState;
import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.type.StandardTypes;
import com.facebook.presto.type.SqlType;
import com.google.common.collect.ImmutableList;
import io.airlift.stats.QuantileDigest;

import static com.facebook.presto.spi.StandardErrorCode.INVALID_FUNCTION_ARGUMENT;
import static com.facebook.presto.spi.type.BigintType.BIGINT;
import static com.facebook.presto.spi.type.DoubleType.DOUBLE;
import static com.facebook.presto.spi.type.FloatType.FLOAT;
import static com.facebook.presto.util.Failures.checkCondition;
import static com.google.common.base.Preconditions.checkState;

@AggregationFunction("approx_percentile")
public class ApproximateFloatPercentileAggregations
{
    public static final InternalAggregationFunction FLOAT_APPROXIMATE_PERCENTILE_AGGREGATION = new AggregationCompiler().generateAggregationFunction(ApproximateFloatPercentileAggregations.class, FLOAT, ImmutableList.of(FLOAT, DOUBLE));
    public static final InternalAggregationFunction FLOAT_APPROXIMATE_PERCENTILE_WEIGHTED_AGGREGATION = new AggregationCompiler().generateAggregationFunction(ApproximateFloatPercentileAggregations.class, FLOAT, ImmutableList.of(FLOAT, BIGINT, DOUBLE));
    public static final InternalAggregationFunction FLOAT_APPROXIMATE_PERCENTILE_WEIGHTED_AGGREGATION_WITH_ACCURACY = new AggregationCompiler().generateAggregationFunction(ApproximateFloatPercentileAggregations.class, FLOAT, ImmutableList.of(FLOAT, BIGINT, DOUBLE, DOUBLE));

    private ApproximateFloatPercentileAggregations() {}

    @InputFunction
    public static void input(DigestAndPercentileState state, @SqlType(StandardTypes.FLOAT) long value, @SqlType(StandardTypes.DOUBLE) double percentile)
    {
        ApproximateLongPercentileAggregations.input(state, value, percentile);
    }

    @InputFunction
    public static void weightedInput(DigestAndPercentileState state, @SqlType(StandardTypes.FLOAT) long value, @SqlType(StandardTypes.BIGINT) long weight, @SqlType(StandardTypes.DOUBLE) double percentile)
    {
        ApproximateLongPercentileAggregations.weightedInput(state, value, weight, percentile);
    }

    @InputFunction
    public static void weightedInput(DigestAndPercentileState state, @SqlType(StandardTypes.FLOAT) long value, @SqlType(StandardTypes.BIGINT) long weight, @SqlType(StandardTypes.DOUBLE) double percentile, @SqlType(StandardTypes.DOUBLE) double accuracy)
    {
        ApproximateLongPercentileAggregations.weightedInput(state, value, weight, percentile, accuracy);
    }

    @CombineFunction
    public static void combine(DigestAndPercentileState state, DigestAndPercentileState otherState)
    {
        ApproximateLongPercentileAggregations.combine(state, otherState);
    }

    @OutputFunction(StandardTypes.FLOAT)
    public static void output(DigestAndPercentileState state, BlockBuilder out)
    {
        QuantileDigest digest = state.getDigest();
        double percentile = state.getPercentile();
        if (digest == null || digest.getCount() == 0.0) {
            out.appendNull();
        }
        else {
            checkState(percentile != -1.0, "Percentile is missing");
            checkCondition(0 <= percentile && percentile <= 1, INVALID_FUNCTION_ARGUMENT, "Percentile must be between 0 and 1");
            FLOAT.writeLong(out, digest.getQuantile(percentile));
        }
    }
}

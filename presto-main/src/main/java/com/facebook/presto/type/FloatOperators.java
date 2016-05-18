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
package com.facebook.presto.type;

import com.facebook.presto.operator.scalar.ScalarOperator;
import com.facebook.presto.spi.PrestoException;
import com.facebook.presto.spi.type.StandardTypes;
import io.airlift.slice.Slice;

import static com.facebook.presto.metadata.OperatorType.ADD;
import static com.facebook.presto.metadata.OperatorType.BETWEEN;
import static com.facebook.presto.metadata.OperatorType.CAST;
import static com.facebook.presto.metadata.OperatorType.DIVIDE;
import static com.facebook.presto.metadata.OperatorType.EQUAL;
import static com.facebook.presto.metadata.OperatorType.GREATER_THAN;
import static com.facebook.presto.metadata.OperatorType.GREATER_THAN_OR_EQUAL;
import static com.facebook.presto.metadata.OperatorType.HASH_CODE;
import static com.facebook.presto.metadata.OperatorType.LESS_THAN;
import static com.facebook.presto.metadata.OperatorType.LESS_THAN_OR_EQUAL;
import static com.facebook.presto.metadata.OperatorType.MODULUS;
import static com.facebook.presto.metadata.OperatorType.MULTIPLY;
import static com.facebook.presto.metadata.OperatorType.NEGATION;
import static com.facebook.presto.metadata.OperatorType.NOT_EQUAL;
import static com.facebook.presto.metadata.OperatorType.SUBTRACT;
import static com.facebook.presto.spi.StandardErrorCode.NUMERIC_VALUE_OUT_OF_RANGE;
import static io.airlift.slice.Slices.utf8Slice;
import static java.lang.String.format;
import static java.lang.String.valueOf;

public final class FloatOperators
{
    private FloatOperators()
    {
    }

    @ScalarOperator(ADD)
    @SqlType(StandardTypes.FLOAT)
    public static long add(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.floatToRawIntBits(Float.intBitsToFloat((int) left) + Float.intBitsToFloat((int) right));
    }

    @ScalarOperator(SUBTRACT)
    @SqlType(StandardTypes.FLOAT)
    public static long subtract(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.floatToRawIntBits(Float.intBitsToFloat((int) left) - Float.intBitsToFloat((int) right));
    }

    @ScalarOperator(MULTIPLY)
    @SqlType(StandardTypes.FLOAT)
    public static long multiply(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.floatToRawIntBits(Float.intBitsToFloat((int) left) * Float.intBitsToFloat((int) right));
    }

    @ScalarOperator(DIVIDE)
    @SqlType(StandardTypes.FLOAT)
    public static long divide(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.floatToRawIntBits(Float.intBitsToFloat((int) left) / Float.intBitsToFloat((int) right));
    }

    @ScalarOperator(MODULUS)
    @SqlType(StandardTypes.FLOAT)
    public static long modulus(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.floatToRawIntBits(Float.intBitsToFloat((int) left) % Float.intBitsToFloat((int) right));
    }

    @ScalarOperator(NEGATION)
    @SqlType(StandardTypes.FLOAT)
    public static long negate(@SqlType(StandardTypes.FLOAT) long value)
    {
        ensureFloatInRange(value);
        return Float.floatToRawIntBits(-Float.intBitsToFloat((int) value));
    }

    @ScalarOperator(EQUAL)
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean equal(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.intBitsToFloat((int) left) == Float.intBitsToFloat((int) right);
    }

    @ScalarOperator(NOT_EQUAL)
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean notEqual(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.intBitsToFloat((int) left) != Float.intBitsToFloat((int) right);
    }

    @ScalarOperator(LESS_THAN)
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean lessThan(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.intBitsToFloat((int) left) < Float.intBitsToFloat((int) right);
    }

    @ScalarOperator(LESS_THAN_OR_EQUAL)
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean lessThanOrEqual(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.intBitsToFloat((int) left) <= Float.intBitsToFloat((int) right);
    }

    @ScalarOperator(GREATER_THAN)
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean greaterThan(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.intBitsToFloat((int) left) > Float.intBitsToFloat((int) right);
    }

    @ScalarOperator(GREATER_THAN_OR_EQUAL)
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean greaterThanOrEqual(@SqlType(StandardTypes.FLOAT) long left, @SqlType(StandardTypes.FLOAT) long right)
    {
        ensureFloatInRange(left);
        ensureFloatInRange(right);
        return Float.intBitsToFloat((int) left) >= Float.intBitsToFloat((int) right);
    }

    @ScalarOperator(BETWEEN)
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean between(@SqlType(StandardTypes.FLOAT) long value, @SqlType(StandardTypes.FLOAT) long min, @SqlType(StandardTypes.FLOAT) long max)
    {
        ensureFloatInRange(value);
        ensureFloatInRange(min);
        ensureFloatInRange(max);

        return Float.intBitsToFloat((int) min) <= Float.intBitsToFloat((int) value) &&
                Float.intBitsToFloat((int) value) <= Float.intBitsToFloat((int) max);
    }

    @ScalarOperator(HASH_CODE)
    @SqlType(StandardTypes.BIGINT)
    public static long hashCode(@SqlType(StandardTypes.FLOAT) long value)
    {
        ensureFloatInRange(value);
        return value;
    }

    @ScalarOperator(CAST)
    @SqlType(StandardTypes.VARCHAR)
    public static Slice castToVarchar(@SqlType(StandardTypes.FLOAT) long value)
    {
        ensureFloatInRange(value);
        return utf8Slice(valueOf(Float.intBitsToFloat((int) value)));
    }

    @ScalarOperator(CAST)
    @SqlType(StandardTypes.BIGINT)
    public static long castToLong(@SqlType(StandardTypes.FLOAT) long value)
    {
        ensureFloatInRange(value);
        return (long) Float.intBitsToFloat((int) value);
    }

    @ScalarOperator(CAST)
    @SqlType(StandardTypes.INTEGER)
    public static long castToInteger(@SqlType(StandardTypes.FLOAT) long value)
    {
        ensureFloatInRange(value);
        return (int) Float.intBitsToFloat((int) value);
    }

    @ScalarOperator(CAST)
    @SqlType(StandardTypes.DOUBLE)
    public static double castToDouble(@SqlType(StandardTypes.FLOAT) long value)
    {
        ensureFloatInRange(value);
        return (double) Float.intBitsToFloat((int) value);
    }

    @ScalarOperator(CAST)
    @SqlType(StandardTypes.BOOLEAN)
    public static boolean castToBoolean(@SqlType(StandardTypes.FLOAT) long value)
    {
        ensureFloatInRange(value);
        return Float.intBitsToFloat((int) value) != 0.0f;
    }

    private static void ensureFloatInRange(long value)
    {
        try {
            Math.toIntExact(value);
        }
        catch (ArithmeticException e) {
            throw new PrestoException(NUMERIC_VALUE_OUT_OF_RANGE, format("More than 32b value provided for Float type. Value given: %s", hex(value)));
        }
    }

    private static String hex(long value)
    {
        return format("0x%16s", Long.toHexString(value)).replace(' ', '0');
    }
}

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
package com.facebook.presto.testing;

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * Materialize all values in a row
 * Special handling is added for Double types for approximate comparisons
 */
public class MaterializedRow
{
    private final int precision;
    private final List<Object> values;

    public MaterializedRow(int precision, Object... values)
    {
        this(precision, Arrays.asList(requireNonNull(values, "values is null")));
    }

    public MaterializedRow(int precision, List<Object> values)
    {
        checkArgument(precision > 0, "Need at least one digit of precision");
        this.precision = precision;

        this.values = (List<Object>) processValue(precision, values);
    }

    private static Object processValue(int precision, Object value)
    {
        checkArgument(!(value instanceof Short || value instanceof Byte), "SMALLINT and TINYINT are not yet supported");
        if (value instanceof Double || value instanceof Float) {
            return new ApproximateDouble(((Number) value), precision);
        }
        if (value instanceof List) {
            return ((List<?>) value).stream()
                    .map(element -> processValue(precision, element))
                    .collect(toList());
        }
        if (value instanceof Map) {
            Map<Object, Object> map = new HashMap<>();
            for (Entry<Object, Object> entry : ((Map<Object, Object>) value).entrySet()) {
                map.put(processValue(precision, entry.getKey()), processValue(precision, entry.getValue()));
            }
            return map;
        }
        return value;
    }

    public int getPrecision()
    {
        return precision;
    }

    public int getFieldCount()
    {
        return values.size();
    }

    public List<Object> getFields()
    {
        return values.stream()
                .map(MaterializedRow::processField)
                .collect(toList());
    }

    public Object getField(int field)
    {
        Preconditions.checkElementIndex(field, values.size());
        return processField(values.get(field));
    }

    private static Object processField(Object value)
    {
        if (value instanceof ApproximateDouble) {
            return ((ApproximateDouble) value).getValue();
        }
        if (value instanceof List) {
            return ((List<?>) value).stream()
                    .map(MaterializedRow::processField)
                    .collect(toList());
        }
        if (value instanceof Map) {
            Map<Object, Object> map = new HashMap<>();
            for (Entry<Object, Object> entry : ((Map<Object, Object>) value).entrySet()) {
                map.put(processField(entry.getKey()), processField(entry.getValue()));
            }
            return map;
        }

        return value;
    }

    @Override
    public String toString()
    {
        return values.toString();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }
        MaterializedRow o = (MaterializedRow) obj;
        return Objects.equals(values, o.values);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(values);
    }

    private static class ApproximateDouble
    {
        private final Number value;
        private final Number normalizedValue;

        private ApproximateDouble(Number value, int precision)
        {
            checkArgument(value instanceof Float || value instanceof Double, "Only floating point values accepted");
            this.value = value;
            if (value instanceof Float) {
                normalizedValue = normalizeFloat((Float) value, precision);
            }
            else if (value instanceof Double) {
                normalizedValue = normalizeDouble((Double) value, precision);
            }
            else {
                throw new IllegalStateException("unreachable"); // to silence "uninitialized" error for normalizedValue
            }
        }

        private static Float normalizeFloat(Float value, int precision)
        {
            if (value.isNaN() || value.isInfinite()) {
                return value;
            }
            else {
                return new BigDecimal(value).round(new MathContext(precision)).floatValue();
            }
        }

        private static Double normalizeDouble(Double value, int precision)
        {
            if (value.isNaN() || value.isInfinite()) {
                return value;
            }
            else {
                return new BigDecimal(value).round(new MathContext(precision)).doubleValue();
            }
        }

        public Number getValue()
        {
            return value;
        }

        @Override
        public String toString()
        {
            return value.toString();
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj == this) {
                return true;
            }
            if ((obj == null) || (getClass() != obj.getClass())) {
                return false;
            }
            ApproximateDouble o = (ApproximateDouble) obj;
            return Objects.equals(normalizedValue, o.normalizedValue);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(normalizedValue);
        }
    }
}

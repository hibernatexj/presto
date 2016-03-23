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
package com.facebook.presto.operator;

import com.facebook.presto.sql.planner.plan.PlanNodeId;
import io.airlift.json.JsonCodec;
import io.airlift.units.DataSize;
import io.airlift.units.Duration;
import org.testng.annotations.Test;

import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.MoreObjects.toStringHelper;
import static io.airlift.units.DataSize.Unit.BYTE;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.testng.Assert.assertEquals;

public class TestOperatorStats
{
    public static final OperatorStats EXPECTED = new OperatorStats(
            41,
            new PlanNodeId("test"),
            "test",

            1,

            2,
            new Duration(3, NANOSECONDS),
            new Duration(4, NANOSECONDS),
            new Duration(5, NANOSECONDS),
            new DataSize(6, BYTE),
            7,
            8d,

            9,
            new Duration(10, NANOSECONDS),
            new Duration(11, NANOSECONDS),
            new Duration(12, NANOSECONDS),
            new DataSize(13, BYTE),
            14,

            new Duration(15, NANOSECONDS),

            16,
            new Duration(17, NANOSECONDS),
            new Duration(18, NANOSECONDS),
            new Duration(19, NANOSECONDS),

            new DataSize(20, BYTE),
            new DataSize(21, BYTE),
            Optional.empty(),
            "22");

    public static final OperatorStats MERGEABLE = new OperatorStats(
            41,
            new PlanNodeId("test"),
            "test",

            1,

            2,
            new Duration(3, NANOSECONDS),
            new Duration(4, NANOSECONDS),
            new Duration(5, NANOSECONDS),
            new DataSize(6, BYTE),
            7,
            8d,

            9,
            new Duration(10, NANOSECONDS),
            new Duration(11, NANOSECONDS),
            new Duration(12, NANOSECONDS),
            new DataSize(13, BYTE),
            14,

            new Duration(15, NANOSECONDS),

            16,
            new Duration(17, NANOSECONDS),
            new Duration(18, NANOSECONDS),
            new Duration(19, NANOSECONDS),

            new DataSize(20, BYTE),
            new DataSize(21, BYTE),
            Optional.empty(),
            new LongMergeable(22));

    @Test
    public void testJson()
    {
        JsonCodec<OperatorStats> codec = JsonCodec.jsonCodec(OperatorStats.class);

        String json = codec.toJson(EXPECTED);
        OperatorStats actual = codec.fromJson(json);

        assertExpectedOperatorStats(actual);
    }

    public static void assertExpectedOperatorStats(OperatorStats actual)
    {
        assertEquals(actual.getOperatorId(), 41);
        assertEquals(actual.getOperatorType(), "test");

        assertEquals(actual.getTotalDrivers(), 1);

        assertEquals(actual.getAddInputCalls(), 2);
        assertEquals(actual.getAddInputWall(), new Duration(3, NANOSECONDS));
        assertEquals(actual.getAddInputCpu(), new Duration(4, NANOSECONDS));
        assertEquals(actual.getAddInputUser(), new Duration(5, NANOSECONDS));
        assertEquals(actual.getInputDataSize(), new DataSize(6, BYTE));
        assertEquals(actual.getInputPositions(), 7);
        assertEquals(actual.getSumSquaredInputPositions(), 8d);

        assertEquals(actual.getGetOutputCalls(), 9);
        assertEquals(actual.getGetOutputWall(), new Duration(10, NANOSECONDS));
        assertEquals(actual.getGetOutputCpu(), new Duration(11, NANOSECONDS));
        assertEquals(actual.getGetOutputUser(), new Duration(12, NANOSECONDS));
        assertEquals(actual.getOutputDataSize(), new DataSize(13, BYTE));
        assertEquals(actual.getOutputPositions(), 14);

        assertEquals(actual.getBlockedWall(), new Duration(15, NANOSECONDS));

        assertEquals(actual.getFinishCalls(), 16);
        assertEquals(actual.getFinishWall(), new Duration(17, NANOSECONDS));
        assertEquals(actual.getFinishCpu(), new Duration(18, NANOSECONDS));
        assertEquals(actual.getFinishUser(), new Duration(19, NANOSECONDS));

        assertEquals(actual.getMemoryReservation(), new DataSize(20, BYTE));
        assertEquals(actual.getSystemMemoryReservation(), new DataSize(21, BYTE));
        assertEquals(actual.getInfo(), "22");
    }

    @Test
    public void testAdd()
    {
        OperatorStats actual = EXPECTED.add(EXPECTED, EXPECTED);

        assertEquals(actual.getOperatorId(), 41);
        assertEquals(actual.getOperatorType(), "test");

        assertEquals(actual.getTotalDrivers(), 3);

        assertEquals(actual.getAddInputCalls(), 3 * 2);
        assertEquals(actual.getAddInputWall(), new Duration(3 * 3, NANOSECONDS));
        assertEquals(actual.getAddInputCpu(), new Duration(3 * 4, NANOSECONDS));
        assertEquals(actual.getAddInputUser(), new Duration(3 * 5, NANOSECONDS));
        assertEquals(actual.getInputDataSize(), new DataSize(3 * 6, BYTE));
        assertEquals(actual.getInputPositions(), 3 * 7);
        assertEquals(actual.getSumSquaredInputPositions(), 3 * 8d);

        assertEquals(actual.getGetOutputCalls(), 3 * 9);
        assertEquals(actual.getGetOutputWall(), new Duration(3 * 10, NANOSECONDS));
        assertEquals(actual.getGetOutputCpu(), new Duration(3 * 11, NANOSECONDS));
        assertEquals(actual.getGetOutputUser(), new Duration(3 * 12, NANOSECONDS));
        assertEquals(actual.getOutputDataSize(), new DataSize(3 * 13, BYTE));
        assertEquals(actual.getOutputPositions(), 3 * 14);

        assertEquals(actual.getBlockedWall(), new Duration(3 * 15, NANOSECONDS));

        assertEquals(actual.getFinishCalls(), 3 * 16);
        assertEquals(actual.getFinishWall(), new Duration(3 * 17, NANOSECONDS));
        assertEquals(actual.getFinishCpu(), new Duration(3 * 18, NANOSECONDS));
        assertEquals(actual.getFinishUser(), new Duration(3 * 19, NANOSECONDS));

        assertEquals(actual.getMemoryReservation(), new DataSize(3 * 20, BYTE));
        assertEquals(actual.getSystemMemoryReservation(), new DataSize(3 * 21, BYTE));
        assertEquals(actual.getInfo(), null);
    }

    @Test
    public void testAddMergeable()
    {
        OperatorStats actual = MERGEABLE.add(MERGEABLE, MERGEABLE);

        assertEquals(actual.getOperatorId(), 41);
        assertEquals(actual.getOperatorType(), "test");

        assertEquals(actual.getTotalDrivers(), 3);

        assertEquals(actual.getAddInputCalls(), 3 * 2);
        assertEquals(actual.getAddInputWall(), new Duration(3 * 3, NANOSECONDS));
        assertEquals(actual.getAddInputCpu(), new Duration(3 * 4, NANOSECONDS));
        assertEquals(actual.getAddInputUser(), new Duration(3 * 5, NANOSECONDS));
        assertEquals(actual.getInputDataSize(), new DataSize(3 * 6, BYTE));
        assertEquals(actual.getInputPositions(), 3 * 7);
        assertEquals(actual.getSumSquaredInputPositions(), 3 * 8d);

        assertEquals(actual.getGetOutputCalls(), 3 * 9);
        assertEquals(actual.getGetOutputWall(), new Duration(3 * 10, NANOSECONDS));
        assertEquals(actual.getGetOutputCpu(), new Duration(3 * 11, NANOSECONDS));
        assertEquals(actual.getGetOutputUser(), new Duration(3 * 12, NANOSECONDS));
        assertEquals(actual.getOutputDataSize(), new DataSize(3 * 13, BYTE));
        assertEquals(actual.getOutputPositions(), 3 * 14);

        assertEquals(actual.getBlockedWall(), new Duration(3 * 15, NANOSECONDS));

        assertEquals(actual.getFinishCalls(), 3 * 16);
        assertEquals(actual.getFinishWall(), new Duration(3 * 17, NANOSECONDS));
        assertEquals(actual.getFinishCpu(), new Duration(3 * 18, NANOSECONDS));
        assertEquals(actual.getFinishUser(), new Duration(3 * 19, NANOSECONDS));

        assertEquals(actual.getMemoryReservation(), new DataSize(3 * 20, BYTE));
        assertEquals(actual.getSystemMemoryReservation(), new DataSize(3 * 21, BYTE));
        assertEquals(actual.getInfo(), new LongMergeable(22 * 3));
    }

    private static class LongMergeable
            implements Mergeable<LongMergeable>
    {
        private final long value;

        private LongMergeable(long value)
        {
            this.value = value;
        }

        @Override
        public LongMergeable mergeWith(LongMergeable other)
        {
            return new LongMergeable(value + other.value);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(value);
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            LongMergeable other = (LongMergeable) obj;
            return Objects.equals(this.value, other.value);
        }

        @Override
        public String toString()
        {
            return toStringHelper(this)
                    .add("value", value)
                    .toString();
        }
    }
}

/*
 * Copyright © 2022 the original author or authors.
 *
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
package com.photowey.component.common.calculator;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

/**
 * {@code DateTimeCalculator}
 *
 * @author photowey
 * @date 2023/04/08
 * @since 1.0.0
 */
public interface DateTimeCalculator {

    long MILLIS_PER_SECOND = TimeUnit.SECONDS.toMillis(1);
    long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

    static boolean between(LocalDateTime target, LocalDateTime from, LocalDateTime to) {
        if (null == target || null == from || null == to) {
            return false;
        }

        return from.isBefore(target) && target.isBefore(to);
    }

    static boolean between(long target, long from, long to) {
        return from < target && target < to;
    }

    static boolean notBetween(LocalDateTime target, LocalDateTime from, LocalDateTime to) {
        return !between(target, from, to);
    }

    static boolean notBetween(long target, long from, long to) {
        return !between(target, from, to);
    }

    static long determineDuration(LocalDateTime from, LocalDateTime to) {
        if (null == from || null == to) {
            return 0L;
        }

        long tsFrom = from.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long tsTo = to.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        return determineDuration(tsFrom, tsTo);
    }

    static long determineDuration(long from, long to) {
        return to - from;
    }

    static long determineDurationNow(long from, long to) {
        long now = System.currentTimeMillis() / MILLIS_PER_SECOND * MILLIS_PER_SECOND;
        long start = Math.min(now, from);

        return to - start;
    }
}

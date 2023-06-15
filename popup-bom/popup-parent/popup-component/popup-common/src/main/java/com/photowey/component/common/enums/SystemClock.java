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
package com.photowey.component.common.enums;

import com.photowey.component.common.constant.PopupConstants;
import com.photowey.component.common.date.DateUtils;

/**
 * {@code SystemClock}
 *
 * @author photowey
 * @date 2023/06/02
 * @since 1.0.0
 */
public enum SystemClock {

    ;

    public enum Strings {

        ;

        public static String now() {
            return DateUtils.nowStr();
        }

        public static java.time.LocalDateTime transfer(String dateTime) {
            return DateUtils.toLocalDateTime(dateTime);
        }

        public static java.time.LocalDateTime transfer(String dateTime, String pattern) {
            return DateUtils.toLocalDateTime(dateTime, pattern);
        }
    }

    public enum Timestamp {

        ;

        public static long now() {
            return System.currentTimeMillis() / PopupConstants.MILLIS_UNIT * PopupConstants.MILLIS_UNIT;
        }

        public static java.time.LocalDateTime transfer(Long ts) {
            if (null == ts || 0L == ts) {
                return null;
            }
            if (String.valueOf(ts).length() < PopupConstants.TIME_STAMP_LENGTH) {
                return transferShort(ts);
            }

            return DateUtils.toLocalDateTime(ts);
        }

        public static java.time.LocalDateTime transferShort(Long ts) {
            if (null == ts || 0L == ts) {
                return null;
            }
            if (String.valueOf(ts).length() >= PopupConstants.TIME_STAMP_LENGTH) {
                return transfer(ts);
            }

            return transfer(ts * PopupConstants.MILLIS_UNIT);
        }
    }

    public enum LocalDateTime {

        ;

        public static java.time.LocalDateTime now() {
            return LocalDateTime.now();
        }

        public static Long transfer(java.time.LocalDateTime time) {
            return DateUtils.toTimestamp(time);
        }

        public static String transfers(java.time.LocalDateTime time) {
            return DateUtils.format(time);
        }

        public static String transfers(java.time.LocalDateTime time, String pattern) {
            return DateUtils.format(time, pattern);
        }
    }
}

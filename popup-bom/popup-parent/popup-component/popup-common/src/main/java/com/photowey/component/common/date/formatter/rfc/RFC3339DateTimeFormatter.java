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
package com.photowey.component.common.date.formatter.rfc;

import com.photowey.component.common.date.DatePatternConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

/**
 * {@code RFC3339DateTimeFormatter}
 *
 * @author photowey
 * @date 2022/12/28
 * @since 1.0.0
 */
public interface RFC3339DateTimeFormatter {

    String RFC_3339_DEFAULT_ZONE_PATTERN = rfc3339DefaultZonePattern();

    static String rfc3339DefaultZonePattern() {
        return rfc3339ZonePattern(DatePatternConstants.GMT_8);
    }

    static String rfc3339ZonePattern(String zone) {
        StringBuilder buf = new StringBuilder();
        buf.append(DatePatternConstants.yyyy_MM_dd)
                .append(DatePatternConstants.RFC_3339_T_STRING)
                .append(DatePatternConstants.HH_mm_ss)
                .append(zone);

        return buf.toString();
    }

    static DateTimeFormatter buildDefault() {
        return build(DatePatternConstants.GMT_8);
    }

    static DateTimeFormatter build(String zone) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .appendLiteral(DatePatternConstants.RFC_3339_T)
                .append(DateTimeFormatter.ISO_LOCAL_TIME)
                .appendLiteral(zone)
                .toFormatter();
    }

    static String format(LocalDateTime dateTime) {
        if (null == dateTime) {
            return null;
        }

        DateTimeFormatter formatter = buildDefault();
        return formatter.format(dateTime);
    }

    static String format() {
        return format(LocalDateTime.now());
    }

    static LocalDateTime toLocalDateTime(String dateTime) {
        if (null == dateTime || dateTime.trim().length() == 0) {
            return null;
        }

        return LocalDateTime.parse(dateTime, buildDefault());
    }

}

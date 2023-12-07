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

/**
 * {@code WeekDayEnum}
 *
 * @author photowey
 * @date 2023/05/23
 * @since 1.0.0
 */
public enum WeekDayEnum {

    // Monday、Tuesday、Wednesday、Thursday、Friday、Saturday、Sunday

    MONDAY("Monday", 1 << (1 - 1)),
    TUESDAY("Tuesday", 1 << (2 - 1)),
    WEDNESDAY("Wednesday", 1 << (3 - 1)),
    THURSDAY("Thursday", 1 << (4 - 1)),
    FRIDAY("Friday", 1 << (5 - 1)),
    SATURDAY("Saturday", 1 << (6 - 1)),
    SUNDAY("Sunday", 1 << (7 - 1)),

    FULL("full", 1 | 2 | 4 | 8 | 16 | 32 | 64),

    ;

    private final String name;
    private final int value;

    WeekDayEnum(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String toName() {
        return name;
    }

    public int value() {
        return value;
    }

    public int determineWeekdays(WeekDayEnum... days) {
        int weekdays = 1;
        for (WeekDayEnum day : days) {
            weekdays |= day.value();
        }

        return weekdays;
    }
}

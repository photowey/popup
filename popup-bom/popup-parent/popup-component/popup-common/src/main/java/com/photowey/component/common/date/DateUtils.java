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
package com.photowey.component.common.date;

import com.photowey.component.common.constant.PopupConstants;
import com.photowey.component.common.date.formatter.rfc.RFC3339DateTimeFormatter;
import com.photowey.component.common.thrower.AssertionErrorThrower;
import com.photowey.component.common.util.ObjectUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;
import java.util.Locale;

/**
 * {@code DateUtils}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public final class DateUtils {

    private DateUtils() {
        // utility class; can't create
        AssertionErrorThrower.throwz(DateUtils.class);
    }

    // ------------------------------------------------------------------------- Formatter

    public static DateTimeFormatter rfc3339DefaultZoneFormatter() {
        return RFC3339DateTimeFormatter.buildDefault();
    }

    public static DateTimeFormatter rfc3339ZoneFormatter(String zone) {
        return RFC3339DateTimeFormatter.build(zone);
    }

    public static DateTimeFormatter formatter() {
        return formatter(DatePatternConstants.yyyy_MM_dd_HH_mm_ss);
    }

    public static DateTimeFormatter formatter(String pattern) {
        pattern = ObjectUtils.isNotNullOrEmpty(pattern) ? pattern : DatePatternConstants.yyyy_MM_dd_HH_mm_ss;

        return DateTimeFormatter.ofPattern(pattern);
    }

    // ------------------------------------------------------------------------- Format

    public static String format(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return format(target, DatePatternConstants.yyyy_MM_dd_HH_mm_ss);
    }

    public static String formatRfc3339(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        DateTimeFormatter formatter = rfc3339DefaultZoneFormatter();
        return formatter.format(target);
    }

    public static String format(LocalDateTime target, String pattern) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }
        pattern = ObjectUtils.isNotNullOrEmpty(pattern) ? pattern : DatePatternConstants.yyyy_MM_dd_HH_mm_ss;

        DateTimeFormatter formatter = formatter(pattern);
        return formatter.format(target);
    }

    public static String format(LocalDate target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return format(target, DatePatternConstants.yyyy_MM_dd);
    }

    public static String format(LocalDate target, String pattern) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }
        pattern = ObjectUtils.isNotNullOrEmpty(pattern) ? pattern : DatePatternConstants.yyyy_MM_dd_HH_mm_ss;

        DateTimeFormatter formatter = formatter(pattern);
        return formatter.format(target);
    }

    public static String format(LocalTime target) {
        return format(target, DatePatternConstants.HH_mm_ss);
    }

    public static String format(LocalTime target, String pattern) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }
        pattern = ObjectUtils.isNotNullOrEmpty(pattern) ? pattern : DatePatternConstants.yyyy_MM_dd_HH_mm_ss;

        DateTimeFormatter formatter = formatter(pattern);
        return formatter.format(target);
    }

    public static String format(Date target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return format(target, DatePatternConstants.yyyy_MM_dd_HH_mm_ss);
    }

    public static String format(Date target, String pattern) {
        return format(toLocalDateTime(target), pattern);
    }

    // ------------------------------------------------------------------------- now

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String nowStr() {
        return formatter().format(LocalDateTime.now());
    }

    public static String now(String pattern) {
        LocalDateTime now = LocalDateTime.now();

        return formatter(pattern).format(now);
    }

    // ------------------------------------------------------------------------- Date

    public static Date toDate(LocalDate date) {
        if (ObjectUtils.isNullOrEmpty(date)) {
            return null;
        }

        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date toDate(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return Date.from(target.atZone(ZoneId.systemDefault()).toInstant());
    }

    // ------------------------------------------------------------------------- LocalDate

    public static LocalDate toLocalDate(Date date) {
        if (ObjectUtils.isNullOrEmpty(date)) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // ------------------------------------------------------------------------- LocalDateTime

    public static LocalDateTime toLocalDateTime(Date date) {
        if (ObjectUtils.isNullOrEmpty(date)) {
            return null;
        }

        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(Long timestamp) {
        if (ObjectUtils.isNullOrEmpty(timestamp)) {
            return null;
        }

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    // ------------------------------------------------------------------------- Parse

    public static LocalDateTime toLocalDateTime(String target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return LocalDateTime.parse(target, formatter());
    }

    public static LocalDateTime toLocalDateTime(String target, String pattern) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return LocalDateTime.parse(target, formatter(pattern));
    }

    public static LocalDateTime toLocalDateTimeRfc3339(String target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return LocalDateTime.parse(target, rfc3339DefaultZoneFormatter());
    }

    // ------------------------------------------------------------------------- Timestamp

    public static Long toTimestamp(Date target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return trimTail(target.getTime());
    }

    public static Long toTimestamp(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return trimTail(target.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }

    public static long trimTail(long ts) {
        return ts / PopupConstants.MILLIS_UNIT * PopupConstants.MILLIS_UNIT;
    }

    // ------------------------------------------------------------------------- between

    public static boolean betweenNow(LocalDateTime from, LocalDateTime to) {
        if (ObjectUtils.isNullOrEmpty(from)
                || ObjectUtils.isNullOrEmpty(to)) {
            return false;
        }
        LocalDateTime now = now();
        return from.isBefore(now) && to.isAfter(now);
    }

    public static boolean betweenNow(LocalTime from, LocalTime to) {
        if (ObjectUtils.isNullOrEmpty(from)
                || ObjectUtils.isNullOrEmpty(to)) {
            return false;
        }
        LocalTime now = now().toLocalTime();
        return from.isBefore(now) && to.isAfter(now);
    }

    public static boolean between(LocalDateTime target, LocalDateTime from, LocalDateTime to) {
        if (ObjectUtils.isNullOrEmpty(target)
                || ObjectUtils.isNullOrEmpty(from)
                || ObjectUtils.isNullOrEmpty(to)) {
            return false;
        }

        return from.isBefore(target) && to.isAfter(target);
    }

    public static boolean between(LocalTime target, LocalTime from, LocalTime to) {
        if (ObjectUtils.isNullOrEmpty(target)
                || ObjectUtils.isNullOrEmpty(from)
                || ObjectUtils.isNullOrEmpty(to)) {
            return false;
        }

        return from.isBefore(target) && to.isAfter(target);
    }

    // ------------------------------------------------------------------------- dayStart

    public static LocalDateTime dayStart() {
        return dayStart(now());
    }

    public static LocalDateTime dayStart(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return LocalDateTime.of(target.toLocalDate(), LocalTime.MIN);
    }

    // ------------------------------------------------------------------------- dayEnd

    public static LocalDateTime dayEnd() {
        return dayEnd(now());
    }

    public static LocalDateTime dayEnd(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return LocalDateTime.of(target.toLocalDate(), LocalTime.MAX);
    }

    // ------------------------------------------------------------------------- weekStart

    public static LocalDateTime weekStart() {
        return weekStart(now());
    }

    public static LocalDateTime weekStart(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        TemporalField week = WeekFields.of(Locale.CHINA).dayOfWeek();
        ZonedDateTime zdt = target.atZone(ZoneId.systemDefault()).with(week, 2);
        return dayStart(zdt.toLocalDateTime());
    }

    // ------------------------------------------------------------------------- weekEnd

    public static LocalDateTime weekEnd() {
        return weekEnd(now());
    }

    public static LocalDateTime weekEnd(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        TemporalField week = WeekFields.of(Locale.CHINA).dayOfWeek();
        ZonedDateTime zdt = target.atZone(ZoneId.systemDefault()).with(week, 7);
        return dayEnd(zdt.toLocalDateTime().plusDays(1));
    }

    // ------------------------------------------------------------------------- monthStart

    public static LocalDateTime monthStart() {
        return monthStart(now());
    }

    public static LocalDateTime monthStart(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        ZonedDateTime zdt = target.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
        return dayStart(zdt.toLocalDateTime());
    }

    // ------------------------------------------------------------------------- monthEnd

    public static LocalDateTime monthEnd() {
        return monthEnd(now());
    }

    public static LocalDateTime monthEnd(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        ZonedDateTime zdt = target.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
        return dayEnd(zdt.toLocalDateTime());
    }

    // ------------------------------------------------------------------------- yearStart

    public static LocalDateTime yearStart() {
        return yearStart(now());
    }

    public static LocalDateTime yearStart(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        ZonedDateTime zdt = target.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
        return dayStart(zdt.toLocalDateTime());
    }

    // ------------------------------------------------------------------------- yearEnd

    public static LocalDateTime yearEnd() {
        return yearEnd(now());
    }

    public static LocalDateTime yearEnd(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        ZonedDateTime zdt = target.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
        return dayEnd(zdt.toLocalDateTime());
    }

    public static int lengthOfMonth(LocalDateTime now) {
        int month = now.getMonthValue();
        int year = now.getYear();
        return YearMonth.of(year, month).lengthOfMonth();
    }

    public static int lengthOfMonth(long ts) {
        LocalDateTime now = toLocalDateTime(ts);
        return lengthOfMonth(now);
    }
}

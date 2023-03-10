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
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    // ------------------------------------------------------------------------- Formatter

    public static DateTimeFormatter rfc3339FormatterGMT8() {
        return RFC3339DateTimeFormatter.rfc3339FormatterGMT8();
    }

    public static DateTimeFormatter rfc3339Formatter(String zone) {
        return RFC3339DateTimeFormatter.rfc3339Formatter(zone);
    }


    public static DateTimeFormatter formatter() {
        return formatter(DatePatternConstants.yyyy_MM_dd_HH_mm_ss);
    }

    public static DateTimeFormatter formatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    // ------------------------------------------------------------------------- Format

    public static String format(LocalDateTime dateTime) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        return format(dateTime, DatePatternConstants.yyyy_MM_dd_HH_mm_ss);
    }

    public static String formatRfc3339(LocalDateTime dateTime) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        DateTimeFormatter formatter = rfc3339FormatterGMT8();
        return formatter.format(dateTime);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        DateTimeFormatter formatter = formatter(pattern);
        return formatter.format(dateTime);
    }

    public static String format(LocalDate dateTime) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        return format(dateTime, DatePatternConstants.yyyy_MM_dd);
    }

    public static String format(LocalDate dateTime, String pattern) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        DateTimeFormatter formatter = formatter(pattern);
        return formatter.format(dateTime);
    }

    public static String format(LocalTime dateTime) {
        return format(dateTime, DatePatternConstants.HH_mm_ss);
    }

    public static String format(LocalTime dateTime, String pattern) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        DateTimeFormatter formatter = formatter(pattern);
        return formatter.format(dateTime);
    }

    public static String format(Date dateTime) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        return format(dateTime, DatePatternConstants.yyyy_MM_dd_HH_mm_ss);
    }

    public static String format(Date dateTime, String pattern) {
        return format(toLocalDateTime(dateTime), pattern);
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

    public static Date toDate(LocalDateTime dateTime) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
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
        if (null == timestamp) {
            return null;
        }

        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
    }

    // ------------------------------------------------------------------------- Parse

    public static LocalDateTime toLocalDateTime(String dateTime) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        return LocalDateTime.parse(dateTime, formatter());
    }

    public static LocalDateTime toLocalDateTime(String dateTime, String pattern) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        return LocalDateTime.parse(dateTime, formatter(pattern));
    }

    public static LocalDateTime toLocalDateTimeRfc3339(String dateTime) {
        if (ObjectUtils.isNullOrEmpty(dateTime)) {
            return null;
        }

        return LocalDateTime.parse(dateTime, rfc3339FormatterGMT8());
    }

    // ------------------------------------------------------------------------- Timestamp

    public static Long toTimestamp(Date target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return target.getTime();
    }

    public static Long toTimestamp(LocalDateTime target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return null;
        }

        return target.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    // ------------------------------------------------------------------------- between

    public static boolean betweenNow(LocalDateTime start, LocalDateTime end) {
        LocalDateTime now = now();
        return start.isBefore(now) && end.isAfter(now);
    }

    // ------------------------------------------------------------------------- dayStart

    public static LocalDateTime dayStart() {
        return dayStart(now());
    }

    public static LocalDateTime dayStart(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
    }

    // ------------------------------------------------------------------------- dayEnd

    public static LocalDateTime dayEnd() {
        return dayEnd(now());
    }

    public static LocalDateTime dayEnd(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);
    }

    // ------------------------------------------------------------------------- weekStart

    public static LocalDateTime weekStart() {
        return weekStart(now());
    }

    public static LocalDateTime weekStart(LocalDateTime dateTime) {
        TemporalField week = WeekFields.of(Locale.CHINA).dayOfWeek();
        // 第一天: MONDAY==2
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault()).with(week, 2);
        return dayStart(zdt.toLocalDateTime());
    }

    // ------------------------------------------------------------------------- weekEnd

    public static LocalDateTime weekEnd() {
        return weekEnd(now());
    }

    public static LocalDateTime weekEnd(LocalDateTime dateTime) {
        TemporalField week = WeekFields.of(Locale.CHINA).dayOfWeek();
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault()).with(week, 7);
        return dayEnd(zdt.toLocalDateTime().plusDays(1));
    }

    // ------------------------------------------------------------------------- monthStart

    public static LocalDateTime monthStart() {
        return monthStart(now());
    }

    public static LocalDateTime monthStart(LocalDateTime dateTime) {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfMonth());
        return dayStart(zdt.toLocalDateTime());
    }

    // ------------------------------------------------------------------------- monthEnd

    public static LocalDateTime monthEnd() {
        return monthEnd(now());
    }

    public static LocalDateTime monthEnd(LocalDateTime dateTime) {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfMonth());
        return dayEnd(zdt.toLocalDateTime());
    }

    // ------------------------------------------------------------------------- yearStart

    public static LocalDateTime yearStart() {
        return yearStart(now());
    }

    public static LocalDateTime yearStart(LocalDateTime dateTime) {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.firstDayOfYear());
        return dayStart(zdt.toLocalDateTime());
    }

    // ------------------------------------------------------------------------- yearEnd

    public static LocalDateTime yearEnd() {
        return yearEnd(now());
    }

    public static LocalDateTime yearEnd(LocalDateTime dateTime) {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault()).with(TemporalAdjusters.lastDayOfYear());
        return dayEnd(zdt.toLocalDateTime());
    }
}

/*
 * Copyright © 2022 the original author or authors (photowey@gmail.com)
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

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.photowey.component.common.date.DateUtils.*;

/**
 * {@code DateUtilsTest}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
@Slf4j
class DateUtilsTest {

 @Test
 void testFormat() {
  DateTimeFormatter formatter = formatter();
  LocalDateTime target = LocalDateTime.from(formatter.parse("2022-01-07 15:30:00"));

  LocalDateTime dayStart = dayStart(target);
  LocalDateTime dayEnd = dayEnd(target);

  LocalDateTime weekStart = weekStart(target);
  LocalDateTime weekEnd = weekEnd(target);

  LocalDateTime monthStart = monthStart(target);
  LocalDateTime monthEnd = monthEnd(target);

  LocalDateTime yearStart = yearStart(target);
  LocalDateTime yearEnd = yearEnd(target);

  Assertions.assertEquals("2022-01-07 00:00:00", formatter.format(dayStart));
  Assertions.assertEquals("2022-01-07 23:59:59", formatter.format(dayEnd));

  Assertions.assertEquals("2022-01-03 00:00:00", formatter.format(weekStart));
  Assertions.assertEquals("2022-01-09 23:59:59", formatter.format(weekEnd));

  Assertions.assertEquals("2022-01-01 00:00:00", formatter.format(monthStart));
  Assertions.assertEquals("2022-01-31 23:59:59", formatter.format(monthEnd));

  Assertions.assertEquals("2022-01-01 00:00:00", formatter.format(yearStart));
  Assertions.assertEquals("2022-12-31 23:59:59", formatter.format(yearEnd));
 }

 @Test
 void testTimestamp() throws ParseException {
  String now = "2022-07-29 16:45:00";

  DateTimeFormatter formatter = formatter();
  SimpleDateFormat dateFormatter = new SimpleDateFormat(DatePatternConstants.yyyy_MM_dd_HH_mm_ss);

  Date date = dateFormatter.parse(now);
  LocalDateTime ldt = LocalDateTime.from(formatter.parse(now));

  Long dateTimestamp = toTimestamp(date);
  Long ldtTimestamp = toTimestamp(ldt);

  Assertions.assertEquals(1659084300000L, dateTimestamp);
  Assertions.assertEquals(1659084300000L, ldtTimestamp);
 }

 @Test
 void testParseString() {
  String now = "2022-07-29 16:45:00";

  LocalDateTime localDateTime = toLocalDateTime(now);
  assert localDateTime != null;
  Long ldtTimestamp = toTimestamp(localDateTime);
  Assertions.assertEquals(1659084300000L, ldtTimestamp);

  localDateTime = toLocalDateTime(now, DatePatternConstants.yyyy_MM_dd_HH_mm_ss);
  assert localDateTime != null;
  ldtTimestamp = toTimestamp(localDateTime);
  Assertions.assertEquals(1659084300000L, ldtTimestamp);
 }

 @Test
 void testFormat_T_Z() {
  DateTimeFormatter formatter = formatter();
  LocalDateTime target = LocalDateTime.from(formatter.parse("2022-01-07 15:30:00"));
  String tz = format(target, DatePatternConstants.yyyy_MM_dd_T_HH_mm_ss_Z);
  Assertions.assertEquals("2022-01-07T15:30:00Z", tz);
 }
}

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
package com.photowey.component.common.number;

import com.photowey.component.common.util.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * {@code BigDecimalUtilsTest}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
@Slf4j
class BigDecimalUtilsTest {

    @Test
    void testFormat() {
        BigDecimal target1 = BigDecimalUtils.newBigDecimal("37254185351.53046572");
        BigDecimal target2 = BigDecimalUtils.newBigDecimal("37254185351.53");
        BigDecimal target3 = BigDecimalUtils.newBigDecimal("37254185351.536572");

        BigDecimal target4 = BigDecimalUtils.newBigDecimal("37254185351.536572");
        BigDecimal target5 = BigDecimalUtils.newBigDecimal("88.6");
        BigDecimal target6 = BigDecimalUtils.newBigDecimal("-88.067");

        BigDecimal result1 = BigDecimalUtils.toBigDecimal(target1);
        String result2 = BigDecimalUtils.toStr(target3);
        String result3 = BigDecimalUtils.toPlainString(target3);
        String result4 = BigDecimalUtils.toStr(target3, NumberConstants.PERCENT_WITH_COMMA_4_POINT, RoundingMode.HALF_UP);

        String result5 = BigDecimalUtils.toStr(target4, NumberConstants.FOUR_DECIMAL_POINTS, RoundingMode.HALF_UP);
        String result6 = BigDecimalUtils.toStr(target5, NumberConstants.FOUR_DECIMAL_POINTS, RoundingMode.HALF_UP);
        String result7 = BigDecimalUtils.toStr(target6, NumberConstants.FOUR_DECIMAL_POINTS, RoundingMode.HALF_UP);

        String result8 = BigDecimalUtils.toStr(target5, NumberConstants.TWO_DECIMAL_POINTS, RoundingMode.HALF_UP);
        String result9 = BigDecimalUtils.toStr(target6, NumberConstants.TWO_DECIMAL_POINTS, RoundingMode.HALF_UP);

        boolean compareEquals = ObjectUtils.compareEq(target2, result1);

        Assertions.assertTrue(compareEquals);
        Assertions.assertEquals("37254185351.54", result2);
        Assertions.assertEquals("37254185351.536572", result3);

        Assertions.assertEquals("37,254,185,351.5366", result4);

        Assertions.assertEquals("37254185351.5366", result5);
        Assertions.assertEquals("88.6000", result6);
        Assertions.assertEquals("-88.0670", result7);

        Assertions.assertEquals("88.60", result8);
        Assertions.assertEquals("-88.07", result9);

    }

    @Test
    void testScaleAndFormat() {
        BigDecimal target1 = new BigDecimal("37254185351.53046572").setScale(2, RoundingMode.HALF_UP);
        BigDecimal target2 = BigDecimalUtils.toBigDecimal(new BigDecimal("37254185351.53046572"));

        boolean compareEquals = ObjectUtils.compareEq(target1, target2);

        Assertions.assertTrue(compareEquals);

    }

    @Test
    void testToYuan() {
        BigDecimal fen = new BigDecimal("1000.1234");
        BigDecimal yuan = BigDecimalUtils.toYuan(fen);

        Assertions.assertEquals(new BigDecimal("10.00"), yuan);

        fen = new BigDecimal("900.1254");
        yuan = BigDecimalUtils.toYuan(fen);
        Assertions.assertEquals(new BigDecimal("9.00"), yuan);

        fen = new BigDecimal("900.5254");
        yuan = BigDecimalUtils.toYuan(fen);
        Assertions.assertEquals(new BigDecimal("9.01"), yuan);
    }

    @Test
    void testToCent() {
        BigDecimal yuan = new BigDecimal("1000.1234");
        BigDecimal fen = BigDecimalUtils.toFen(yuan);

        Assertions.assertEquals(new BigDecimal("100012.00"), fen);

        yuan = new BigDecimal("1000.123451");
        fen = BigDecimalUtils.toFen(yuan);

        Assertions.assertEquals(new BigDecimal("100012.00"), fen);
    }
}

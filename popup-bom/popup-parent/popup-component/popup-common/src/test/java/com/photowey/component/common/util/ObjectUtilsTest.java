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
package com.photowey.component.common.util;

import com.photowey.component.common.number.BigDecimalUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * {@code ObjectUtilsTest}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
@Slf4j
class ObjectUtilsTest {

    @Test
    void testDefaultIfNull() {
        String targetNull = null;
        String targetNotNull = "I'm WEI changjun";

        String result1 = ObjectUtils.defaultIfNull(targetNull, "targetNull");
        String result2 = ObjectUtils.defaultIfNull(targetNotNull, "targetNotNull");

        Assertions.assertEquals("targetNull", result1);
        Assertions.assertEquals("I'm WEI changjun", result2);
    }

    @Test
    void testIsNullOrEmpty() {
        String targetNull = null;
        String targetNotNull = "I'm WEI changjun";

        boolean notNull_true = ObjectUtils.isNullOrEmpty(targetNull);
        boolean notNull_false = ObjectUtils.isNullOrEmpty(targetNotNull);
        Assertions.assertTrue(notNull_true);
        Assertions.assertFalse(notNull_false);
    }

    @Test
    void testIsNotNullOrEmpty() {
        String targetNull = null;
        String targetNotNull = "I'm WEI changjun";

        boolean notNull_false = ObjectUtils.isNotNullOrEmpty(targetNull);
        boolean notNull_true = ObjectUtils.isNotNullOrEmpty(targetNotNull);
        Assertions.assertFalse(notNull_false);
        Assertions.assertTrue(notNull_true);
    }

    @Test
    void testEquals() {
        BigDecimal target1 = BigDecimalUtils.newBigDecimal("8848");
        BigDecimal target2 = BigDecimalUtils.newBigDecimal("8848.00");
        boolean equals1 = ObjectUtils.equals(target1, target1);
        boolean equals2 = ObjectUtils.notEqual(target1, target2);

        Assertions.assertTrue(equals1);
        Assertions.assertTrue(equals2);
    }

    @Test
    void testCollectionContains() {
        List<Long> longs = Arrays.asList(0L, 1L, 2L, 3L);
        Long target_0 = 0L;
        Long target_4 = 4L;

        boolean contains_true = ObjectUtils.contains(longs, target_0);
        boolean contains_false = ObjectUtils.contains(longs, target_4);

        Assertions.assertTrue(contains_true);
        Assertions.assertFalse(contains_false);
    }

    @Test
    void testCollectionNotContains() {
        List<Long> longs = Arrays.asList(0L, 1L, 2L, 3L);
        Long target_0 = 0L;
        Long target_4 = 4L;

        boolean contains_true = ObjectUtils.notContains(longs, target_4);
        boolean contains_false = ObjectUtils.notContains(longs, target_0);

        Assertions.assertTrue(contains_true);
        Assertions.assertFalse(contains_false);
    }

    @Test
    void testArrayContains() {
        Long[] longs = new Long[]{0L, 1L, 2L, 3L};
        Long target_0 = 0L;
        Long target_4 = 4L;

        boolean contains_true = ObjectUtils.contains(longs, target_0);
        boolean contains_false = ObjectUtils.contains(longs, target_4);

        Assertions.assertTrue(contains_true);
        Assertions.assertFalse(contains_false);
    }

    @Test
    void testArrayNotContains() {
        Long[] longs = new Long[]{0L, 1L, 2L, 3L};
        Long target_0 = 0L;
        Long target_4 = 4L;

        boolean contains_true = ObjectUtils.notContains(longs, target_4);
        boolean contains_false = ObjectUtils.notContains(longs, target_0);

        Assertions.assertTrue(contains_true);
        Assertions.assertFalse(contains_false);
    }

    @Test
    void testCompareEq() {
        BigDecimal target1 = BigDecimalUtils.newBigDecimal("8848");
        BigDecimal target2 = BigDecimalUtils.newBigDecimal("8848.00");
        BigDecimal target3 = BigDecimalUtils.newBigDecimal("8848.10");
        boolean compareEquals1 = ObjectUtils.compareEq(target1, target2);
        boolean compareEquals2 = ObjectUtils.compareEq(target1, target3);

        Assertions.assertTrue(compareEquals1);
        Assertions.assertFalse(compareEquals2);
    }

    @Test
    void testCompareGt() {
        BigDecimal target1 = BigDecimalUtils.newBigDecimal("8848.00");
        BigDecimal target2 = BigDecimalUtils.newBigDecimal("8848.10");
        BigDecimal target3 = BigDecimalUtils.newBigDecimal("8849.10");

        boolean compareGt_true = ObjectUtils.compareGt(target2, target1);
        boolean compareGt_false = ObjectUtils.compareGt(target2, target3);

        Assertions.assertTrue(compareGt_true);
        Assertions.assertFalse(compareGt_false);
    }

    @Test
    void testCompareGte() {
        BigDecimal target0 = BigDecimalUtils.newBigDecimal("8849.10");
        BigDecimal target1 = BigDecimalUtils.newBigDecimal("8848.10");
        BigDecimal target2 = BigDecimalUtils.newBigDecimal("8847.10");
        BigDecimal target3 = BigDecimalUtils.newBigDecimal("8849.10");

        boolean compareGte_true = ObjectUtils.compareGte(target1, target2);
        boolean compareGte_false = ObjectUtils.compareGte(target1, target3);
        boolean compareGte_false_lt = ObjectUtils.compareGte(target1, target0);

        Assertions.assertTrue(compareGte_true);
        Assertions.assertFalse(compareGte_false);
        Assertions.assertFalse(compareGte_false_lt);
    }

    @Test
    void testCompareLt() {
        BigDecimal target0 = BigDecimalUtils.newBigDecimal("8848.10");
        BigDecimal target1 = BigDecimalUtils.newBigDecimal("8848.10");
        BigDecimal target2 = BigDecimalUtils.newBigDecimal("8849.10");
        BigDecimal target3 = BigDecimalUtils.newBigDecimal("8847.10");

        boolean compareLt_true = ObjectUtils.compareLt(target1, target2);
        boolean compareLt_false = ObjectUtils.compareLt(target1, target3);
        boolean compareLt_false_eq = ObjectUtils.compareLt(target1, target0);

        Assertions.assertTrue(compareLt_true);
        Assertions.assertFalse(compareLt_false);
        Assertions.assertFalse(compareLt_false_eq);
    }

    @Test
    void testCompareLte() {
        BigDecimal target0 = BigDecimalUtils.newBigDecimal("8847.10");
        BigDecimal target1 = BigDecimalUtils.newBigDecimal("8848.10");
        BigDecimal target2 = BigDecimalUtils.newBigDecimal("8848.10");
        BigDecimal target3 = BigDecimalUtils.newBigDecimal("8847.10");

        boolean compareLte_true_lt = ObjectUtils.compareLte(target0, target2);
        boolean compareLte_true_lte = ObjectUtils.compareLte(target1, target2);
        boolean compareGte_false = ObjectUtils.compareLte(target1, target3);

        Assertions.assertTrue(compareLte_true_lt);
        Assertions.assertTrue(compareLte_true_lte);
        Assertions.assertFalse(compareGte_false);
    }

    @Test
    void testNotNull() {
        String targetNull = null;
        String targetNotNull = "I'm WEI changjun";

        Assertions.assertThrows(NullPointerException.class, () -> {
            ObjectUtils.notNull(targetNull);
        });

        ObjectUtils.notNull(targetNotNull);
    }

}

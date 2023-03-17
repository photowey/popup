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

import com.photowey.component.common.thrower.AssertionErrorThrower;
import com.photowey.component.common.util.ObjectUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * {@code BigDecimalUtils}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public final class BigDecimalUtils {

    public static final String ONE_STRING = "1";
    public static final String HUNDRED_STRING = "100";

    private BigDecimalUtils() {
        // utility class; can't create
        AssertionErrorThrower.throwz(BigDecimalUtils.class);
    }

    // ------------------------------------------------------------------------- newBigDecimal

    public static BigDecimal newBigDecimal(Long target) {
        if (null == target) {
            return null;
        }
        return newBigDecimal(String.valueOf(target));
    }

    public static BigDecimal newBigDecimal(Integer target) {
        if (null == target) {
            return null;
        }
        return newBigDecimal(String.valueOf(target));
    }

    public static BigDecimal newBigDecimal(String target) {
        if (null == target) {
            return null;
        }
        return new BigDecimal(target);
    }

    // ------------------------------------------------------------------------- toBigDecimal

    public static BigDecimal toBigDecimal(BigDecimal target, String decimalPoints, RoundingMode roundingMode) {
        if (null == target) {
            return null;
        }
        return new BigDecimal(toStr(target, decimalPoints, roundingMode));
    }

    public static BigDecimal toBigDecimal(BigDecimal target) {
        if (null == target) {
            return null;
        }
        return toBigDecimal(target, NumberPatternConstants.TWO_DECIMAL_POINTS, RoundingMode.HALF_UP);
    }

    // ------------------------------------------------------------------------- divide

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return divide(dividend, divisor, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor, int scale, RoundingMode roundingMod) {
        if (ObjectUtils.isNullOrEmpty(dividend)) {
            nullPointerException(dividend, "the dividend can't be null");
        }
        if (ObjectUtils.isNullOrEmpty(divisor)) {
            nullPointerException(divisor, "the divisor can't be null");
        }

        return dividend.divide(divisor, scale, roundingMod);
    }

    public static BigDecimal divide(String dividend, String divisor) {
        return divide(dividend, divisor, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(String dividend, String divisor, int scale, RoundingMode roundingMod) {
        return divide(newBigDecimal(dividend), newBigDecimal(divisor), scale, roundingMod);
    }

    public static BigDecimal divide(Integer dividend, Integer divisor) {
        return divide(dividend, divisor, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(Integer dividend, Integer divisor, int scale, RoundingMode roundingMod) {
        return divide(newBigDecimal(dividend), newBigDecimal(divisor), scale, roundingMod);
    }

    public static BigDecimal divide(Long dividend, Long divisor) {
        return divide(dividend, divisor, 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(Long dividend, Long divisor, int scale, RoundingMode roundingMod) {
        return divide(newBigDecimal(dividend), newBigDecimal(divisor), scale, roundingMod);
    }

    public static String toStr(BigDecimal target, String decimalPoints, RoundingMode roundingMode) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return "";
        }
        return NumberFormatUtils.format(target, decimalPoints, roundingMode);
    }

    public static String toPlainString(BigDecimal target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return "";
        }
        return target.toPlainString();
    }

    public static String toStr(BigDecimal target) {
        // #0.00
        return toStr(target, NumberPatternConstants.TWO_DECIMAL_POINTS, RoundingMode.HALF_UP);
    }

    // ------------------------------------------------------------------------- format

    public static String toThousand(BigDecimal target) {
        if (ObjectUtils.isNullOrEmpty(target)) {
            return "";
        }

        return toStr(target, NumberPatternConstants.PERCENT_WITH_COMMA_2_POINT, RoundingMode.HALF_UP);
    }

    public static BigDecimal toTenThousand(BigDecimal target) {
        if (ObjectUtils.isNullOrEmpty(target) || ObjectUtils.compareEq(BigDecimal.ZERO, target)) {
            return target;
        }

        return target.divide(newBigDecimal(NumberPatternConstants.TEN_THOUSAND), 2, RoundingMode.HALF_UP);
    }

    public static BigDecimal formatRatio(BigDecimal divisor, BigDecimal dividend) {
        if (ObjectUtils.isNullOrEmpty(divisor)) {
            return newBigDecimal("0.0000");
        }
        if (ObjectUtils.isNullOrEmpty(dividend) || ObjectUtils.compareEq(BigDecimal.ZERO, divisor)) {
            dividend = newBigDecimal(ONE_STRING);
        }
        BigDecimal winRatio = divisor.divide(dividend, 4, RoundingMode.HALF_UP);

        return winRatio;
    }

    // ------------------------------------------------------------------------- CNY transfer

    public static BigDecimal toCent(BigDecimal yuan) {
        if (yuan == null) {
            return null;
        }

        return toBigDecimal(yuan.multiply(new BigDecimal(HUNDRED_STRING)));
    }

    public static BigDecimal toYuan(BigDecimal cent) {
        if (cent == null) {
            return null;
        }

        return divide(cent, new BigDecimal(HUNDRED_STRING));
    }

    // ------------------------------------------------------------------------- exception

    public static <T> void nullPointerException(T candidate, String message) {
        if (ObjectUtils.isNullOrEmpty(candidate)) {
            throw new NullPointerException(message);
        }
    }
}
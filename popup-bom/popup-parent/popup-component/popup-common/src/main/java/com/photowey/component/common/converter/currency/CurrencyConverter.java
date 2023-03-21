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
package com.photowey.component.common.converter.currency;

import com.photowey.component.common.number.BigDecimalUtils;

import java.math.BigDecimal;

/**
 * {@code CurrencyConverter}
 *
 * @author photowey
 * @date 2023/03/21
 * @since 1.0.0
 */
public interface CurrencyConverter {

    static BigDecimal toFen(BigDecimal cnyYuan) {
        return BigDecimalUtils.toFen(cnyYuan);
    }

    static Long toIntFen(BigDecimal cnyYuan) {
        if (cnyYuan == null) {
            return null;
        }

        return BigDecimalUtils.toFen(cnyYuan).longValue();
    }

    static BigDecimal toYuan(BigDecimal cnyFen) {
        return BigDecimalUtils.toYuan(cnyFen);
    }

    static BigDecimal toYuan(Long cnyIntFen) {
        return BigDecimalUtils.toYuan(BigDecimalUtils.newBigDecimal(cnyIntFen));
    }
}

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
package com.photowey.component.common.counter;

import java.math.BigDecimal;
import java.util.function.Supplier;

/**
 * {@code Counter}
 *
 * @author weichangjun
 * @date 2023/12/29
 * @since 1.0.0
 */
public interface Counter {

    default Long determineCounter(Supplier<Long> fx) {
        Long counter = fx.get();

        return null != counter ? counter : 0L;
    }

    default Integer determineIntegerCounter(Supplier<Integer> fx) {
        Integer counter = fx.get();

        return null != counter ? counter : 0;
    }

    default BigDecimal determineBigDecimalCounter(Supplier<BigDecimal> fx) {
        BigDecimal counter = fx.get();

        return null != counter ? counter : BigDecimal.ZERO;
    }

}
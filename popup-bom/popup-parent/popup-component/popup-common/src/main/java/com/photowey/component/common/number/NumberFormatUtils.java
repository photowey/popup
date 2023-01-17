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

import org.apache.commons.lang3.Validate;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * {@code NumberFormatUtils}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public final class NumberFormatUtils {

    private NumberFormatUtils() {
        // utility class; can't create
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public static String format(Number nv, String pattern, RoundingMode rm) {
        Validate.notNull(nv, "nv can't be null!");
        Validate.notBlank(pattern, "pattern can't be null!");
        DecimalFormat formatter = new DecimalFormat(pattern);
        formatter.setRoundingMode(org.apache.commons.lang3.ObjectUtils.defaultIfNull(rm, RoundingMode.HALF_UP));

        return formatter.format(nv);
    }
}
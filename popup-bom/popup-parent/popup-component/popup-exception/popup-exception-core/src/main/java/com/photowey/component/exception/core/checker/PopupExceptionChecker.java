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
package com.photowey.component.exception.core.checker;

import com.photowey.component.common.util.ObjectUtils;
import com.photowey.component.exception.core.model.PopupException;

import java.math.BigDecimal;

/**
 * {@code PopupExceptionChecker}
 *
 * @author photowey
 * @date 2023/03/02
 * @since 1.0.0
 */
public abstract class PopupExceptionChecker implements ExceptionChecker {

    public static <T> void checkNotNull(T v, String message, Object... args) {
        if (ObjectUtils.isNullOrEmpty(v)) {
            throwException(message, args);
        }
    }

    public static <T> void checkNull(T v, String message, Object... args) {
        if (ObjectUtils.isNotNullOrEmpty(v)) {
            throwException(message, args);
        }
    }

    public static void checkTrue(boolean expression, String message, Object... args) {
        if (!expression) {
            throwException(message, args);
        }
    }

    public static void checkFalse(boolean expression, String message, Object... args) {
        checkTrue(!expression, message, args);
    }

    public static void checkExistB(boolean expression, String message, Object... args) {
        checkTrue(expression, message, args);
    }

    public static <T> void checkExist(T t, String message, Object... args) {
        checkExistB(ObjectUtils.isNotNullOrEmpty(t), message, args);
    }

    public static void checkNotExistB(boolean expression, String message, Object... args) {
        checkExistB(!expression, message, args);
    }

    public static <T> void checkNotExist(T t, String message, Object... args) {
        checkNotExistB(ObjectUtils.isNotNullOrEmpty(t), message, args);
    }

    public static void checkEq(Number left, Number right, String message, Object... args) {
        BigDecimal t1 = new BigDecimal(String.valueOf(left.doubleValue()));
        BigDecimal t2 = new BigDecimal(String.valueOf(right.doubleValue()));

        if (t1.compareTo(t2) != 0) {
            throwException(message, args);
        }
    }

    public static void checkNotEq(Number left, Number right, String message, Object... args) {
        BigDecimal t1 = new BigDecimal(String.valueOf(left.doubleValue()));
        BigDecimal t2 = new BigDecimal(String.valueOf(right.doubleValue()));

        if (t1.compareTo(t2) == 0) {
            throwException(message, args);
        }
    }

    public static void throwException(String message, Object... args) {
        throw new PopupException(message, args);
    }
}

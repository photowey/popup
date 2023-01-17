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
package com.photowey.component.common.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code ObjectUtils}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public final class ObjectUtils {

    private ObjectUtils() {
        // utility class; can't create
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public static <T> T defaultIfNull(T target, T defaultValue) {
        return defaultIfNullOrEmpty(target, defaultValue);
    }

    public static <T> T defaultIfNullOrEmpty(final T target, final T defaultValue) {
        return isNotNullOrEmpty(target) ? target : defaultValue;
    }

    // -------------------------------------------------------------------------

    /**
     * {@code isNullOrEmpty}
     *
     * @param target the target Object.
     * @return {@code boolean}
     */
    public static boolean isNullOrEmpty(Object target) {
        if (null == target) {
            return true;
        }

        if (target instanceof CharSequence) {
            return StringUtils.isBlank((CharSequence) target);
        }

        if (isCollectionsSupportedType(target)) {
            return CollectionUtils.sizeIsEmpty(target);
        }

        return false;
    }

    /**
     * {@code isNotNullOrEmpty}
     *
     * @param target the target Object.
     * @return {@code boolean}
     */
    public static boolean isNotNullOrEmpty(Object target) {
        return !isNullOrEmpty(target);
    }

    // -------------------------------------------------------------------------

    /**
     * Is supported type
     *
     * @param target the target Object.
     * @return {@code boolean}
     */
    private static boolean isCollectionsSupportedType(Object target) {
        boolean isCollectionOrMap = target instanceof Collection || target instanceof Map;
        boolean isEnumerationOrIterator = target instanceof Enumeration || target instanceof Iterator;

        return isCollectionOrMap
                || isEnumerationOrIterator
                || target.getClass().isArray();
    }

    // -------------------------------------------------------------------------

    public static <T> void callbackEmpty(T target, Consumer<T> fx) {
        if (isNullOrEmpty(target)) {
            fx.accept(target);
        }
    }

    public static <T> void callbackNotEmpty(T target, Consumer<T> fx) {
        if (isNotNullOrEmpty(target)) {
            fx.accept(target);
        }
    }

    // -------------------------------------------------------------------------

    /**
     * ==
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return {@code boolean}
     */
    public static <T> boolean equals(final T t1, final T t2) {
        return Objects.equals(t1, t2);
    }

    /**
     * !=
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return {@code boolean}
     */
    public static <T> boolean notEqual(final T t1, final T t2) {
        return !equals(t1, t2);
    }

    // -------------------------------------------------------------------------

    /**
     * Collection {@code contains}
     *
     * @param c1  Collection
     * @param t2  target
     * @param <T> T type
     * @return {@code boolean}
     */
    public static <T> boolean contains(final Collection<T> c1, final T t2) {
        return c1.contains(t2);
    }

    public static <T> boolean notContains(final Collection<T> c1, final T t2) {
        return !contains(c1, t2);
    }

    /**
     * Array {@code contains}
     *
     * @param c1  array
     * @param t2  target
     * @param <T> T type
     * @return {@code boolean}
     */
    public static <T> boolean contains(final T[] c1, final T t2) {
        return contains(Stream.of(c1).collect(Collectors.toSet()), t2);
    }

    public static <T> boolean notContains(final T[] c1, final T t2) {
        return !contains(c1, t2);
    }

    // -------------------------------------------------------------------------

    /**
     * {@code compareTo}
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return
     */
    public static <T extends Number> int compareTo(final T t1, final T t2) {
        checkNull(t1, t2);
        BigDecimal t11 = t1 instanceof BigDecimal ? (BigDecimal) t1 : new BigDecimal(t1.toString());
        BigDecimal t21 = t1 instanceof BigDecimal ? (BigDecimal) t2 : new BigDecimal(t2.toString());

        return t11.compareTo(t21);
    }

    /**
     * ==
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return {@code boolean}
     */
    public static <T extends Number> boolean compareEq(final T t1, final T t2) {
        return compareTo(t1, t2) == 0;
    }

    /**
     * >
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return {@code boolean}
     */
    public static <T extends Number> boolean compareGt(final T t1, final T t2) {
        return compareTo(t1, t2) > 0;
    }

    /**
     * >=
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return {@code boolean}
     */
    public static <T extends Number> boolean compareGte(final T t1, final T t2) {
        return compareTo(t1, t2) >= 0;
    }

    /**
     * <
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return {@code boolean}
     */
    public static <T extends Number> boolean compareLt(final T t1, final T t2) {
        return compareTo(t1, t2) < 0;
    }

    /**
     * <=
     *
     * @param t1
     * @param t2
     * @param <T>
     * @return {@code boolean}
     */
    public static <T extends Number> boolean compareLte(final T t1, final T t2) {
        return compareTo(t1, t2) <= 0;
    }

    // -------------------------------------------------------------------------

    public static void notNull(final Object target) {
        notNull(target, "argument invalid, Please check");
    }

    public static void notNull(final Object target, String message) {
        if (target == null) {
            throw new NullPointerException(message);
        }
    }

    // -------------------------------------------------------------------------

    private static <T extends Number> void checkNull(T t1, T t2) {
        notNull(t1, "t1 can't be null");
        notNull(t2, "t2 can't be null");
    }

}
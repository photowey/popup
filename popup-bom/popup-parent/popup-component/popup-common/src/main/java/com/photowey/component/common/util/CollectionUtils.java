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

import com.photowey.component.common.thrower.AssertionErrorThrower;

import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code CollectionUtils}
 *
 * @author photowey
 * @date 2023/06/10
 * @since 1.0.0
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // utility class; can't create
        AssertionErrorThrower.throwz(CollectionUtils.class);
    }

    public static <T> List<T> asList(T... ts) {
        return new ArrayList<>(Arrays.asList(ts));
    }

    public static <T> Set<T> asSet(T... ts) {
        return new HashSet<>(Arrays.asList(ts));
    }

    public static <T> List<T> newArrayList() {
        return new ArrayList<>();
    }

    public static <T> Set<T> newHashSet() {
        return new HashSet<>();
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return org.apache.commons.collections4.CollectionUtils.isEmpty(collection);
    }

    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return org.apache.commons.collections4.CollectionUtils.isNotEmpty(collection);
    }

    public static <T> List<T> union(Collection<T> c1, Collection<T> c2) {
        List<T> c11 = new ArrayList<>(c1);
        List<T> c21 = new ArrayList<>(c2);
        c11.addAll(c21);

        return distinct(c11);
    }

    public static <T> List<T> unionAll(Collection<T> c1, Collection<T> c2) {
        List<T> c11 = new ArrayList<>(c1);
        List<T> c21 = new ArrayList<>(c2);
        c11.addAll(c21);

        return c11;
    }

    public static <T> List<T> intersection(Collection<T> c1, Collection<T> c2) {
        List<T> c11 = new ArrayList<>(c1);
        c11.retainAll(c2);

        return c11;
    }

    public static <T> List<T> reduce21(Collection<T> c1, Collection<T> c2) {
        List<T> c21 = new ArrayList<>(c2);
        c21.removeAll(c1);

        return c21;
    }

    public static <T> List<T> reduce12(Collection<T> c1, Collection<T> c2) {
        List<T> c11 = new ArrayList<>(c1);
        c11.removeAll(c2);

        return c11;
    }

    public static <T> List<T> distinct(Collection<T> c1) {
        return c1.stream().distinct().collect(Collectors.toList());
    }

    private static int calculateInitialCapacityFromExpectedSize(int expectedSize) {
        return expectedSize < 3 ? expectedSize + 1 : (int) ((float) expectedSize / 0.75F + 1.0F);
    }
}

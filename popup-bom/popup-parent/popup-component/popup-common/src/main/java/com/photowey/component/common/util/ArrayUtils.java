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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * {@code ArrayUtils}
 *
 * @author photowey
 * @date 2023/05/23
 * @since 1.0.0
 */
public final class ArrayUtils {

    private ArrayUtils() {
        // utility class; can't create
        AssertionErrorThrower.throwz(ArrayUtils.class);
    }

    // -----------------------------------------------------------------

    public static String[] toArray(Collection<String> src) {
        if (ObjectUtils.isNullOrEmpty(src)) {
            return new String[0];
        }
        if (src instanceof List) {
            return src.toArray(new String[0]);
        }

        return new ArrayList<>(src).toArray(new String[0]);
    }

    public static Long[] toLongArray(Collection<Long> src) {
        if (ObjectUtils.isNullOrEmpty(src)) {
            return new Long[0];
        }
        if (src instanceof List) {
            return src.toArray(new Long[0]);
        }

        return new ArrayList<>(src).toArray(new Long[0]);
    }

    public static Integer[] toIntArray(Collection<Integer> src) {
        if (ObjectUtils.isNullOrEmpty(src)) {
            return new Integer[0];
        }
        if (src instanceof List) {
            return src.toArray(new Integer[0]);
        }

        return new ArrayList<>(src).toArray(new Integer[0]);
    }

    public static <T> CompletableFuture<T>[] toCompletableFutureArray(Collection<CompletableFuture<T>> src) {
        if (ObjectUtils.isNullOrEmpty(src)) {
            return new CompletableFuture[0];
        }
        if (src instanceof List) {
            return src.toArray(new CompletableFuture[0]);
        }

        return new ArrayList<>(src).toArray(new CompletableFuture[0]);
    }
}

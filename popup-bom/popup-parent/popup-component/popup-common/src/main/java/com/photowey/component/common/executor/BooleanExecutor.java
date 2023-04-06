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
package com.photowey.component.common.executor;

import com.photowey.component.common.util.ObjectUtils;

import java.util.function.Consumer;

/**
 * {@code BooleanExecutor}
 *
 * @author photowey
 * @date 2023/04/06
 * @since 1.0.0
 */
public interface BooleanExecutor {

    // ----------------------------------------------------------------

    static void executeTrue(boolean expression, ValueExecutor fx) {
        if (expression) {
            fx.accept();
        }
    }

    static void executeFalse(boolean expression, ValueExecutor fx) {
        if (!expression) {
            fx.accept();
        }
    }

    // ----------------------------------------------------------------

    static <T> void executeEmpty(T t, ValueExecutor fx) {
        executeTrue(ObjectUtils.isNullOrEmpty(t), fx);
    }

    static <T> void executeNotEmpty(T t, ValueExecutor fx) {
        executeTrue(ObjectUtils.isNotNullOrEmpty(t), fx);
    }

    // ----------------------------------------------------------------

    static <T> void executeEmpty(T t, Consumer<T> fx) {
        if (ObjectUtils.isNullOrEmpty(t)) {
            fx.accept(t);
        }
    }

    static <T> void executeNotEmpty(T t, Consumer<T> fx) {
        if (ObjectUtils.isNotNullOrEmpty(t)) {
            fx.accept(t);
        }
    }
}

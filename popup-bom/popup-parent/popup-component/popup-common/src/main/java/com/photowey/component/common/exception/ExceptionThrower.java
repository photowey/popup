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
package com.photowey.component.common.exception;

/**
 * {@code ExceptionThrower}
 *
 * @author photowey
 * @date 2023/01/06
 * @since 1.0.0
 */
public abstract class ExceptionThrower {

    public static RuntimeException throwUnchecked(Throwable t) {
        if (null == t) {
            throw new NullPointerException("Throwable must not be null");
        }
        throwAs(t);

        throw new AssertionError("Unreached here.");
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> void throwAs(Throwable t) throws T {
        throw (T) t;
    }
}

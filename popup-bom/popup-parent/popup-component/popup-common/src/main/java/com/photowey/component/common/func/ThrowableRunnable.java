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
package com.photowey.component.common.func;

import static com.photowey.component.common.func.ThrowableRunnableThrower.sneakyThrow;

/**
 * {@code ThrowableRunnable}
 *
 * @author photowey
 * @date 2023/03/01
 * @since 1.0.0
 */
@FunctionalInterface
public interface ThrowableRunnable {

    static ThrowableRunnable of(ThrowableRunnable ref) {
        return ref;
    }

    void run() throws Throwable;

    default Runnable unchecked() {
        return () -> {
            try {
                run();
            } catch (Throwable x) {
                sneakyThrow(x);
            }
        };
    }
}

interface ThrowableRunnableThrower {

    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }

}

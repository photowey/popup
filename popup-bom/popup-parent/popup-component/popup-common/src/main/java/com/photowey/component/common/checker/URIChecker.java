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
package com.photowey.component.common.checker;

import com.photowey.component.common.thrower.AssertionErrorThrower;

import java.net.URI;
import java.util.function.Consumer;

/**
 * {@code URIChecker}
 *
 * @author photowey
 * @date 2023/05/23
 * @since 1.0.0
 */
public final class URIChecker {

    private URIChecker() {
        // utility class; can't create
        AssertionErrorThrower.throwz(URIChecker.class);
    }

    public static boolean checkUri(String uri) {
        return checkUri(uri, (x) -> {
        });
    }

    public static boolean checkUri(String uri, Consumer<String> fx) {
        try {
            new URI(uri).toURL();
        } catch (Exception ignored) {
            fx.accept(uri);
            return false;
        }

        return true;
    }

}

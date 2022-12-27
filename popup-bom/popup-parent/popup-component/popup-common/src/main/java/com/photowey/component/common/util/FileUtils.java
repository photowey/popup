/*
 * Copyright © 2022 the original author or authors (photowey@gmail.com)
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

import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;

/**
 * {@code FileUtils}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public final class FileUtils {

    private FileUtils() {
        // utility class; can't create
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public static void write(final String file, final String data) {
        write(file, data.getBytes(StandardCharsets.UTF_8));
    }

    public static void write(final String file, final String data, boolean quiet) {
        write(file, quiet, data.getBytes(StandardCharsets.UTF_8));
    }

    public static void write(final String file, final byte[] data) {
        write(file, false, data);
    }

    public static void write(final String file, boolean quiet, final byte[] data) {
        IOUtils.write(file, quiet, data);
    }

    public static String yaml(final String target) {
        return read(target, each -> !each.startsWith("#"));
    }

    public static String read(final String target, Predicate<String> filter) {
        return read(target, false, filter);
    }

    public static String read(final String target, boolean quiet, Predicate<String> filter) {
        return IOUtils.read(target, quiet, filter);
    }
}

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

import com.photowey.component.common.constant.PopupConstants;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw");
             FileChannel channel = raf.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(data.length);
            buffer.put(data);
            buffer.flip();
            while (buffer.hasRemaining()) {
                channel.write(buffer);
            }
            channel.force(true);
        } catch (IOException e) {
            if (!quiet) {
                throw new RuntimeException(String.format("write data into file:%s exception", file), e);
            }
        }
    }

    public static String yaml(final String target) {
        return read(target, each -> !each.startsWith("#"));
    }

    public static String read(final String target, Predicate<String> filter) {
        return read(target, false, filter);
    }

    public static String read(final String target, boolean quiet, Predicate<String> filter) {
        try {
            return Files.readAllLines(Paths.get(ClassLoader.getSystemResource(target).toURI()))
                    .stream()
                    .filter(filter)
                    .map(each -> each + System.lineSeparator())
                    .collect(Collectors.joining());
        } catch (Exception e) {
            if (!quiet) {
                throw new RuntimeException(String.format("read from the file:%s exception", target));
            }
        }

        return PopupConstants.EMPTY_STRING;
    }
}

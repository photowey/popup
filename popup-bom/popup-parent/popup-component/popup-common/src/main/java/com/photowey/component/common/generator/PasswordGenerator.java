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
package com.photowey.component.common.generator;

/**
 * {@code PasswordGenerator}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public interface PasswordGenerator extends Generator {

    char[] NANOID_LOWER_ALPHABET = "23456789abcdefghjkmnpqrstuvwxyz".toCharArray();
    String DEFAULT_PASSWORD_PREFIX = "pop";
    int DEFAULT_PASSWORD_RANDOM_SIZE = 6;

    RandomGenerator randomGenerator();

    default String generate() {
        return this.generate(DEFAULT_PASSWORD_PREFIX, NANOID_LOWER_ALPHABET, DEFAULT_PASSWORD_RANDOM_SIZE);
    }

    @Override
    default String generate(String prefix) {
        return this.randomGenerator().generate(prefix);
    }

    @Override
    default String generate(String prefix, int size) {
        return this.randomGenerator().generate(prefix, size);
    }

    @Override
    default String generate(char[] alphabet, int size) {
        return this.randomGenerator().generate(alphabet, size);
    }

    @Override
    default String generate(String prefix, char[] alphabet, int size) {
        return this.randomGenerator().generate(prefix, alphabet, size);
    }
}

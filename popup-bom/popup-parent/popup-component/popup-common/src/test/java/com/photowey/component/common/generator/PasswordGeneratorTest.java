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
package com.photowey.component.common.generator;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code PasswordGeneratorTest}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
@Slf4j
class PasswordGeneratorTest {

    public static final char[] NANOID_LOWER_ALPHABET = "23456789abcdefghjkmnpqrstuvwxyz".toCharArray();
    private PasswordGenerator passwordGenerator;

    @BeforeEach
    public void setUp() {
        this.passwordGenerator = new PopupPasswordGenerator();
    }

    @Test
    void testGenerate() {
        String defaultPassword = this.passwordGenerator.generate();
        Pattern pattern = Pattern.compile("^pop[a-z0-9]{6}$");
        Matcher matcher = pattern.matcher(defaultPassword);
        Assertions.assertTrue(matcher.matches());

        String givenPassword = this.passwordGenerator.generate("pop", 6);
        Matcher matcher2 = pattern.matcher(givenPassword);
        Assertions.assertTrue(matcher2.matches());

        String alphabetPassword = this.passwordGenerator.generate("pop", NANOID_LOWER_ALPHABET, 6);
        Matcher matcher3 = pattern.matcher(alphabetPassword);
        Assertions.assertTrue(matcher3.matches());
    }
}

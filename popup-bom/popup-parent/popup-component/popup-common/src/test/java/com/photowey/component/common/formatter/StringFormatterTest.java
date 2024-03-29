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
package com.photowey.component.common.formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@code StringFormatterTest}
 *
 * @author photowey
 * @date 2023/03/22
 * @since 1.0.0
 */
class StringFormatterTest {

    @Test
    void testFormat() {
        String template_123 = "1 {} 3";
        String template_13 = "1 3";

        String format_123 = StringFormatter.format(template_123, "2");
        String format_13 = StringFormatter.format(template_13, "2");

        Assertions.assertEquals("1 2 3", format_123);
        Assertions.assertEquals("1 3", format_13);
    }

}
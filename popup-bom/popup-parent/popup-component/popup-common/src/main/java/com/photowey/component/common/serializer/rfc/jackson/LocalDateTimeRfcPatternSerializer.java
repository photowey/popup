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
package com.photowey.component.common.serializer.rfc.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.photowey.component.common.date.formatter.rfc.RFC3339DateTimeFormatter;
import com.photowey.component.common.serializer.Cleaner;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * {@code LocalDateTimeRfcPatternSerializer}
 *
 * @author photowey
 * @date 2023/03/15
 * @since 1.0.0
 */
public class LocalDateTimeRfcPatternSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            String formatted = RFC3339DateTimeFormatter.format(Cleaner.trimTail(value));
            gen.writeString(formatted);
        }
    }
}

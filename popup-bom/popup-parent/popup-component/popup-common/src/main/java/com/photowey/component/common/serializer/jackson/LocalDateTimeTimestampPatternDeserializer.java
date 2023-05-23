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
package com.photowey.component.common.serializer.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.photowey.component.common.date.DateUtils;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * {@code LocalDateTimeTimestampPatternFastjsonDeserializer}
 *
 * @author photowey
 * @date 2023/05/23
 * @since 1.0.0
 */
public class LocalDateTimeTimestampPatternDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException {
        long ts = p.getValueAsLong();
        if (ts > 0) {
            return DateUtils.toLocalDateTime(ts);
        } else {
            return null;
        }
    }
}
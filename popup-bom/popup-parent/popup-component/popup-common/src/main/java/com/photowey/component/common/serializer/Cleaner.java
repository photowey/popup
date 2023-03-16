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
package com.photowey.component.common.serializer;

import com.photowey.component.common.date.DateUtils;

import java.time.LocalDateTime;

/**
 * {@code Cleaner}
 *
 * @author photowey
 * @date 2023/03/15
 * @since 1.0.0
 */
public interface Cleaner {

    static LocalDateTime trimTail(LocalDateTime dateTime) {
        Long ts = DateUtils.toTimestamp(dateTime);

        return DateUtils.toLocalDateTime(ts);
    }
}

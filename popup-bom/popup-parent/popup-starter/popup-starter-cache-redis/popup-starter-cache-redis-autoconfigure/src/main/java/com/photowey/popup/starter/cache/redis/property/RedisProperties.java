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
package com.photowey.popup.starter.cache.redis.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * {@code RedisProperties}
 *
 * @author photowey
 * @date 2023/12/24
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "spring.redis", ignoreUnknownFields = true)
public class RedisProperties implements Serializable {

    private static final long serialVersionUID = 412036528519210928L;

    private String host = "localhost";
    private Integer port = 6379;
    private String password;
    private Integer database = 0;
    private Integer timeout = 3000;
    private String serializer = "Generic";

    public boolean determinePasswordIsEnabled() {
        return StringUtils.hasText(this.password);
    }
}
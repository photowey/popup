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
package com.photowey.popup.spring.cloud.gateway.app.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * {@code GatewayProperties}
 *
 * @author photowey
 * @date 2023/02/14
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "popup.gateway")
public class GatewayProperties {

    private Health health = new Health();

    @Data
    public static class Health implements Serializable {

        private static final long serialVersionUID = 5619735114318224437L;
        
        private String healthApi = "http://127.0.0.1:5010/healthz";
    }
}

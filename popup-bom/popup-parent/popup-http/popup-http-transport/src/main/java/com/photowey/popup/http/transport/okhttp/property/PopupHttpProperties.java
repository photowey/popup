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
package com.photowey.popup.http.transport.okhttp.property;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code PopupHttpProperties}
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
@Data
public class PopupHttpProperties implements Serializable {

    private static final long serialVersionUID = 8562723230457736534L;

    private OkHttp okHttp = new OkHttp();

    @Data
    public static class OkHttp implements Serializable {

        private static final long serialVersionUID = 2162838867050344878L;

        private boolean enabled = true;
        private int connectTimeout = 5;
        private int readTimeout = 30;
        private int writeTimeout = 30;
        private int maxIdleConnections = 200;
        private long keepAliveDuration = 300L;
        private List<String> ignoreApis = new ArrayList<>();
    }
}

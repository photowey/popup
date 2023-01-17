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
package com.photowey.popup.http.api.enums;

/**
 * {@code HttpMethod}
 *
 * @author photowey
 * @date 2023/01/04
 * @since 1.0.0
 */
public enum HttpMethod {
    /**
     * HTTP Method
     */
    GET,
    HEAD,
    DELETE,
    CONNECT,
    OPTIONS,
    TRACE,
    POST(true),
    PUT(true),
    PATCH(true),

    ;

    private final boolean withBody;

    HttpMethod() {
        this(false);
    }

    HttpMethod(boolean withBody) {
        this.withBody = withBody;
    }

    public boolean isWithBody() {
        return this.withBody;
    }
}
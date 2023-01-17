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
package com.photowey.popup.http.api.model.header;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * {@code HttpHeaders}
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
public class HttpHeaders implements Serializable {

    private static final long serialVersionUID = -4600134947694353694L;

    private final Map<String, String> headers = new HashMap<>();

    public HttpHeaders() {
    }

    public HttpHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    public static HttpHeaders empty() {
        return new HttpHeaders();
    }

    public void addHeader(String name, String value) {
        this.headers.put(requireNonNull(name), requireNonNull(value));
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>(this.headers);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        this.headers.forEach((k, v) -> {
            buf.append(k).append("=").append(v).append("&");
        });

        String text = buf.toString().replaceAll("&*$", "");

        return text;
    }
}

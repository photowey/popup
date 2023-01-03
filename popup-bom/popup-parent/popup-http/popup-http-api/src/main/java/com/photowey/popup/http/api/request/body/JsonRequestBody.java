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
package com.photowey.popup.http.api.request.body;

import com.photowey.popup.http.api.model.media.MediaType;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

/**
 * {@code JsonRequestBody}
 * JSON
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
public final class JsonRequestBody implements RequestBody {

    private final String body;

    private JsonRequestBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    @Override
    public byte[] getBytes() {
        return body.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getContentType() {
        return MediaType.APPLICATION_JSON.value();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Serializable {

        private static final long serialVersionUID = -6388609927211930379L;

        private String body;

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public JsonRequestBody build() {
            return new JsonRequestBody(requireNonNull(body));
        }
    }
}
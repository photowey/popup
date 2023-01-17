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
package com.photowey.popup.http.api.request.body;

import com.photowey.popup.http.api.model.media.MediaType;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

/**
 * {@code TextRequestBody}
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
public final class TextRequestBody implements RequestBody {

    private final String text;

    private TextRequestBody(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public byte[] getBytes() {
        return text.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getContentType() {
        return MediaType.TEXT_PLAIN.value();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Serializable {

        private static final long serialVersionUID = -5150002091915221771L;

        private String text;

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public TextRequestBody build() {
            return new TextRequestBody(requireNonNull(text));
        }
    }
}

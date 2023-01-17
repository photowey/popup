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
import com.photowey.popup.http.api.model.query.HttpParameters;

import static java.util.Objects.requireNonNull;

/**
 * {@code FormRequestBody}
 * FORM
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
public final class FormRequestBody implements RequestBody {

    private final HttpParameters parameters;

    private FormRequestBody(HttpParameters parameters) {
        this.parameters = parameters;
    }

    public HttpParameters getParameters() {
        return parameters;
    }

    @Override
    public byte[] getBytes() {
        return new byte[0];
    }

    @Override
    public String getContentType() {
        return MediaType.APPLICATION_FORM.value();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private HttpParameters parameters;

        public Builder body(HttpParameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public FormRequestBody build() {
            return new FormRequestBody(requireNonNull(parameters));
        }
    }
}

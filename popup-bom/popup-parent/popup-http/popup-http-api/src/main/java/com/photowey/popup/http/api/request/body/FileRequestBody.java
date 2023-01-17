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
import java.net.URLConnection;

import static java.util.Objects.requireNonNull;

/**
 * {@code FileRequestBody}
 * FILE
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
public final class FileRequestBody implements RequestBody {

    private final String meta;
    private final String fileName;
    private final byte[] bytes;

    private FileRequestBody(String meta, String fileName, byte[] bytes) {
        this.meta = meta;
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public String getMeta() {
        return meta;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public String getContentType() {
        String contentTypeFromName = URLConnection.guessContentTypeFromName(fileName);
        if (contentTypeFromName == null) {
            return MediaType.APPLICATION_OCTET_STREAM.value();
        }

        return contentTypeFromName;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder implements Serializable {

        private static final long serialVersionUID = -7080597772353533412L;

        private String meta;
        private String fileName;
        private byte[] bytes;

        public Builder meta(String meta) {
            this.meta = meta;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder bytes(byte[] bytes) {
            this.bytes = bytes;
            return this;
        }

        public FileRequestBody build() {
            return new FileRequestBody(
                    requireNonNull(meta), requireNonNull(fileName), requireNonNull(bytes));
        }
    }
}

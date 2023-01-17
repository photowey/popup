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
package com.photowey.popup.http.api.client;

import com.photowey.component.common.util.IOUtils;
import com.photowey.component.common.util.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@code Downloader}
 *
 * @author photowey
 * @date 2023/01/04
 * @since 1.0.0
 */
public interface Downloader {

    InputStream download(String url) throws IOException;

    default byte[] downloadBins(String url) throws IOException {
        InputStream input = ObjectUtils.defaultIfNull(this.download(url), this.emptyStream());

        return IOUtils.toBytes(input);
    }

    default InputStream emptyStream() {
        return new ByteArrayInputStream(new byte[0]);
    }

}

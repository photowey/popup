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
package com.photowey.popup.http.transport.okhttp.client;

import com.photowey.component.common.exception.ExceptionThrower;
import com.photowey.popup.http.api.request.HttpRequest;
import com.photowey.popup.http.api.response.HttpResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * {@code OkHttpExecutor}
 *
 * {@code OkHttp} executor of {@link com.photowey.popup.http.api.client.HttpClient}
 *
 * @author photowey
 * @date 2023/01/08
 * @since 1.0.0
 */
public class OkHttpExecutor extends OkHttpExecutorAdaptor {

    private final okhttp3.OkHttpClient okHttpClient;

    public OkHttpExecutor() {
        // TODO
        this.okHttpClient = new OkHttpClient();
    }

    @Override
    public byte[] downloadBins(String url) throws IOException {
        HttpRequest httpRequest = this.buildDownloadRequest(url);
        try (Response okHttpResponse = this.executeDownload(httpRequest)) {
            if (okHttpResponse.body() != null) {
                return Objects.requireNonNull(okHttpResponse.body()).bytes();
            }
        } catch (Exception e) {
            throw ExceptionThrower.throwUnchecked(e);
        }

        // return null

        return new byte[0];
    }

    @Override
    public InputStream doDownload(HttpRequest httpRequest) {
        // TODO
        try (Response okHttpResponse = this.executeDownload(httpRequest)) {
            if (okHttpResponse.body() != null) {
                return Objects.requireNonNull(okHttpResponse.body()).byteStream();
            }
        } catch (Exception e) {
            throw ExceptionThrower.throwUnchecked(e);
        }

        return null;
    }

    @Override
    public <T> HttpResponse<T> execute(HttpRequest request, Class<T> responseClass) {
        // TODO

        return null;
    }

    private Request buildOkHttpRequest(HttpRequest request) {
        return null;
    }

    private Response executeDownload(HttpRequest httpRequest) {
        Request okHttpRequest = this.buildOkHttpRequest(httpRequest);
        try {
            // preHandle
            Response okHttpResponse = okHttpClient
                    .newCall(okHttpRequest)
                    .execute();
            // posHandle

            if (super.isSuccessful(okHttpResponse.code())) {
                // TODO
            }

            return okHttpResponse;
        } catch (Exception e) {
            throw ExceptionThrower.throwUnchecked(e);
        }
    }
}

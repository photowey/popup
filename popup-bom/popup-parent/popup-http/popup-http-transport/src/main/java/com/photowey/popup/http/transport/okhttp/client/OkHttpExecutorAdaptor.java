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
package com.photowey.popup.http.transport.okhttp.client;

import com.photowey.popup.http.api.client.HttpClient;
import com.photowey.popup.http.api.constant.HttpConstants;
import com.photowey.popup.http.api.enums.HttpMethod;
import com.photowey.popup.http.api.request.HttpRequest;

import java.io.InputStream;

import static java.net.HttpURLConnection.HTTP_MULT_CHOICE;
import static java.net.HttpURLConnection.HTTP_OK;

/**
 * {@code OkHttpExecutorAdaptor}
 *
 * @author photowey
 * @date 2023/01/07
 * @since 1.0.0
 */
public abstract class OkHttpExecutorAdaptor implements HttpClient {

    @Override
    public InputStream download(String url) {
        HttpRequest httpRequest = this.buildDownloadRequest(url);

        return this.doDownload(httpRequest);
    }

    protected HttpRequest buildDownloadRequest(String url) {
        HttpRequest originRequest = HttpRequest
                .builder()
                .httpMethod(HttpMethod.GET)
                .url(url)
                .build();

        HttpRequest.Builder builder = HttpRequest
                .builder()
                .url(url)
                .httpMethod(HttpMethod.GET)
                .addHeader(HttpConstants.ACCEPT, HttpConstants.ACCEPT_VALUE_ALL)
                .addHeader(HttpConstants.USER_AGENT, this.determineUserAgent());

        this.preBuild(originRequest, builder);
        HttpRequest httpRequest = builder.build();
        this.postBuild(originRequest, httpRequest);

        return httpRequest;
    }

    private String determineUserAgent() {
        String ua = HttpConstants.determineUserAgent();

        return ua;
    }

    // -------------------------------------------------------------------------

    protected boolean isFailure(int httpStatus) {
        return this.isSuccessful(httpStatus);
    }

    protected boolean isSuccessful(int httpStatus) {
        return httpStatus >= HTTP_OK && httpStatus < HTTP_MULT_CHOICE;
    }

    // -------------------------------------------------------------------------

    public void preBuild(HttpRequest originRequest, HttpRequest.Builder builder) {
        // do nothing
    }

    public void postBuild(HttpRequest originRequest, HttpRequest httpRequest) {
        // do nothing
    }

    /**
     * {@code Execute} Download
     *
     * @param httpRequest {@link HttpRequest}
     * @return {@link InputStream}
     */
    public abstract InputStream doDownload(HttpRequest httpRequest);
}

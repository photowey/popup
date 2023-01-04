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
package com.photowey.popup.http.api.request;

import com.photowey.component.common.util.StringFormatUtils;
import com.photowey.popup.http.api.enums.HttpMethod;
import com.photowey.popup.http.api.model.header.HttpHeaders;
import com.photowey.popup.http.api.request.body.RequestBody;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static java.util.Objects.requireNonNull;

/**
 * {@code HttpRequest}
 *
 * @author photowey
 * @date 2023/01/02
 * @since 1.0.0
 */
public class HttpRequest implements Serializable {

    private static final long serialVersionUID = 7743910882263749659L;

    private final HttpMethod httpMethod;
    private final URL url;
    private final URI uri;
    private final HttpHeaders headers;
    private final RequestBody body;

    private HttpRequest(HttpMethod httpMethod, URL url, URI uri, HttpHeaders headers, RequestBody body) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.uri = uri;
        this.headers = headers;
        this.body = body;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public URL getUrl() {
        return url;
    }

    public URI getUri() {
        return uri;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public RequestBody getBody() {
        return body;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        HttpMethod httpMethod;
        private URL url;
        private HttpHeaders headers = new HttpHeaders();
        private RequestBody body;

        public Builder httpMethod(HttpMethod httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder url(URL url) {
            this.url = url;
            return this;
        }

        public Builder url(String url) {
            try {
                this.url = new URL(url);
                return this;
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid URL string:" + url);
            }
        }

        public Builder headers(HttpHeaders headers) {
            this.headers = headers;
            return this;
        }

        public Builder addHeader(String name, String value) {
            headers.addHeader(name, value);
            return this;
        }

        public Builder body(RequestBody body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            try {
                return new HttpRequest(
                        requireNonNull(this.httpMethod),
                        requireNonNull(this.url),
                        this.url.toURI(),
                        this.headers == null ? HttpHeaders.empty() : this.headers,
                        this.body
                );
            } catch (URISyntaxException e) {
                throw new RuntimeException(StringFormatUtils.format("Convert url: [{}] to uri failed.", url), e);
            }
        }
    }
}

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
package com.photowey.popup.http.transport.okhttp.interceptor;

import com.photowey.popup.http.api.model.context.RequestContext;
import okhttp3.Request;
import okhttp3.Response;

/**
 * {@code CompositeInterceptor}
 *
 * @author photowey
 * @date 2023/01/03
 * @since 1.0.0
 */
public interface CompositeInterceptor extends OkhttpInterceptor, RequestInterceptor {

    /**
     * {@code PRE} handle
     *
     * @param context {@link RequestContext}
     * @param builder {@link Request.Builder}
     */
    @Override
    default void preHandle(RequestContext context, Request.Builder builder) {

    }

    /**
     * {@code POST} handle
     *
     * @param context  {@link RequestContext}
     * @param response {@link Response}
     */
    @Override
    default void postHandle(RequestContext context, Response response) {

    }

}

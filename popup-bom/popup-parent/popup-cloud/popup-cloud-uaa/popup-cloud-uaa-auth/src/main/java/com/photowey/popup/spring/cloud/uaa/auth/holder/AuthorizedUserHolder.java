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
package com.photowey.popup.spring.cloud.uaa.auth.holder;

import com.photowey.component.common.util.ObjectUtils;
import com.photowey.component.exception.core.enums.ResponseStatusEnum;
import com.photowey.component.exception.core.model.PopupException;
import com.photowey.popup.spring.cloud.uaa.auth.user.JwtUser;
import com.photowey.popup.spring.cloud.uaa.auth.user.LoginUser;

/**
 * {@code AuthorizedUserHolder}
 *
 * @author photowey
 * @date 2023/03/02
 * @since 1.0.0
 */
public final class AuthorizedUserHolder {

    private static final ThreadLocal<LoginUser> LOGIN_USER_THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadLocal<JwtUser> JWT_USER_THREAD_LOCAL = new ThreadLocal<>();

    private AuthorizedUserHolder() {
        // utility class; can't create
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public static void set(LoginUser loginUser) {
        LOGIN_USER_THREAD_LOCAL.set(loginUser);
    }

    public static LoginUser get() {
        return LOGIN_USER_THREAD_LOCAL.get();
    }

    public static LoginUser must() {
        LoginUser loginUser = LOGIN_USER_THREAD_LOCAL.get();
        if (ObjectUtils.isNullOrEmpty(loginUser)) {
            throw new PopupException(ResponseStatusEnum.UNAUTHORIZED);
        }

        return loginUser;
    }

    public static void clean() {
        LOGIN_USER_THREAD_LOCAL.remove();
    }

    // --------------------------------------------------------------------------------------------------------------------------------

    public static void setJwt(JwtUser jwtUser) {
        JWT_USER_THREAD_LOCAL.set(jwtUser);
    }

    public static JwtUser getJwt() {
        return JWT_USER_THREAD_LOCAL.get();
    }

    public static JwtUser mustJwt() {
        JwtUser jwtUser = JWT_USER_THREAD_LOCAL.get();
        if (ObjectUtils.isNullOrEmpty(jwtUser)) {
            throw new PopupException(ResponseStatusEnum.UNAUTHORIZED);
        }

        return jwtUser;
    }

    public static void cleanJwt() {
        JWT_USER_THREAD_LOCAL.remove();
    }

}

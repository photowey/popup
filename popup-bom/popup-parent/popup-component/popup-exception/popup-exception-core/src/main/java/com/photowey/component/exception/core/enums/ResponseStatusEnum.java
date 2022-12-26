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
package com.photowey.component.exception.core.enums;

/**
 * {@code ResponseStatusEnum}
 *
 * @author photowey
 * @date 2022/12/26
 * @since 1.0.0
 */
public enum ResponseStatusEnum {

    /**
     * OK
     */
    OK("handle.request.ok", 200, "000000", "Ok"),

    BAD_REQUEST("handle.request.bad", 400, "400001", "Bad request"),

    UNAUTHORIZED("handle.request.unauthorized", 401, "401000", "Unauthorized"),

    UNAUTHORIZED_EXPIRED("handle.request.unauthorized.expired", 401, "401001", "Token:expired"),

    UNAUTHORIZED_INVALID_SIGNATURE("handle.request.unauthorized.invalid.signature", 401, "401002", "Token:invalid"),
    UNAUTHORIZED_DISTRUST_REQUEST("handle.request.unauthorized.distrust.request", 401, "401003", "Token:distrust"),
    UNAUTHORIZED_INVALID_TOKEN_TYPE("handle.request.unauthorized.invalid.token.type", 401, "401004", "Token:invalid type"),
    UNAUTHORIZED_INVALID_MISSING_REQUIRED_PARAMETER("handle.request.unauthorized.miss.request.parameter", 401, "401005", "Miss request parameter"),

    FORBIDDEN("handle.request.forbidden", 403, "403000", "Forbidden"),

    INNER_ERROR("handle.request.inner.error", 500, "500000", "Inner error"),

    TIME_OUT("handle.request.timeout.error", 504, "504000", "Time out"),

    ;

    private final String alias;
    private final int status;
    private final String code;
    private final String message;

    ResponseStatusEnum(String alias, int status, String code, String message) {
        this.alias = alias;
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static ResponseStatusEnum resolve(int status) {
        for (ResponseStatusEnum anEnum : values()) {
            if (status == anEnum.status()) {
                return anEnum;
            }
        }

        return INNER_ERROR;
    }

    public static ResponseStatusEnum resolve(String code) {
        for (ResponseStatusEnum anEnum : values()) {
            if (anEnum.code().equalsIgnoreCase(code)) {
                return anEnum;
            }
        }

        return INNER_ERROR;
    }

    public String alias() {
        return alias;
    }

    public int status() {
        return status;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}

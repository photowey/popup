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
package com.photowey.component.exception.core.model;

import com.photowey.component.exception.core.enums.ResponseStatusEnum;
import com.photowey.component.exception.core.util.MessageUtils;
import com.photowey.component.exception.core.wrapper.ResponseWrapper;

/**
 * {@code PopupException}
 *
 * @author photowey
 * @date 2023/03/02
 * @since 1.0.0
 */
public class PopupException extends RuntimeException {

    protected Integer status;
    protected String code;
    protected String message;

    public PopupException() {
        this(ResponseStatusEnum.BAD_REQUEST);
    }

    public PopupException(String message) {
        this(ResponseStatusEnum.BAD_REQUEST.status(), ResponseStatusEnum.BAD_REQUEST.code(), message);
    }

    public PopupException(String message, Object... args) {
        this(ResponseStatusEnum.BAD_REQUEST.status(), ResponseStatusEnum.BAD_REQUEST.code(), message, args);
    }

    public PopupException(int status, String message) {
        this(status, ResponseStatusEnum.resolve(status).code(), message);
    }

    public PopupException(int status, String message, Object... args) {
        this(status, ResponseStatusEnum.resolve(status).code(), message, args);
    }

    public PopupException(int status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public PopupException(String code, String message, Object... args) {
        this(ResponseStatusEnum.INNER_ERROR.status(), code, message, args);
    }

    public PopupException(int status, String code, String message, Object... args) {
        super(MessageUtils.format(message, args));
        this.status = status;
        this.code = code;
        this.message = MessageUtils.format(message, args);
    }

    public PopupException(String message, Throwable cause) {
        super(message, cause);
        this.status = ResponseStatusEnum.INNER_ERROR.status();
        this.code = ResponseStatusEnum.INNER_ERROR.code();
        this.message = message;
    }

    public PopupException(ResponseStatusEnum status) {
        this(status.status(), status.code(), status.message());
    }

    public PopupException(ResponseStatusEnum status, String message, Object... args) {
        this(status.status(), status.code(), message, args);
    }

    public PopupException(ResponseStatusEnum status, Throwable cause) {
        super(status.message(), cause);
        this.status = status.status();
        this.code = status.code();
        this.message = status.message();
    }

    public PopupException(String code, String message, Throwable cause) {
        super(message, cause);
        this.status = ResponseStatusEnum.INNER_ERROR.status();
        this.code = code;
        this.message = message;
    }

    public PopupException(Throwable cause) {
        this(ResponseStatusEnum.INNER_ERROR, cause);
    }

    public Integer getStatus() {
        return status;
    }

    public Integer status() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String code() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public ResponseWrapper toExceptionWrapper() {
        return new ResponseWrapper(this.status, this.code, this.message);
    }
}

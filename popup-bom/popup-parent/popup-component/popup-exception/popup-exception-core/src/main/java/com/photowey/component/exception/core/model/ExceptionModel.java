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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.photowey.component.exception.core.enums.ResponseStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * {@code ExceptionModel}
 *
 * @author photowey
 * @date 2022/12/26
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Exception data.model")
public class ExceptionModel implements Serializable {

    private static final long serialVersionUID = 7480517023708312921L;

    @JsonIgnore
    @ApiModelProperty(value = "Http status", example = "200")
    private Integer status;
    @ApiModelProperty(value = "Response code(ok: 000000) ", example = "000000")
    protected String code;
    @ApiModelProperty(value = "Response message", example = "Ok")
    protected String message;

    public ExceptionModel(ResponseStatusEnum exceptionStatus) {
        this.status = exceptionStatus.status();
        this.code = exceptionStatus.code();
        this.message = exceptionStatus.message();
    }

    public ExceptionModel(ResponseStatusEnum exceptionStatus, String message) {
        this.status = exceptionStatus.status();
        this.code = exceptionStatus.code();
        this.message = message;
    }

    public static ExceptionModel badRequest() {
        return new ExceptionModel(ResponseStatusEnum.BAD_REQUEST);
    }

    public static ExceptionModel badUnHandle() {
        return new ExceptionModel(
                ResponseStatusEnum.BAD_REQUEST,
                "The request could not be processed correctly, please try again later");
    }

    public static ExceptionModel unauthorized() {
        return new ExceptionModel(ResponseStatusEnum.UNAUTHORIZED);
    }

    public static ExceptionModel unauthorized(String message) {
        return new ExceptionModel(ResponseStatusEnum.UNAUTHORIZED, message);
    }

    public static ExceptionModel forbidden() {
        return new ExceptionModel(ResponseStatusEnum.FORBIDDEN);
    }

    public static ExceptionModel forbidden(String message) {
        return new ExceptionModel(ResponseStatusEnum.FORBIDDEN, message);
    }

    public static ExceptionModel timeout() {
        return new ExceptionModel(ResponseStatusEnum.TIME_OUT);
    }

}
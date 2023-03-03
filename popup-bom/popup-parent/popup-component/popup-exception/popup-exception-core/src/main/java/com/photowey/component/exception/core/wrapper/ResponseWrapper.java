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
package com.photowey.component.exception.core.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.photowey.component.exception.core.enums.ResponseStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * {@code ResponseWrapper}
 *
 * @author photowey
 * @date 2022/12/26
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Exception data.model")
public class ResponseWrapper implements Serializable {

    private static final long serialVersionUID = 7480517023708312921L;

    @JsonIgnore
    @ApiModelProperty(value = "Http status", example = "200")
    private Integer status;
    @ApiModelProperty(value = "Response code(ok: 000000) ", example = "000000")
    protected String code;
    @ApiModelProperty(value = "Response message", example = "Ok")
    protected String message;

    public ResponseWrapper(ResponseStatusEnum status) {
        this.status = status.status();
        this.code = status.code();
        this.message = status.message();
    }

    public ResponseWrapper(ResponseStatusEnum status, String message) {
        this.status = status.status();
        this.code = status.code();
        this.message = message;
    }

    public static ResponseWrapper badRequest() {
        return new ResponseWrapper(ResponseStatusEnum.BAD_REQUEST);
    }

    public static ResponseWrapper badUnHandle() {
        return new ResponseWrapper(
                ResponseStatusEnum.BAD_REQUEST,
                "The request could not be processed correctly, please try again later");
    }

    public static ResponseWrapper unauthorized() {
        return new ResponseWrapper(ResponseStatusEnum.UNAUTHORIZED);
    }

    public static ResponseWrapper unauthorized(String message) {
        return new ResponseWrapper(ResponseStatusEnum.UNAUTHORIZED, message);
    }

    public static ResponseWrapper forbidden() {
        return new ResponseWrapper(ResponseStatusEnum.FORBIDDEN);
    }

    public static ResponseWrapper forbidden(String message) {
        return new ResponseWrapper(ResponseStatusEnum.FORBIDDEN, message);
    }

    public static ResponseWrapper timeout() {
        return new ResponseWrapper(ResponseStatusEnum.TIME_OUT);
    }

}
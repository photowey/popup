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
package com.photowey.component.core.api;

import com.photowey.component.core.api.factory.EmptyEntityFactory;
import com.photowey.component.exception.core.enums.ResponseStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code ApiResult}
 *
 * @author photowey
 * @date 2022/12/26
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Single Object-data.model")
public class ApiResult<T> extends ResultSupportAdapter<T, ApiResult<T>> {

    private static final long serialVersionUID = 1795720589537195296L;

    @ApiModelProperty(value = "Response data")
    private T data;

    @ApiModelProperty(value = "Additional parameters")
    private Map<String, Object> additional = new HashMap<>();

    public ApiResult() {
    }

    public static <T> ApiResult<T> create() {
        return new ApiResult<>();
    }

    public static <T> ApiResult<T> success() {
        return new ApiResult<>(ResponseStatusEnum.OK.code(), ResponseStatusEnum.OK.message(), (T) EmptyEntityFactory.empty());
    }

    public static <T> ApiResult<T> success(T data) {
        ApiResult<T> apiResult = create();
        return apiResult.of(ResponseStatusEnum.OK.code(), ResponseStatusEnum.OK.message(), data);
    }

    public static <T> ApiResult<T> success(T data, Map<String, Object> additional) {
        ApiResult<T> apiResult = create();
        return apiResult.of(ResponseStatusEnum.OK.code(), ResponseStatusEnum.OK.message(), data, additional);
    }

    public static <T> ApiResult<T> failure() {
        return new ApiResult<>(ResponseStatusEnum.INNER_ERROR.code(), ResponseStatusEnum.INNER_ERROR.message());
    }

    public static <T> ApiResult<T> failure(Map<String, Object> additional) {
        ApiResult<T> apiResult = create();
        return apiResult.of(ResponseStatusEnum.INNER_ERROR.code(), ResponseStatusEnum.INNER_ERROR.message(), (T) EmptyEntityFactory.empty(), additional);
    }

    public static <T> ApiResultBuilder<T> builder() {
        return new ApiResultBuilder<>();
    }

    public ApiResult(String code, String message, T data) {
        super(code, message);
        this.data = data;
    }

    protected ApiResult(String code, String message) {
        super(code, message);
    }

    @Override
    public ApiResult<T> of(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;

        return this;
    }

    public ApiResult<T> of(String code, String message, T data, Map<String, Object> additional) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.additional = additional;

        return this;
    }

    @Data
    @NoArgsConstructor
    public static class ApiResultBuilder<T> {

        @ApiModelProperty(value = "Response code")
        private String code;

        @ApiModelProperty(value = "Response message")
        private String message;

        @ApiModelProperty(value = "Response data")
        private T data;

        @ApiModelProperty(value = "Additional parameters")
        private Map<String, Object> additional = new HashMap<>();

        public ApiResultBuilder<T> code(String code) {
            this.code = code;
            return this;
        }

        public ApiResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResultBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResultBuilder<T> data(Map<String, Object> additional) {
            this.additional = additional;
            return this;
        }

        public ApiResult<T> build() {
            ApiResult<T> apiResult = ApiResult.create();
            apiResult.of(code, message, data, additional);
            return apiResult;
        }
    }
}
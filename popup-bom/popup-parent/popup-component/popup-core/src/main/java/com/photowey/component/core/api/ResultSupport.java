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
package com.photowey.component.core.api;

import com.photowey.component.common.constant.PopupConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * {@code ResultSupport}
 *
 * @author photowey
 * @date 2022/12/26
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@ApiModel(value = "Response base data.model")
public abstract class ResultSupport<T, R extends ResultSupport<T, R>> implements Serializable {

    private static final long serialVersionUID = 1915000598709694588L;

    @ApiModelProperty(value = "Response code(ok: 000000) ", example = "000000")
    protected String code;

    @ApiModelProperty(value = "Response message", example = "Ok")
    protected String message;

    protected ResultSupport(String code, String message) {
        this.code = code;
        this.message = message;
    }

    protected abstract ResultSupport<T, R> of(String code, String message, T data);

    protected abstract ResultSupport<T, R> of(String code, String message, List<T> data, Meta meta);

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public R code(String code) {
        this.code = code;
        return (R) this;
    }

    public R message(String message) {
        this.message = message;
        return (R) this;
    }

    public boolean requestSuccessful() {
        return PopupConstants.isSuccessfully(this.code());
    }

    public boolean requestFailure() {
        return !this.requestSuccessful();
    }
}
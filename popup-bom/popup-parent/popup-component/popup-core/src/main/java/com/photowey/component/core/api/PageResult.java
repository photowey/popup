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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.photowey.component.core.api.struct.ListStruct;
import com.photowey.component.exception.core.enums.ResponseStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * {@code PageResult}
 *
 * @author photowey
 * @date 2022/12/26
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Multi Object-data.model")
public class PageResult<T> extends ResultSupportAdapter<T, PageResult<T>> {

    private static final long serialVersionUID = -3989525348337490204L;

    @ApiModelProperty(value = "List data.model")
    public ListStruct<T> data;

    public PageResult() {
    }

    protected PageResult(String code, String message) {
        super(code, message);
    }

    public ListStruct<T> data() {
        return data;
    }

    public static <T> PageResultBuilder<T> builder() {
        return new PageResultBuilder<>();
    }

    public static <T> PageResult<T> create() {
        return new PageResult<>();
    }

    public static <T> PageResult<T> ofStatic(List<T> data) {
        PageResult<T> pageResult = PageResult.create();
        return pageResult.of(data);
    }

    public static <T> PageResult<T> ofStatic(List<T> data, Meta meta) {
        PageResult<T> pageResult = PageResult.create();
        return pageResult.of(data, meta);
    }

    public static <T> PageResult<T> ofStatic(List<T> data, Map<String, Object> additional) {
        PageResult<T> pageResult = PageResult.create();
        return pageResult.of(data, additional);
    }

    public static <T> PageResult<T> emptyStatic() {
        PageResult<T> pageResult = PageResult.create();
        return pageResult.of(new ArrayList<>(), Meta.populateDefaultMeta());
    }

    public static <T> PageResult<T> ofStatic(IPage<T> page) {
        PageResult<T> pageResult = create();
        ListStruct<T> list = ListStruct.of(page);
        pageResult.setData(list);

        return pageResult;
    }

    public static <T> PageResult<T> ofStatic(IPage<T> page, Map<String, Object> additional) {
        PageResult<T> pageResult = create();
        ListStruct<T> listStruct = ListStruct.of(page, additional);
        pageResult.setData(listStruct);

        return pageResult;
    }

    public static <T, D> PageResult<T> ofStatic(IPage<D> page, Function<List<D>, List<T>> transfer) {
        PageResult<T> pageResult = create();
        return pageResult.of(ResponseStatusEnum.OK.code(),
                ResponseStatusEnum.OK.message(),
                transfer.apply(page.getRecords()),
                Meta.populateMeta(page)
        );
    }

    public static <T, D> PageResult<T> ofStatic(
            IPage<D> page,
            Map<String, Object> additional,
            Function<List<D>, List<T>> transfer
    ) {
        PageResult<T> pageResult = create();
        return pageResult.of(ResponseStatusEnum.OK.code(),
                ResponseStatusEnum.OK.message(),
                transfer.apply(page.getRecords()),
                Meta.populateMeta(page),
                additional
        );
    }

    public static <T, D> PageResult<T> ofStatic(PageResult<D> page, Function<List<D>, List<T>> transfer) {
        PageResult<T> pageResult = create();
        return pageResult.of(ResponseStatusEnum.OK.code(),
                ResponseStatusEnum.OK.message(),
                transfer.apply(page.getData().getList()),
                Meta.populateMeta(page)
        );
    }

    public static <T, D> PageResult<T> ofStatic(
            PageResult<D> page,
            Map<String, Object> additional,
            Function<List<D>, List<T>> transfer
    ) {
        PageResult<T> pageResult = create();
        return pageResult.of(ResponseStatusEnum.OK.code(),
                ResponseStatusEnum.OK.message(),
                transfer.apply(page.getData().getList()),
                Meta.populateMeta(page), additional
        );
    }

    public static <T> PageResult<T> empty() {
        PageResult<T> pageResult = create();
        ListStruct<T> listStruct = ListStruct.create();
        pageResult.setData(listStruct);

        return pageResult;
    }

    public PageResult<T> of(List<T> data) {
        return this.of(ResponseStatusEnum.OK.code(), ResponseStatusEnum.OK.message(), data);
    }

    public PageResult<T> of(List<T> data, Map<String, Object> additional) {
        return this.of(ResponseStatusEnum.OK.code(), ResponseStatusEnum.OK.message(), data, additional);
    }

    public PageResult<T> of(String code, String message, List<T> data) {
        return this.of(code, message, data, Meta.populateDefaultMeta());
    }

    public PageResult<T> of(String code, String message, List<T> data, Map<String, Object> additional) {
        return this.of(code, message, data, Meta.populateDefaultMeta(), additional);
    }

    public PageResult<T> of(List<T> data, Meta meta) {
        this.code = ResponseStatusEnum.OK.code();
        this.message = ResponseStatusEnum.OK.message();
        this.data = new ListStruct<>(data, meta);

        return this;
    }

    public PageResult<T> of(IPage<T> page) {
        return this.of(ResponseStatusEnum.OK.code(), ResponseStatusEnum.OK.message(), page);
    }

    public PageResult<T> of(IPage<T> page, Map<String, Object> additional) {
        return this.of(ResponseStatusEnum.OK.code(), ResponseStatusEnum.OK.message(), page, additional);
    }

    public PageResult<T> of(String code, String message, IPage<T> page) {
        return this.of(code, message, page.getRecords(), Meta.populateMeta(page));
    }

    public PageResult<T> of(String code, String message, IPage<T> page, Map<String, Object> additional) {
        return this.of(code, message, page.getRecords(), Meta.populateMeta(page), additional);
    }

    public <D> PageResult<T> of(String code, String message, IPage<D> page, Function<List<D>, List<T>> transfer) {
        return this.of(code, message, transfer.apply(page.getRecords()), Meta.populateMeta(page));
    }

    public <D> PageResult<T> of(
            String code,
            String message,
            IPage<D> page,
            Map<String, Object> additional,
            Function<List<D>, List<T>> transfer) {
        return this.of(code, message, transfer.apply(page.getRecords()), Meta.populateMeta(page), additional);
    }

    public <D> PageResult<T> of(IPage<D> page, Function<List<D>, List<T>> transfer) {
        return this.of(ResponseStatusEnum.OK.code(),
                ResponseStatusEnum.OK.message(),
                transfer.apply(page.getRecords()),
                Meta.populateMeta(page)
        );
    }

    public <D> PageResult<T> of(IPage<D> page, Map<String, Object> additional, Function<List<D>, List<T>> transfer) {
        return this.of(ResponseStatusEnum.OK.code(),
                ResponseStatusEnum.OK.message(),
                transfer.apply(page.getRecords()),
                Meta.populateMeta(page),
                additional
        );
    }

    @Override
    public PageResult<T> of(String code, String message, List<T> data, Meta meta) {
        this.code = code;
        this.message = message;
        this.data = new ListStruct<>(data, meta);

        return this;
    }

    public PageResult<T> of(String code, String message, List<T> data, Meta meta, Map<String, Object> additional) {
        this.code = code;
        this.message = message;
        this.data = new ListStruct<>(data, meta, additional);

        return this;
    }

    @Data
    @NoArgsConstructor
    public static class PageResultBuilder<T> {
        /**
         * 响应状态码
         */
        @ApiModelProperty(value = "响应状态码")
        protected String code;
        /**
         * 响应信息
         */
        @ApiModelProperty(value = "响应信息")
        protected String message;
        /**
         * 响应数据
         */
        @ApiModelProperty(value = "响应数据")
        protected List<T> data;
        /**
         * 元数据
         */
        @ApiModelProperty(value = "元数据")
        private Meta meta;
        /**
         * 分页对象
         */
        @ApiModelProperty(value = "分页对象")
        private IPage<T> page;

        @ApiModelProperty(value = "附加参数列表")
        private Map<String, Object> additional = new HashMap<>();

        public PageResultBuilder<T> code(String code) {
            this.code = code;
            return this;
        }

        public PageResultBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public PageResultBuilder<T> data(List<T> data) {
            this.data = data;
            return this;
        }

        public PageResultBuilder<T> meta(Meta meta) {
            this.meta = meta;
            return this;
        }

        public PageResultBuilder<T> page(IPage<T> page) {
            this.page = page;
            return this;
        }

        public PageResultBuilder<T> additional(Map<String, Object> additional) {
            this.additional = additional;
            return this;
        }

        public PageResult<T> build() {
            PageResult<T> pageResult = PageResult.create();
            if (null != this.page) {
                return pageResult.of(code, message, page, additional);
            }
            pageResult.of(code, message, data, meta, additional);

            return pageResult;
        }
    }
}
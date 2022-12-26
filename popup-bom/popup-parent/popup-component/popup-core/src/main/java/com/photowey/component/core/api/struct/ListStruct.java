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
package com.photowey.component.core.api.struct;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.photowey.component.common.util.ObjectUtils;
import com.photowey.component.core.api.Meta;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code ListStruct}
 *
 * @author photowey
 * @date 2022/12/26
 * @since 1.0.0
 */
@Data
public class ListStruct<T> implements Serializable {

    private static final long serialVersionUID = -7034712230756937594L;

    @ApiModelProperty(value = "Current page no.(default: 1)", example = "1")
    private int current = 1;

    @ApiModelProperty(value = "Page size(default: 10)", example = "10")
    private int pageSize = 10;

    @ApiModelProperty(value = "Total count", example = "8848")
    private int count;

    @ApiModelProperty(value = "Response body")
    private List<T> list = new ArrayList<>(0);

    @ApiModelProperty(value = "Additional parameters")
    private Map<String, Object> additional = new HashMap<>();

    public ListStruct() {
    }

    public ListStruct(List<T> list) {
        this.current = 1;
        this.pageSize = 20;
        this.count = 0;
        this.list = list;
    }

    public ListStruct(List<T> list, Meta meta) {
        this(list, meta, null);
    }

    public ListStruct(List<T> list, Meta meta, Map<String, Object> additional) {
        this.current = ObjectUtils.isNotNullOrEmpty(meta.getCurrent()) ? meta.getCurrent().intValue() : 1;
        this.pageSize = ObjectUtils.isNotNullOrEmpty(meta.getPageSize()) ? meta.getPageSize().intValue() : 10;
        this.count = ObjectUtils.isNotNullOrEmpty(meta.getTotal()) ? meta.getTotal().intValue() : 0;
        this.list = list;
        if (ObjectUtils.isNullOrEmpty(additional)) {
            additional = new HashMap<>(2);
        }

        this.additional = additional;
    }

    public ListStruct(Long current, Long pageSize, Long count, List<T> list) {
        this.current = ObjectUtils.isNotNullOrEmpty(current) ? current.intValue() : 1;
        this.pageSize = ObjectUtils.isNotNullOrEmpty(pageSize) ? pageSize.intValue() : 10;
        this.count = ObjectUtils.isNotNullOrEmpty(count) ? count.intValue() : 0;
        this.list = list;
    }

    public ListStruct(IPage<T> page) {
        this.current = ObjectUtils.isNotNullOrEmpty(page.getCurrent()) ? (int) page.getCurrent() : 1;
        this.pageSize = ObjectUtils.isNotNullOrEmpty(page.getSize()) ? (int) page.getSize() : 10;
        this.count = ObjectUtils.isNotNullOrEmpty(page.getTotal()) ? (int) page.getTotal() : 0;
        this.list = page.getRecords();
    }

    public static <D> ListStruct<D> create() {
        return new ListStruct<>();
    }

    public static <D> ListStruct<D> create(Meta meta) {
        ListStruct<D> list = create();
        list.current(meta.getCurrent());
        list.pageSize(meta.getPageSize());
        list.count(meta.getTotal());

        return list;
    }

    public static <D> ListStruct<D> of(List<D> data) {
        ListStruct<D> list = create(Meta.populateDefaultMeta());
        list.list(data);

        return list;
    }

    public static <D> ListStruct<D> of(List<D> data, Meta meta) {
        ListStruct<D> list = create(meta);
        list.list(data);

        return list;
    }

    public static <D> ListStruct<D> of(List<D> data, Meta meta, Map<String, Object> additional) {
        ListStruct<D> list = create(meta);
        list.list(data);
        list.additional(additional);

        return list;
    }

    public static <D> ListStruct<D> of(IPage<D> page) {
        Meta meta = Meta.populateMeta(page);
        return of(page.getRecords(), meta);
    }

    public static <D> ListStruct<D> of(IPage<D> page, Map<String, Object> additional) {
        Meta meta = Meta.populateMeta(page);
        return of(page.getRecords(), meta, additional);
    }

    public Long current() {
        return (long) current;
    }

    public Long pageSize() {
        return (long) pageSize;
    }

    public Long count() {
        return (long) count;
    }

    public List<T> list() {
        return list;
    }

    public Map<String, Object> additional() {
        return additional;
    }

    public ListStruct<T> current(Long current) {
        this.current = ObjectUtils.isNotNullOrEmpty(current) ? current.intValue() : 1;
        return this;
    }

    public ListStruct<T> pageSize(Long pageSize) {
        this.pageSize = ObjectUtils.isNotNullOrEmpty(pageSize) ? pageSize.intValue() : 10;
        return this;
    }

    public ListStruct<T> count(Long count) {
        this.count = ObjectUtils.isNotNullOrEmpty(count) ? count.intValue() : 0;
        return this;
    }

    public ListStruct<T> list(List<T> list) {
        this.list = list;
        return this;
    }

    public ListStruct<T> additional(Map<String, Object> additional) {
        if (ObjectUtils.isNullOrEmpty(additional)) {
            additional = new HashMap<>(2);
        }
        this.additional = additional;

        return this;
    }
}

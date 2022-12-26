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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * {@code Meta}
 *
 * @author photowey
 * @date 2022/12/26
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Page meta data")
public class Meta implements Serializable {

    private static final long serialVersionUID = 3594719854459082616L;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "Current page no.(default: 1)", example = "1")
    private Long current;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "Page size(default: 10)", example = "10")
    private Long pageSize;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "Total count", example = "105")
    private Long total;

    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "Total pages", example = "11")
    private Long totalPages;

    public static <D> Meta populateMeta(IPage<D> page) {
        return Meta.builder()
                .current(page.getCurrent())
                .pageSize(page.getSize())
                .total(page.getTotal())
                .totalPages(page.getPages())
                .build();
    }

    public static <D> Meta populateMeta(PageResult<D> page) {
        return Meta.builder()
                .current(page.getData().current())
                .pageSize(page.getData().pageSize())
                .total(page.getData().count())
                .build();
    }

    public static Meta populateDefaultMeta() {
        return populateMeta(10L);
    }

    public static Meta populateMeta(Integer pageSize) {
        return populateMeta(Long.valueOf(pageSize));
    }

    public static Meta populateMeta(Long pageSize) {
        return populateMeta(0L, 1L, pageSize);
    }

    public static Meta populateMeta(Long total, Long pageNo, Long pageSize) {
        return Meta.builder()
                .current(pageNo)
                .pageSize(pageSize)
                .total(total)
                .totalPages(getPages(total, pageSize))
                .build();
    }

    private static long getPages(Long total, Long pageSize) {
        if (pageSize == 0L) {
            return 0L;
        } else {
            long pages = total / pageSize;
            if (total % pageSize != 0L) {
                ++pages;
            }

            return pages;
        }
    }
}
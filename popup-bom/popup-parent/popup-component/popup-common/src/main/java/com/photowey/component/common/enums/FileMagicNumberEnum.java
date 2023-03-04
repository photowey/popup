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
package com.photowey.component.common.enums;

import com.photowey.component.common.util.ObjectUtils;

import java.util.*;

/**
 * {@code FileMagicNumberEnum}
 *
 * @author photowey
 * @date 2023/03/04
 * @since 1.0.0
 */
public enum FileMagicNumberEnum {

    /**
     * Magic number.
     */
    XLS("xls", "D0CF11E0A1B11AE1"),
    DOC("doc", "D0CF11E0A1B11AE1"),

    XLSX("xlsx", "504B0304"),
    DOCX("docx", "504B0304"),

    ;

    private final String suffix;
    private final String code;

    private static final Set<String> EXCEL_SUFFIX = new HashSet<>();
    private static final Set<String> DOC_SUFFIX = new HashSet<>();

    static {
        EXCEL_SUFFIX.add(XLS.suffix());
        EXCEL_SUFFIX.add(XLSX.suffix());

        DOC_SUFFIX.add(DOC.suffix());
        DOC_SUFFIX.add(DOCX.suffix());
    }

    FileMagicNumberEnum(String suffix, String code) {
        this.suffix = suffix;
        this.code = code;
    }

    public static FileMagicNumberEnum codeOf(String header, String... suffix) {
        if (ObjectUtils.isNullOrEmpty(header)) {
            return null;
        }

        List<String> suffixes = Arrays.asList(suffix);

        List<FileMagicNumberEnum> magics = new ArrayList<>();
        if (ObjectUtils.isNotNullOrEmpty(suffixes)) {
            for (FileMagicNumberEnum magic : values()) {
                if (suffixes.contains(magic.suffix())) {
                    magics.add(magic);
                }
            }
        }

        magics = ObjectUtils.isNotNullOrEmpty(magics) ? magics : Arrays.asList(values());

        for (FileMagicNumberEnum magic : magics) {
            if (header.toUpperCase().startsWith(magic.code())) {
                return magic;
            }
        }

        return null;
    }

    public static Set<String> excelSuffix() {
        return EXCEL_SUFFIX;
    }

    public static Set<String> docSuffix() {
        return DOC_SUFFIX;
    }

    public String suffix() {
        return suffix;
    }

    public String code() {
        return code;
    }
}

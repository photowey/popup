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
package com.photowey.component.common.constant;

/**
 * {@code PopupConstants}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public interface PopupConstants {

    // ------------------------------------------------------------------------- Integer

    int STATUS_OK = 1;
    int STATUS_DELETE = 0;

    int EMPTY_LIST = 0;
    int SINGLE_LIST = 1;

    int MOBILE_LENGTH = 11;

    // ------------------------------------------------------------------------- Long

    // ------------------------------------------------------------------------- String

    /**
     * {@code API} Success
     */
    String API_OK = "000000";
    /**
     * Empty String
     */
    String EMPTY_STRING = "";
    String MESSAGE_OK = "";

    /**
     * Unique key
     */
    String DUPLICATE_KEY_UNIQUE_CONSTRAINT = "unique constraint \"uk_";
    /**
     * Primary key
     */
    String DUPLICATE_KEY_PRIMARY_CONSTRAINT = "unique constraint \"pk_";

    // ------------------------------------------------------------------------- SQL

    String LIMIT_1 = "LIMIT 1";

    // --------------------------------------------------------- Pattern

    String ANY_REQUEST = "/**";
    String ANY_REQUEST_ANT = ANY_REQUEST;
    String ANY_REQUEST_PATH = "/*";

    // --------------------------------------------------------- Separator

    String PATH_SEPARATOR = "/";
    String SLASH_SEPARATOR = PATH_SEPARATOR;
    String BACK_SLASH_SEPARATOR = "\\";
    String COLON_SEPARATOR = ":";
    String DOT_SEPARATOR = ".";
    String AT_SEPARATOR = "@";
    String WELL_SEPARATOR = "#";
    String DOLLAR_SEPARATOR = "$";
    String COMMA_SEPARATOR = ",";

    // ------------------------------------------------------------------------- Boolean

    /**
     * {@code API} response status
     *
     * @param responseCode the response code
     * @return {@code boolean}
     */
    static boolean isSuccessfully(String responseCode) {
        return API_OK.equalsIgnoreCase(responseCode);
    }
}

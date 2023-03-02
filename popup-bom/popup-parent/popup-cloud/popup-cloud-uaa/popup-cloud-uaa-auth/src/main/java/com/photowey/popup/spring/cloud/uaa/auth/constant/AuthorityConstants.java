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
package com.photowey.popup.spring.cloud.uaa.auth.constant;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code AuthorityConstants}
 *
 * @author photowey
 * @date 2023/03/02
 * @since 1.0.0
 */
public interface AuthorityConstants {

    // ---------------------------------------------------------- String

    String EMPTY_STRING = "";
    String SPACE_STRING = " ";

    String ANONYMOUS_STRING = "anonymous";

    // ---------------------------------------------------------- login

    String LOGIN_PAGE = "/login";

    // ---------------------------------------------------------- symbol

    String PRINCIPAL_SEPARATOR_AT = ":@:";
    String ENC_UI_SEPARATOR_COMMA = ",";
    String CHALLENGE_SEPARATOR_FROM_TO = "_2_";

    /**
     * {@code BindTypeEnum#ACCOUNT}
     */
    String LOGIN_USER_NAME_PREFIX_U = "u";
    String LOGIN_USER_NAME_REFRESH_PREFIX_UR = "ur";
    String LOGIN_USER_NAME_EXCHANGE_PREFIX_UE = "ue";
    String LOGIN_USER_NAME_SCAN_PREFIX_US = "us";

    String LOGIN_MOBILE_PREFIX_M = "m";
    String LOGIN_OPENAPI_PREFIX_M = "oi";
    String LOGIN_THIRDPARTY_PREFIX_THP = "thp";

    String LOGIN_USER_NAME_PREFIX = LOGIN_USER_NAME_PREFIX_U + PRINCIPAL_SEPARATOR_AT;
    String LOGIN_USER_NAME_REFRESH_PREFIX = LOGIN_USER_NAME_REFRESH_PREFIX_UR + PRINCIPAL_SEPARATOR_AT;
    String LOGIN_USER_NAME_EXCHANGE_PREFIX = LOGIN_USER_NAME_EXCHANGE_PREFIX_UE + PRINCIPAL_SEPARATOR_AT;
    String LOGIN_USER_NAME_SCAN_PREFIX = LOGIN_USER_NAME_SCAN_PREFIX_US + PRINCIPAL_SEPARATOR_AT;

    String LOGIN_MOBILE_PREFIX = LOGIN_MOBILE_PREFIX_M + PRINCIPAL_SEPARATOR_AT;
    String LOGIN_OPENAPI_PREFIX = LOGIN_OPENAPI_PREFIX_M + PRINCIPAL_SEPARATOR_AT;
    String LOGIN_THIRDPARTY_PREFIX = LOGIN_THIRDPARTY_PREFIX_THP + PRINCIPAL_SEPARATOR_AT;

    // ---------------------------------------------------------- scope_select

    String SCOPE_SELECT = "select";
    String SCOPE_GLOBAL = "global";
    String SCOPE_USERINFO = "userinfo";
    /**
     * thirdparty_binding
     */
    String SCOPE_THIRDPARTY_BINDING = "thp_b";
    /**
     * single_binding
     */
    String SCOPE_SINGLE_BINDING = "sb";
    String SCOPE_PRINCIPAL_LEVEL_PREFIX = "plv_";
    String SCOPE_PRINCIPAL_TYPE_PREFIX = "ptp_";
    String SCOPE_APP = "APP";
    String SCOPE_USER = "USER";

    // ---------------------------------------------------------- captcha

    String SPRING_SECURITY_CAPTCHA_FORM_USERNAME_KEY = "mobile";

    String SPRING_SECURITY_CAPTCHA_FORM_CAPTCHA_KEY = "captcha";
    String SPRING_SECURITY_CLIENT_FORM_CAPTCHA_KEY = "client";

    // ---------------------------------------------------------- auth

    /**
     * {@code org.springframework.http.HttpHeaders#AUTHORIZATION}
     */
    String AUTHORIZATION_HEADER = "Authorization";
    String AUTHORIZATION_TOKEN_TYPE_BEARER = "Bearer";

    String AUTHORIZED_SCOPE_SELECT = SCOPE_SELECT;
    String AUTHORIZED_SCOPE_GLOBAL = SCOPE_GLOBAL;
    String AUTHORIZED_SCOPE_USERINFO = SCOPE_USERINFO;
    String AUTHORIZED_SCOPE_APP = SCOPE_APP;
    String AUTHORIZED_SCOPE_USER = SCOPE_USER;
    String AUTHORIZED_SCOPE_THIRDPARTY_BINDING = SCOPE_THIRDPARTY_BINDING;
    String AUTHORIZED_SCOPE_SINGLE_BINDING = SCOPE_SINGLE_BINDING;
    String AUTHORIZED_SCOPE_PRINCIPAL_LEVEL = SCOPE_SINGLE_BINDING;

    /**
     * Prefix
     */
    String SPRING_SECURITY_AUTHORITY_PREFIX = "ROLE_";
    String SPRING_OAUTH2_SCOPE_PREFIX = "SCOPE_";

    String SPRING_SECURITY_AUTHORITY_APP = SCOPE_APP;
    String SPRING_SECURITY_AUTHORITY_USER = SCOPE_USER;

    /**
     * ROLE_xx
     */
    String ROLE_AUTHORIZED_USER = SPRING_SECURITY_AUTHORITY_PREFIX + SPRING_SECURITY_AUTHORITY_USER;

    /**
     * SCOPE_xx
     */
    String SCOPE_AUTHORIZED_ROLE_APP = SPRING_OAUTH2_SCOPE_PREFIX + SPRING_SECURITY_AUTHORITY_APP;
    String SCOPE_AUTHORIZED_ROLE_USER = SPRING_OAUTH2_SCOPE_PREFIX + SPRING_SECURITY_AUTHORITY_USER;
    String SCOPE_AUTHORIZED_SCOPE_USERINFO = SPRING_OAUTH2_SCOPE_PREFIX + AUTHORIZED_SCOPE_USERINFO;
    String SCOPE_AUTHORIZED_SCOPE_APP = SPRING_OAUTH2_SCOPE_PREFIX + AUTHORIZED_SCOPE_APP;
    String SCOPE_AUTHORIZED_SCOPE_SELECT = SPRING_OAUTH2_SCOPE_PREFIX + AUTHORIZED_SCOPE_SELECT;
    String SCOPE_AUTHORIZED_SCOPE_THIRDPARTY_BINDING = SPRING_OAUTH2_SCOPE_PREFIX + AUTHORIZED_SCOPE_THIRDPARTY_BINDING;
    String SCOPE_AUTHORIZED_SCOPE_SINGLE_BINDING = SPRING_OAUTH2_SCOPE_PREFIX + AUTHORIZED_SCOPE_SINGLE_BINDING;

    String ACCESS_TOKEN_CLAIMS_EXT_MAPS_KEY = "ext.params";

    // ---------------------------------------------------------- auth.encrypt

    /**
     * enc == encrypt
     */
    String AUTH_ENCRYPT_SYMBOL_PREFIX = "enc.";
    String QRCODE_ENCRYPT_SYMBOL_PREFIX = "qr.";
    /**
     * ui == username and id
     */
    String AUTH_ENCRYPT_SYMBOL_PREFIX_UI = "ui.enc.";

    // ---------------------------------------------------------------------------------------------------------------- Integer

    int AUTHENTICATED_ORDERED = 5;

    int REQUEST_IDENTITY_ADMIN = 1;
    int REQUEST_IDENTITY_EMPLOYEE = 2;

    // ---------------------------------------------------------------------------------------------------------------- key

    String UAA_FIXED_KEY_PRINCIPAL_LEVEL = "principal.level";

    // ---------------------------------------------------------------------------------------------------------------- method

    String AUTHENTICATED_USER_REQUEST_ATTRIBUTE_KEY = "com.photowey.popup.spring.cloud.uaa.auth.user.JwtUser";

    // ---------------------------------------------------------------------------------------------------------------- method

    static String withExtensionScopes(String scope) {
        Set<String> scopes = Stream.of(scope.split(SPACE_STRING)).collect(Collectors.toSet());
        scopes.add(AUTHORIZED_SCOPE_USERINFO);
        scopes.add(SPRING_SECURITY_AUTHORITY_USER);

        return String.join(SPACE_STRING, scopes);
    }
}


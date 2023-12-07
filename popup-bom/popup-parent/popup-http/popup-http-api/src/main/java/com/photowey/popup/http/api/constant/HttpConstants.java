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
package com.photowey.popup.http.api.constant;

import com.photowey.component.common.formatter.StringFormatter;

/**
 * {@code HttpConstants}
 *
 * @author photowey
 * @date 2023/01/07
 * @since 1.0.0
 */
public interface HttpConstants {

    String OS = System.getProperty("os.name") + "/" + System.getProperty("os.version");
    String JAVA_VERSION = System.getProperty("java.version");

    String AUTHORIZATION = "Authorization";

    String ACCEPT = "Accept";
    String ACCEPT_VALUE_ALL = "*/*";

    String CONTENT_TYPE = "Content-Type";

    String USER_AGENT = "User-Agent";
    /**
     * ${os} Java/${java.version}
     */
    String USER_AGENT_TEMPLATE = "{} Java/{}";

    static String determineJavaVersion() {
        String version = HttpConstants.JAVA_VERSION == null ? "Unknown" : HttpConstants.JAVA_VERSION;

        return version;
    }

    static String determineUserAgent() {
        String version = HttpConstants.determineJavaVersion();

        return StringFormatter.format(HttpConstants.USER_AGENT_TEMPLATE, HttpConstants.OS, version);
    }
}

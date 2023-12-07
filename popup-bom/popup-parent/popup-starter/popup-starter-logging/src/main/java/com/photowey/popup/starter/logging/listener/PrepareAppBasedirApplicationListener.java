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
package com.photowey.popup.starter.logging.listener;

import com.photowey.popup.app.starting.listener.AppStartingListener;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@code PrepareAppBasedirApplicationListener}
 *
 * @author photowey
 * @date 2023/03/01
 * @since 1.0.0
 */
public class PrepareAppBasedirApplicationListener extends AppStartingListener implements EnvironmentAware, Ordered {

    public static final int DEFAULT_ORDER = Ordered.HIGHEST_PRECEDENCE;
    public static final String PROVIDER_HOME_KEY = "app.base";
    public static final String SLASH = "/";
    public static final String CLASSPATH_ROOT = "";

    protected final AtomicBoolean started = new AtomicBoolean(false);

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void handleApplicationStartingEvent(ApplicationStartingEvent event) {
        if (this.started.compareAndSet(false, true)) {
            this.tryInitAppHome();
        }
    }

    protected void tryInitAppHome() {
        String path = this.getClass().getClassLoader().getResource(CLASSPATH_ROOT).getPath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8);

        String[] supports = new String[]{
                "(file:)?(.*/).*\\.jar!.*",
                "(file:)?(.*/target/)test-classes.*",
                "(file:)?(.*/target/)classes.*",
        };
        for (String support : supports) {
            Matcher matcher = Pattern.compile(support).matcher(path);
            if (matcher.matches()) {
                path = matcher.group(2);
                break;
            }
        }

        if (path.endsWith(SLASH)) {
            path = path.substring(0, path.length() - 1);
        }

        System.setProperty(PROVIDER_HOME_KEY, path);
    }

    @Override
    public int getOrder() {
        return DEFAULT_ORDER;
    }

}


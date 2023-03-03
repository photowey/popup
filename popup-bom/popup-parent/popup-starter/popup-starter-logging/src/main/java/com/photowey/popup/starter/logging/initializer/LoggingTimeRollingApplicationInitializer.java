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
package com.photowey.popup.starter.logging.initializer;

import com.photowey.popup.starter.logging.logback.TimeRollingFileInstaller;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.logging.logback.LogbackLoggingSystem;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.ClassPathResource;

/**
 * {@code LoggingTimeRollingApplicationInitializer}
 *
 * @author photowey
 * @date 2023/03/01
 * @since 1.0.0
 */
public class LoggingTimeRollingApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    private static final int INITIALIZER_ORDER = Ordered.HIGHEST_PRECEDENCE + 20;

    protected Boolean hasResourceConfigurer() {
        String[] locations = new String[]{
                "logback.xml",
                "logback-test.xml",
                "logback-dev.xml",
                "logback-prod.xml",
                "logback-spring.xml"
        };

        for (String location : locations) {
            ClassPathResource resource = new ClassPathResource(location, getClass().getClassLoader());
            if (resource.exists()) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    public boolean isLogback() {
        LoggingSystem loggingSystem = LoggingSystem.get(LoggingTimeRollingApplicationInitializer.class.getClassLoader());
        return loggingSystem instanceof LogbackLoggingSystem;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!this.isLogback() || this.hasResourceConfigurer()) {
            return;
        }

        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        TimeRollingFileInstaller installer = new TimeRollingFileInstaller(environment);
        installer.install();
    }

    @Override
    public int getOrder() {
        return INITIALIZER_ORDER;
    }
}

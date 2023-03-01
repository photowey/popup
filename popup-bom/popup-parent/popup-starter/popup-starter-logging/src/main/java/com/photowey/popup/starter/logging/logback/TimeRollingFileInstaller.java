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
package com.photowey.popup.starter.logging.logback;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.photowey.popup.starter.logging.shared.spring.resolver.RelaxedPropertyResolver;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * {@code TimeRollingFileInstaller}
 *
 * @author photowey
 * @date 2023/03/02
 * @since 1.0.0
 */
public class TimeRollingFileInstaller {

    private Environment environment;

    public TimeRollingFileInstaller(Environment environment) {
        this.environment = environment;
    }

    boolean hasInstalled(LoggerContext loggerContext) {
        return null != loggerContext.getProperty(TimeRollingFileInstaller.class.getName());
    }

    void markInstalled(LoggerContext loggerContext) {
        loggerContext.putProperty(TimeRollingFileInstaller.class.getName(), "true");
    }

    public void install() {
        RollingFileConf conf = this.getRollingProperties(this.environment);
        if (conf == null) {
            return;
        }

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        synchronized (loggerContext.getConfigurationLock()) {
            if (this.hasInstalled(loggerContext)) {
                return;
            }

            Appender appender = this.createTimeRollingFileAppender(loggerContext, conf);
            Logger logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
            logger.addAppender(appender);

            this.markInstalled(loggerContext);
        }
    }

    private Appender createTimeRollingFileAppender(LoggerContext ctx, RollingFileConf conf) {

        RollingFileAppender fileAppender = new RollingFileAppender();

        TimeBasedRollingPolicy rollingPolicy = this.populateRollingPolicy(ctx, conf, fileAppender);
        PatternLayoutEncoder encoder = this.populatePatternLayoutEncoder(ctx, conf);
        ThresholdFilter filter = this.populateThresholdFilter(ctx, conf);
        this.populateRollingFileAppender(ctx, fileAppender, rollingPolicy, encoder, filter);

        return fileAppender;
    }

    private void populateRollingFileAppender(
            LoggerContext ctx,
            RollingFileAppender fileAppender,
            TimeBasedRollingPolicy rollingPolicy,
            PatternLayoutEncoder encoder,
            ThresholdFilter filter) {
        fileAppender.setContext(ctx);
        fileAppender.setRollingPolicy(rollingPolicy);
        fileAppender.setEncoder(encoder);
        fileAppender.addFilter(filter);
        fileAppender.setPrudent(true);
        fileAppender.start();
    }

    private ThresholdFilter populateThresholdFilter(LoggerContext ctx, RollingFileConf conf) {
        ThresholdFilter filter = new ThresholdFilter();
        filter.setContext(ctx);
        filter.setLevel(conf.getThresholdLevel());
        filter.start();
        return filter;
    }

    private PatternLayoutEncoder populatePatternLayoutEncoder(LoggerContext ctx, RollingFileConf conf) {
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(ctx);
        encoder.setPattern(conf.getContentPattern());
        encoder.start();
        return encoder;
    }

    private TimeBasedRollingPolicy populateRollingPolicy(LoggerContext ctx, RollingFileConf conf, RollingFileAppender fileAppender) {
        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
        rollingPolicy.setContext(ctx);
        rollingPolicy.setFileNamePattern(conf.getFileNamePattern());
        rollingPolicy.setMaxHistory(conf.getMaxHistory());
        rollingPolicy.setTotalSizeCap(FileSize.valueOf(conf.getTotalSize()));
        rollingPolicy.setParent(fileAppender);
        rollingPolicy.start();
        return rollingPolicy;
    }

    private RollingFileConf getRollingProperties(Environment environment) {
        RelaxedPropertyResolver propertyResolver = RelaxedPropertyResolver
                .ignoringUnresolvableNestedPlaceholders(environment, RollingFileConf.LOGGING_PREFIX);
        String fileNamePattern = propertyResolver.getProperty(RollingFileConf.FILE_NAME_KEY);
        if (null == fileNamePattern || fileNamePattern.isEmpty()) {
            return null;
        }

        RollingFileConf properties = new RollingFileConf();
        properties.setFileNamePattern(fileNamePattern);
        String contentPattern = propertyResolver.getProperty(RollingFileConf.PATTERN_KEY);
        if (contentPattern != null) {
            properties.setContentPattern(contentPattern);
        }

        Integer maxHistory = propertyResolver.getProperty(RollingFileConf.MAX_HISTORY_KEY, Integer.class);
        if (maxHistory != null) {
            properties.setMaxHistory(maxHistory);
        }

        String thresholdLevel = propertyResolver.getProperty(RollingFileConf.THRESHOLD_LEVEL_KEY);
        if (null != thresholdLevel && !thresholdLevel.isEmpty()) {
            properties.setThresholdLevel(thresholdLevel.toUpperCase());
        }

        String totalSize = propertyResolver.getProperty(RollingFileConf.TOTAL_FILE_SIZE_KEY);
        if (StringUtils.hasText(totalSize)) {
            properties.setTotalSize(totalSize);
        }

        return properties;
    }

    private static class RollingFileConf {

        public static String LOGGING_PREFIX = "logging.";
        public static String PATTERN_KEY = "pattern.time-rolling-file";

        public static String FILE_NAME_KEY = "time-rolling-file.file";

        public static String MAX_HISTORY_KEY = "time-rolling-file.max-history";
        public static String TOTAL_FILE_SIZE_KEY = "time-rolling-file.total-size";

        public static String THRESHOLD_LEVEL_KEY = "time-rolling-file.threshold-level";
        public static String CONTENT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} [${spring.application.name}] [%X{traceId}] [%thread] %-5level %logger %msg%n %ex{10}";

        private String fileNamePattern;

        private String contentPattern = CONTENT_PATTERN;

        private String thresholdLevel = "WARN";

        private Integer maxHistory = 90;

        // 单位: G
        private String totalSize = "4GB";

        public String getFileNamePattern() {
            return fileNamePattern;
        }

        public void setFileNamePattern(String fileNamePattern) {
            this.fileNamePattern = fileNamePattern;
        }

        public String getContentPattern() {
            return contentPattern;
        }

        public void setContentPattern(String contentPattern) {
            this.contentPattern = contentPattern;
        }

        public String getThresholdLevel() {
            return thresholdLevel;
        }

        public void setThresholdLevel(String thresholdLevel) {
            this.thresholdLevel = thresholdLevel;
        }

        public Integer getMaxHistory() {
            return maxHistory;
        }

        public void setMaxHistory(Integer maxHistory) {
            this.maxHistory = maxHistory;
        }

        public String getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(String totalSize) {
            this.totalSize = totalSize;
        }
    }
}

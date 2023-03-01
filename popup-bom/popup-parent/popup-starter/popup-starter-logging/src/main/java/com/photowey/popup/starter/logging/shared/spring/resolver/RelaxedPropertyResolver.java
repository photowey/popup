/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.photowey.popup.starter.logging.shared.spring.resolver;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * {@link PropertyResolver} that attempts to resolve values using {@link RelaxedNames}.
 *
 * @author Phillip Webb
 * @see RelaxedNames
 */
public class RelaxedPropertyResolver implements PropertyResolver {

    private final PropertyResolver resolver;

    private final String prefix;

    public RelaxedPropertyResolver(PropertyResolver resolver) {
        this(resolver, null);
    }

    public RelaxedPropertyResolver(PropertyResolver resolver, String prefix) {
        Assert.notNull(resolver, "PropertyResolver must not be null");
        this.resolver = resolver;
        this.prefix = (prefix != null ? prefix : "");
    }

    @Override
    public String getRequiredProperty(String key) throws IllegalStateException {
        return getRequiredProperty(key, String.class);
    }

    @Override
    public <T> T getRequiredProperty(String key, Class<T> targetType)
            throws IllegalStateException {
        T value = getProperty(key, targetType);
        Assert.state(value != null, String.format("required key [%s] not found", key));
        return value;
    }

    @Override
    public String getProperty(String key) {
        return getProperty(key, String.class, null);
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return getProperty(key, String.class, defaultValue);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType) {
        return getProperty(key, targetType, null);
    }

    @Override
    public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        RelaxedNames prefixes = new RelaxedNames(this.prefix);
        RelaxedNames keys = new RelaxedNames(key);
        for (String prefix : prefixes) {
            for (String relaxedKey : keys) {
                if (this.resolver.containsProperty(prefix + relaxedKey)) {
                    return this.resolver.getProperty(prefix + relaxedKey, targetType);
                }
            }
        }
        return defaultValue;
    }

    @Override
    public boolean containsProperty(String key) {
        RelaxedNames prefixes = new RelaxedNames(this.prefix);
        RelaxedNames keys = new RelaxedNames(key);
        for (String prefix : prefixes) {
            for (String relaxedKey : keys) {
                if (this.resolver.containsProperty(prefix + relaxedKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String resolvePlaceholders(String text) {
        throw new UnsupportedOperationException(
                "Unable to resolve placeholders with relaxed properties");
    }

    @Override
    public String resolveRequiredPlaceholders(String text)
            throws IllegalArgumentException {
        throw new UnsupportedOperationException(
                "Unable to resolve placeholders with relaxed properties");
    }

    /**
     * Return a Map of all values from all underlying properties that start with the
     * specified key. NOTE: this method can only be used if the underlying resolver is a
     * {@link ConfigurableEnvironment}.
     *
     * @param keyPrefix the key prefix used to filter results
     * @return a map of all sub properties starting with the specified key prefix.
     * @see PropertySourceUtils#getSubProperties
     */
    public Map<String, Object> getSubProperties(String keyPrefix) {
        Assert.isInstanceOf(ConfigurableEnvironment.class, this.resolver,
                "SubProperties not available.");
        ConfigurableEnvironment env = (ConfigurableEnvironment) this.resolver;
        return PropertySourceUtils.getSubProperties(env.getPropertySources(), this.prefix,
                keyPrefix);
    }

    /**
     * Return a property resolver for the environment, preferring one that ignores
     * unresolvable nested placeholders.
     *
     * @param environment the source environment
     * @param prefix      the prefix
     * @return a property resolver for the environment
     * @since 1.4.3
     */
    public static RelaxedPropertyResolver ignoringUnresolvableNestedPlaceholders(
            Environment environment, String prefix) {
        Assert.notNull(environment, "Environment must not be null");
        PropertyResolver resolver = environment;
        if (environment instanceof ConfigurableEnvironment) {
            resolver = new PropertySourcesPropertyResolver(
                    ((ConfigurableEnvironment) environment).getPropertySources());
            ((PropertySourcesPropertyResolver) resolver)
                    .setIgnoreUnresolvableNestedPlaceholders(true);
        }
        return new RelaxedPropertyResolver(resolver, prefix);
    }

}

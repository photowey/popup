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
package com.photowey.popup.spring.cloud.core.trimmer.factory;

import com.photowey.popup.spring.cloud.core.trimmer.annotation.TrimSpace;
import com.photowey.popup.spring.cloud.core.trimmer.parser.StringSpaceTrimmer;
import org.springframework.context.support.EmbeddedValueResolutionSupport;
import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.HashSet;
import java.util.Set;

/**
 * {@code TrimSpaceAnnotationFormatterFactory}
 *
 * @author photowey
 * @date 2023/07/07
 * @since 1.0.0
 */
public class TrimSpaceAnnotationFormatterFactory
        extends EmbeddedValueResolutionSupport implements AnnotationFormatterFactory<TrimSpace> {

    private final StringSpaceTrimmer trimmer = new StringSpaceTrimmer();

    @Override
    public Set<Class<?>> getFieldTypes() {
        return this.newHashSet(String.class);
    }

    @Override
    public Printer<?> getPrinter(TrimSpace annotation, Class<?> fieldType) {
        return trimmer;
    }

    @Override
    public Parser<?> getParser(TrimSpace annotation, Class<?> fieldType) {
        return trimmer;
    }

    private Set<Class<?>> newHashSet(Class<?> clazz) {
        Set<Class<?>> hashSet = new HashSet<>(1);
        hashSet.add(clazz);

        return hashSet;
    }
}

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
package com.photowey.popup.spring.cloud.core.config.trimmer;

import com.photowey.popup.spring.cloud.core.trimmer.factory.TrimSpaceAnnotationFormatterFactory;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * {@code SpaceTrimmerConfigure}
 *
 * @author photowey
 * @date 2023/07/07
 * @since 1.0.0
 */
public class SpaceTrimmerConfigure implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldAnnotation(this.spaceTrimmerFactory());
    }

    public TrimSpaceAnnotationFormatterFactory spaceTrimmerFactory() {
        return new TrimSpaceAnnotationFormatterFactory();
    }

}

/*
 * Copyright © 2022 the original author or authors (photowey@gmail.com)
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
package com.photowey.popup.starter.reader.config;

import com.photowey.popup.starter.reader.classpath.ClasspathReader;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * {@code ClasspathReaderAutoConfigure}
 *
 * @author photowey
 * @date 2022/12/29
 * @since 1.0.0
 */
@AutoConfiguration
public class ClasspathReaderAutoConfigure {

    @Bean
    public ClasspathReader classpathReader() {
        return new ClasspathReader();
    }
}
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
package com.photowey.popup.starter.reader.classpath;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * {@code ClasspathReader}
 *
 * @author photowey
 * @date 2022/12/29
 * @since 1.0.0
 */
public class ClasspathReader implements ResourceLoaderAware {

    private static final String CLASSPATH = "classpath";
    private static final String CLASSPATH_SEPARATOR = ":";

    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource loaderRead(String location) {
        if (location.startsWith(CLASSPATH)) {
            return this.resourceLoader.getResource(location);
        }

        return this.resourceLoader.getResource(CLASSPATH + CLASSPATH_SEPARATOR + location);
    }

    public Resource classpathRead(String location) {
        if (location.startsWith(CLASSPATH)) {
            location = location.replaceAll(CLASSPATH + CLASSPATH_SEPARATOR, "");
        }

        return new ClassPathResource(location);
    }

    public InputStream read(String location) throws IOException {
        Resource resource = this.loaderRead(location);
        if (resource.isReadable()) {
            return resource.getInputStream();
        }

        return null;
    }

    public String loaderReads(String location) throws IOException {
        try (InputStream input = this.read(location);) {
            if (input != null) {
                return this.toStr(input);
            }
        }

        return null;
    }

    public String loaderReads(Resource resource) throws IOException {
        if (resource.isReadable()) {
            try (InputStream input = resource.getInputStream()) {
                return this.loaderReads(input);
            }
        }

        return null;
    }

    public String loaderReads(InputStream input) throws IOException {
        return this.toStr(input);
    }

    public String toStr(InputStream input) throws IOException {
        if (null != input) {
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);
            List<String> data = new ArrayList<>();
            String line = null;
            while ((line = br.readLine()) != null) {
                data.add(line);
            }

            br.close();
            isr.close();
            input.close();

            return data
                    .stream()
                    //.map(each -> each + System.lineSeparator())
                    .collect(Collectors.joining(System.lineSeparator()));
        }

        return null;
    }

}

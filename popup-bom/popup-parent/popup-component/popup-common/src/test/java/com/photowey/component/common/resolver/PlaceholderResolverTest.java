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
package com.photowey.component.common.resolver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code PlaceholderResolverTest}
 *
 * @author photowey
 * @date 2023/12/07
 * @since 1.0.0
 */
class PlaceholderResolverTest {

    @Test
    void testResolve() {
        String repo = "https://github.com/photowey/popup";
        Map<String, Object> context = populateContext();

        String url = "https://github.com/{{username}}/{{repo.project}}";
        String target = PlaceholderResolver.resolve(url, context);
        Assertions.assertEquals(repo, target);

        String $url = "https://github.com/${username}/${repo.project}";
        String $target = PlaceholderResolver.$resolve($url, context);
        Assertions.assertEquals(repo, $target);
    }

    public static Map<String, Object> populateContext() {
        Map<String, Object> repo = new HashMap<>(4);
        repo.put("project", "popup");
        repo.put("developer", "photowey");
        repo.put("email", "photowey@gmail.com");

        Map<String, Object> context = new HashMap<>(4);
        context.put("username", "photowey");
        context.put("repo", repo);

        return context;
    }

}
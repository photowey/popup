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
package com.photowey.component.common.generator;

import com.photowey.component.common.nanoid.NanoidUtils;

/**
 * {@code RandomGenerator}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public class RandomGenerator implements Generator {

    @Override
    public String generate(String prefix) {
        return prefix + NanoidUtils.randomLowerNanoId();
    }

    @Override
    public String generate(String prefix, int size) {
        return prefix + NanoidUtils.randomLowerNanoId(size);
    }

    @Override
    public String generate(char[] alphabet, int size) {
        return NanoidUtils.randomLowerNanoId(alphabet, size);
    }

    @Override
    public String generate(String prefix, char[] alphabet, int size) {
        return prefix + NanoidUtils.randomLowerNanoId(alphabet, size);
    }
}

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
package com.photowey.popup.spring.cloud.core.engine;

import com.photowey.popup.spring.cloud.core.getter.ApplicationContextGetter;
import com.photowey.popup.spring.cloud.core.getter.EnvironmentGetter;
import com.photowey.popup.spring.cloud.core.getter.ListableBeanFactoryGetter;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;

/**
 * {@code Engine}
 *
 * @author photowey
 * @date 2023/05/06
 * @since 1.0.0
 */
public interface Engine extends BeanFactoryAware, ApplicationContextAware, EnvironmentAware,
        ListableBeanFactoryGetter, ApplicationContextGetter, EnvironmentGetter {

}

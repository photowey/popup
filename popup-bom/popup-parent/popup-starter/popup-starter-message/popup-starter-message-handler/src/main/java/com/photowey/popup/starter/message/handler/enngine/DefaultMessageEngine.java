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
package com.photowey.popup.starter.message.handler.enngine;

import com.photowey.popup.spring.cloud.core.engine.AbstractEngine;
import com.photowey.popup.starter.message.handler.sender.MessageSenderManager;

/**
 * {@code DefaultMessageEngine}
 *
 * @author photowey
 * @date 2023/10/10
 * @since 1.0.0
 */
public class DefaultMessageEngine extends AbstractEngine implements MessageEngine {

    @Override
    public MessageSenderManager messageSenderManager() {
        return this.beanFactory.getBean(MessageSenderManager.class);
    }
}

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
package com.photowey.component.test.delayed.queue.app.component.listener;

import com.photowey.component.queue.delayed.listener.DelayedQueueListener;
import com.photowey.component.test.delayed.queue.app.component.event.TestEvent1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * {@code TestEventListener1}
 *
 * @author photowey
 * @date 2023/01/29
 * @since 1.0.0
 */
@Slf4j
@Component
public class TestEventListener1 implements DelayedQueueListener<TestEvent1> {

    @Override
    public String getTopic() {
        return "delayed.topic.test.event1";
    }

    @Override
    public void onEvent(TestEvent1 event) {
        log.info("TestEventListener1:handle event1: [{}], event.id: [{}];event.topic: [{}]", event.getClass().getName(), event.getId(), event.getTopic());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 10;
    }
}

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
package com.photowey.component.test.delayed.queue.app.listener;

import com.photowey.component.common.date.DateUtils;
import com.photowey.component.common.util.BlockUtils;
import com.photowey.component.queue.delayed.publisher.DelayedQueueEventPublisher;
import com.photowey.component.test.base.TestBase;
import com.photowey.component.test.delayed.queue.app.App;
import com.photowey.component.test.delayed.queue.app.component.event.EventData;
import com.photowey.component.test.delayed.queue.app.component.event.TestEvent1;
import com.photowey.component.test.delayed.queue.app.component.event.TestEvent2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * {@code TestEventListenerTest}
 *
 * @author photowey
 * @date 2023/01/29
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest(classes = App.class)
class TestEventListenerTest extends TestBase {

    @Autowired
    private DelayedQueueEventPublisher publisher;

    @Test
    void testHealthz() throws Exception {
        this.doHealthz();
    }

    @Test
    void testEventListener_event1() {
        TestEvent1 event = new TestEvent1();
        event.setId("8848");
        event.setTopic("delayed.topic.test.event1");
        event.setEnqueueTimes(1);

        LocalDateTime now = LocalDateTime.now();
        event.setRunAt(now.plusSeconds(3).toInstant(ZoneOffset.ofHours(8)).toEpochMilli());

        EventData data = new EventData();
        data.setId(9527L);
        event.setData(data);

        this.publisher.publishEvent(event);
        BlockUtils.block(5_000);

        // Output:
        // com.photowey.component.test.delayed.queue.app.component.listener.TestEventListener1
        // TestEventListener1:handle event1: [com.photowey.component.test.delayed.queue.app.component.event.TestEvent1],
        // event.id: [8848];event.topic: [delayed.topic.test.event1]
    }

    @Test
    void testEventListener_event2() {
        TestEvent2 event = new TestEvent2();
        event.setId("9527");
        event.setTopic("delayed.topic.test.event2");
        event.setEnqueueTimes(1);

        LocalDateTime now = LocalDateTime.now();
        event.setRunAt(now.plusSeconds(3).toInstant(ZoneOffset.ofHours(8)).toEpochMilli());

        EventData data = new EventData();
        data.setId(8848L);
        event.setData(data);

        this.publisher.publishEvent(event);
        BlockUtils.block(5_000);

        // Output:
        //  com.photowey.component.test.delayed.queue.app.component.listener.TestEventListener3
        //  TestEventListener3:handle event2: [com.photowey.component.test.delayed.queue.app.component.event.TestEvent2],
        //  event.id: [9527];event.topic: [delayed.topic.test.event2]

        // com.photowey.component.test.delayed.queue.app.component.listener.TestEventListener2
        // TestEventListener2:handle event2: [com.photowey.component.test.delayed.queue.app.component.event.TestEvent2],
        // event.id: [9527];event.topic: [delayed.topic.test.event2]
    }

    @Test
    void testEventListener_event2_topic_ext() {
        TestEvent2 event = new TestEvent2();
        event.setId("9527");
        event.setTopic("delayed.topic.test.event2.topic.ext");
        event.setEnqueueTimes(1);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime runAt = now.plusSeconds(5);
        event.setRunAt(runAt.toInstant(ZoneOffset.ofHours(8)).toEpochMilli());

        EventData data = new EventData();
        data.setId(8848L);
        event.setData(data);

        log.info("pre publish event:{}", DateUtils.nowStr());
        this.publisher.publishEvent(event);
        BlockUtils.block(8_000);

        // Output:
        // 2023-01-30 21:43:34.854 xxx pre publish event:2023-01-30 21:43:34

        // 2023-01-30 21:43:39.857 xxx TestEventListener4:handle event2: [com.photowey.component.test.delayed.queue.app.component.event.TestEvent2],
        // event.id: [9527];event.topic: [delayed.topic.test.event2.topic.ext]
    }
}

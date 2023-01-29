package com.photowey.component.test.delayed.queue.app.component.listener;

import com.photowey.component.queue.delayed.listener.DelayedQueueListener;
import com.photowey.component.test.delayed.queue.app.component.event.TestEvent2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * {@code TestEventListener2}
 *
 * @author photowey
 * @date 2023/01/29
 * @since 1.0.0
 */
@Slf4j
@Component
public class TestEventListener3 implements DelayedQueueListener<TestEvent2> {

    @Override
    public String getTopic() {
        return "delayed.topic.test.event2";
    }

    @Override
    public void onEvent(TestEvent2 event) {
        log.info("TestEventListener3:handle event2: [{}], event.id: [{}];event.topic: [{}]", event.getClass().getName(), event.getId(), event.getTopic());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 5;
    }
}

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

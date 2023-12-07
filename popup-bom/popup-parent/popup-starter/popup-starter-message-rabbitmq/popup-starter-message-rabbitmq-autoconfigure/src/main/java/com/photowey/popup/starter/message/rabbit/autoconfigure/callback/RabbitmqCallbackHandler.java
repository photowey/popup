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
package com.photowey.popup.starter.message.rabbit.autoconfigure.callback;

import com.photowey.component.common.util.ObjectUtils;
import com.photowey.popup.starter.message.rabbitmq.core.message.MessageRabbitmq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.nio.charset.StandardCharsets;

/**
 * {@code RabbitmqCallbackHandler}
 *
 * @author photowey
 * @date 2023/10/25
 * @since 1.0.0
 */
@Slf4j
public class RabbitmqCallbackHandler implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    @Autowired
    private RabbitProperties rabbitProperties;

    @Override
    public void returnedMessage(ReturnedMessage returned) {
        if (null != returned.getMessage()) {
            MessageRabbitmq message = this.toReturnedMessageRabbit(returned);
            this.save(message);
        }
    }

    @Override
    public void confirm(CorrelationData data, boolean ack, String cause) {
        this.report(data, ack, cause);
        if (null != data) {
            MessageRabbitmq message = this.toConfirmMessageRabbit(data, ack, cause);
            this.save(message);
        }
    }

    private void save(MessageRabbitmq message) {
        // TODO
    }

    private void report(CorrelationData data, boolean ack, String cause) {
        if (ack) {
            String dataId = null != data ? data.getId() : "-";
            log.info("message.rabbit: message.ack.callback.successfully, data.id:[{}]", dataId);

            return;
        }

        String dataId = null != data ? data.getId() : "-";
        ReturnedMessage returned = null != data ? data.getReturned() : null;
        String body = null != returned ? this.toBodyString(returned.getMessage()) : "-";
        log.error("message.rabbit: message.ack.callback.failed, data.id:[{},{}], cause is:{}", dataId, body, cause);
    }

    private MessageRabbitmq toConfirmMessageRabbit(CorrelationData data, boolean ack, String cause) {
        // TODO
        return MessageRabbitmq.builder().build();
    }

    private MessageRabbitmq toReturnedMessageRabbit(ReturnedMessage returned) {
        // TODO
        return MessageRabbitmq.builder().build();
    }

    private String toBodyString(Message message) {
        if (ObjectUtils.isNullOrEmpty(message)) {
            return "-";
        }

        return new String(message.getBody(), StandardCharsets.UTF_8);
    }
}

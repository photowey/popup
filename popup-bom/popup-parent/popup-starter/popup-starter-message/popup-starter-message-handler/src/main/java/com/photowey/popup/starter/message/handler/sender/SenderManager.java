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
package com.photowey.popup.starter.message.handler.sender;

/**
 * {@code SenderManager}
 *
 * @author photowey
 * @date 2023/09/23
 * @since 1.0.0
 */
public interface SenderManager {

    String RABBIT_MESSAGE_SENDER_NAME = "Rabbit";
    String RABBIT_DELAYED_MESSAGE_SENDER_NAME = "Rabbit.delayed";

    String KAFKA_MESSAGE_SENDER_NAME = "Kafka";
    String KAFKA_DELAYED_MESSAGE_SENDER_NAME = "Kafka.delayed";

    String ROCKET_MESSAGE_SENDER_NAME = "Rocket";
    String ROCKET_DELAYED_MESSAGE_SENDER_NAME = "Rocket.delayed";

    static String rabbit() {
        return RABBIT_MESSAGE_SENDER_NAME;
    }

    static String delayedRabbit() {
        return RABBIT_DELAYED_MESSAGE_SENDER_NAME;
    }

    static String kafka() {
        return KAFKA_MESSAGE_SENDER_NAME;
    }

    static String delayedKafka() {
        return KAFKA_DELAYED_MESSAGE_SENDER_NAME;
    }

    static String rocket() {
        return ROCKET_MESSAGE_SENDER_NAME;
    }

    static String delayedRocket() {
        return ROCKET_DELAYED_MESSAGE_SENDER_NAME;
    }
}

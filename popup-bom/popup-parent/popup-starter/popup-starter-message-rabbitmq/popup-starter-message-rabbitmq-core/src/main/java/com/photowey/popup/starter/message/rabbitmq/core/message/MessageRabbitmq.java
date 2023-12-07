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
package com.photowey.popup.starter.message.rabbitmq.core.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * {@code MessageRabbitmq}
 *
 * @author photowey
 * @date 2023/10/21
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRabbitmq implements Serializable {

    private static final long serialVersionUID = -1429473322667862201L;

    private Long id;
    private Long shardingId;
    private Integer deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String brokerHost;
    private Integer brokerPort;

    private String brokerVhost;

    private String biz;
    private String messageId;

    private String exchange;
    private String routingKey;

    private String messageType;
    private String body;

    private Integer confirmed;
    private String confirmCause;
    private Integer returned;
    private Integer replyCode;
    private String replyText;
    private Integer acked;
}

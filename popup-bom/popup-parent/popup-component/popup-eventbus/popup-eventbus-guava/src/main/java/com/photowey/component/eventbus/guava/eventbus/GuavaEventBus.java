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
package com.photowey.component.eventbus.guava.eventbus;

import com.photowey.component.eventbus.guava.event.Event;
import com.photowey.component.eventbus.guava.subscriber.Subscriber;

/**
 * {@code GuavaEventBus}
 *
 * @author photowey
 * @date 2023/01/09
 * @since 1.0.0
 */
public class GuavaEventBus implements EventBus {

    public <EVENT, T extends Subscriber<EVENT>> void register(T subscriber) {

    }

    public <EVENT, T extends Subscriber<EVENT>> void unregister(T subscriber) {

    }

    public <T extends Event> void post(T event) {

    }
}

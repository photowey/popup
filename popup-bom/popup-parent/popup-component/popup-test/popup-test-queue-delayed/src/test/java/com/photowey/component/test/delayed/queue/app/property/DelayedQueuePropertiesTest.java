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
package com.photowey.component.test.delayed.queue.app.property;

import com.photowey.component.common.formatter.StringFormatter;
import com.photowey.component.test.base.TestBase;
import com.photowey.component.test.delayed.queue.app.App;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

/**
 * {@code DelayedQueuePropertiesTest}
 *
 * @author photowey
 * @date 2023/01/20
 * @since 1.0.0
 */
@SpringBootTest(classes = App.class)
class DelayedQueuePropertiesTest extends TestBase {

    @Autowired
    private DelayedQueueProperties delayedQueueProperties;

    @Autowired
    private Environment environment;

    @Test
    void testHealthz() throws Exception {
        this.doHealthz();
    }

    @Test
    void testDelayedQueuePropertiesHealth() {
        this.testDelayedQueuePropertiesHealthApi();
    }

    private void testDelayedQueuePropertiesHealthApi() {
        Assertions.assertNotNull(this.delayedQueueProperties.getHealth());
        Assertions.assertNotNull(this.delayedQueueProperties.getHealth().getApi());

        String port = this.environment.getProperty("server.port", "11001");
        String expectedApi = StringFormatter.format("http://127.0.0.1:11001/healthz", port);
        Assertions.assertEquals(expectedApi, this.delayedQueueProperties.getHealth().getApi());
    }
}



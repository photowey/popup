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
package com.photowey.component.test.cloud.core.app.binder;

import com.photowey.component.test.base.TestBase;
import com.photowey.component.test.cloud.core.app.App;
import com.photowey.popup.spring.cloud.core.binder.PropertyBinders;
import com.photowey.popup.spring.cloud.core.factory.YamlPropertySourceFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

/**
 * {@code PropertyBindersTest}
 *
 * @author photowey
 * @date 2023/02/09
 * @since 1.0.0
 */
@SpringBootTest(classes = {App.class, TestBase.Injector.class, PropertyBindersTest.Configure.class})
class PropertyBindersTest extends TestBase {

    @Configuration
    @PropertySource(value = "classpath:/popup-dev.yml", factory = YamlPropertySourceFactory.class)
    static class Configure {

    }

    @Test
    void testBind() {
        Map<String, Object> ctx = new HashMap<>(8);
        ctx.put("app.popup.name", "cloud-core-tester");
        ctx.put("app.popup.profiles.active[0]", "dev");
        ctx.put("app.popup.profiles.active[1]", "webapp");
        PopupYamlProperties properties = PropertyBinders.bind(ctx, "app.popup", PopupYamlProperties.class);

        Assertions.assertNotNull(properties);
        Assertions.assertEquals("cloud-core-tester", properties.getName());
        Assertions.assertEquals("dev", properties.getProfiles().getActive().get(0));
        Assertions.assertEquals("webapp", properties.getProfiles().getActive().get(1));
    }

    @Test
    void testMapBind_instance() {
        Map<String, Object> ctx = new HashMap<>(8);
        ctx.put("app.popup.profiles.active[0]", "dev");
        ctx.put("app.popup.profiles.active[1]", "webapp");

        PopupYamlProperties properties = new PopupYamlProperties();
        properties.setName("cloud-core-tester");

        PropertyBinders.bind(ctx, "app.popup", properties);

        Assertions.assertNotNull(properties);
        Assertions.assertEquals("cloud-core-tester", properties.getName());
        Assertions.assertEquals("dev", properties.getProfiles().getActive().get(0));
        Assertions.assertEquals("webapp", properties.getProfiles().getActive().get(1));
    }

    @Test
    void testEnvironmentBind() {
        Environment environment = this.applicationContext.getEnvironment();
        PopupYamlProperties properties = PropertyBinders.bind(environment, "app.popup", PopupYamlProperties.class);

        Assertions.assertNotNull(properties);
        Assertions.assertEquals("cloud-core-tester", properties.getName());
        Assertions.assertEquals("dev", properties.getProfiles().getActive().get(0));
        Assertions.assertEquals("webapp", properties.getProfiles().getActive().get(1));
    }

}

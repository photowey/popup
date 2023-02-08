package com.photowey.component.test.cloud.core.app;

import com.photowey.component.test.base.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * {@code AppTests}
 *
 * @author photowey
 * @date 2023/02/08
 * @since 1.0.0
 */
@SpringBootTest(classes = App.class)
class AppTests extends TestBase {

    @Test
    void contextLoads() {
    }

    @Test
    void testHealthz() throws Exception {
        this.doHealthz();
    }
}
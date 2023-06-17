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
package com.photowey.component.test.cloud.core.app.async;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * {@code AsyncTest}
 *
 * @author photowey
 * @date 2023/06/17
 * @since 1.0.0
 */
@Slf4j
class AsyncTest {

    private ExecutorService executorService;

    @BeforeEach
    void init() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Test
    void testAsync() throws Throwable {
        CompletableFuture<Long> fx = createCompletableFuture();

        AsyncMessage message = AsyncMessage.builder()
                .id(System.currentTimeMillis())
                .message("Hello world.")
                .fx(fx)
                .build();

        log.info("------------------------------ step: 1");

        this.executorService.execute(() -> {
            this.send(message);
        });

        log.info("------------------------------ step: 2");

        fx.whenComplete((rvt, ex) -> {
            log.info("------------------------------ step: 3, rvt:[{}]", rvt);
        });

        log.info("------------------------------ step: 4");
        fx.join();
        log.info("------------------------------ step: 5, rvt:[{}]", message.getRvt());

        TimeUnit.SECONDS.sleep(3);

        // ------------------------------ step: 1
        // ------------------------------ step: 2
        // ------------------------------ step: 6
        // ------------------------------ step: 4

        // ------------------------------ step: 7
        // ------------------------------ step: 5, rvt:[1687015181067]
        // ------------------------------ step: 3, rvt:[1687015181067]
        // ------------------------------ step: 8
        // ------------------------------ step: 9

        // ...
        // ------------------------------ step: 7
        // ------------------------------ step: 3, rvt:[1687015631466]
        // ------------------------------ step: 8
        // ------------------------------ step: 5, rvt:[1687015631466]
        // ------------------------------ step: 9
    }

    private void send(AsyncMessage message) {
        log.info("------------------------------ step: 6");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        log.info("------------------------------ step: 7");
        long rvt = System.currentTimeMillis();
        message.setRvt(rvt);
        message.getFx().complete(message.getRvt());
        log.info("------------------------------ step: 8");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        log.info("------------------------------ step: 9");
    }

    private <T> CompletableFuture<T> createCompletableFuture() {
        return new CompletableFuture<>();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class AsyncMessage implements Serializable {

        private static final long serialVersionUID = 2046666020495870634L;

        private Long id;
        private String message;
        private Long rvt;
        private CompletableFuture<Long> fx;
    }
}

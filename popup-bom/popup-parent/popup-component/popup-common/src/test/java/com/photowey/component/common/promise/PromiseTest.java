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
package com.photowey.component.common.promise;

import com.photowey.component.common.util.ObjectUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@code PromiseTest}
 *
 * @author photowey
 * @date 2023/01/05
 * @since 1.0.0
 */
@Slf4j
class PromiseTest {

    @Test
    void testSuccess() throws Throwable {
        Integer result = Promise.run(() -> {
            String message = "OK";
            if (ObjectUtils.isNotNullOrEmpty(message)) {
                return message;
            } else {
                throw new RuntimeException("failure");
            }
        }).then(v -> {
            return Promise.success(1);
        }).ifSuccess(v -> {
            System.out.println("Success" + v);
        }).ifFailure((e) -> {
            System.out.println(e.getMessage());
        }).get();

        Assertions.assertEquals(1, result);
    }

    @Test
    void testSuccess_predicate() throws Throwable {
        Integer result = Promise.run(() -> {
            return "OK";
        }, (v) -> {
            return ObjectUtils.isNotNullOrEmpty(v);
        }).then(v -> {
            return Promise.success(1);
        }).ifSuccess(v -> {
            System.out.println("Success" + v);
        }).ifFailure((e) -> {
            System.out.println(e.getMessage());
        }).get();

        Assertions.assertEquals(1, result);
    }

    @Test
    void testFailure() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            Promise.run(() -> {
                String message = "";
                if (ObjectUtils.isNotNullOrEmpty(message)) {
                    return message;
                } else {
                    throw new RuntimeException("failure");
                }
            }).then(v -> {
                return Promise.success(1);
            }).ifSuccess(v -> {
                System.out.println("Success" + v);
            }).ifFailure((e) -> {
                System.out.println("Failure" + ":" + e.getMessage());
            }).get();
        });
    }

    @Test
    void testFailure_not_throw_v1() throws Throwable {
        Integer failure = Promise.run(() -> {
            return "";
        }, (v) -> {
            return ObjectUtils.isNotNullOrEmpty(v);
        }).then(v -> {
            return Promise.success(1);
        }).quiet((e) -> {
            if (e instanceof Promise.EmptyException) {
                return false;
            }
            return true;
        }).ifSuccess(v -> {
            System.out.println("Success" + v);
        }).ifFailure((e) -> {
            System.out.println("Failure" + ":" + e.getMessage());
        }).get();

        Assertions.assertNull(failure);
    }

    @Test
    void testFailure_not_throw_v2() throws Throwable {
        Integer failure = Promise.run(() -> {
            return "";
        }, (v) -> {
            return ObjectUtils.isNotNullOrEmpty(v);
        }).then(v -> {
            return Promise.success(1);
        }).throwable(e -> {
            return new IllegalArgumentException(e);
        }).quiet((e) -> {
            if (e instanceof IllegalArgumentException) {
                return false;
            }
            return true;
        }).ifSuccess(v -> {
            System.out.println("Success" + v);
        }).ifFailure((e) -> {
            System.out.println("Failure" + ":" + e.getMessage());
        }).get();

        Assertions.assertNull(failure);
    }

    @Test
    void testFailure_not_throw_v3() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            Promise.run(() -> {
                return "";
            }, (v) -> {
                return ObjectUtils.isNotNullOrEmpty(v);
            }).then(v -> {
                return Promise.success(1);
            }).throwable(e -> {
                return new NullPointerException("rethrow");
            }).quiet((e) -> {
                if (e instanceof IllegalArgumentException) {
                    return false;
                }
                return true;
            }).ifSuccess(v -> {
                System.out.println("Success" + v);
            }).ifFailure((e) -> {
                System.out.println("Failure" + ":" + e.getMessage());
            }).get();
        });
    }

    @Test
    void testResolveReject() throws Throwable {
        Integer result = Promise.run(() -> {
            return "";
        }, (v) -> {
            System.out.println("Success" + v);
        }, (e) -> {
            System.out.println("Failure" + ":" + e.getMessage());
        }).then(v -> {
            return Promise.success(1);
        }).ifSuccess(v -> {
            System.out.println("Success" + v);
        }).ifFailure((e) -> {
            System.out.println(e.getMessage());
        }).get();

        Assertions.assertEquals(1, result);
    }

    @Test
    void testResolveReject_v2() {
        Tester ctx = new Tester();
        Promise.run(() -> {
            return "ok";
        }, (v) -> {
            System.out.println("Success" + v);
            ctx.setResult("1");
        }, (e) -> {
            System.out.println("Failure" + ":" + e.getMessage());
            ctx.setResult("0");
        });

        Assertions.assertEquals("1", ctx.getResult());
    }

    @Test
    void testResolveReject_v3() {
        Tester ctx = new Tester();
        Promise.run(() -> {
            if (1 != 2) {
                throw new RuntimeException("failure");
            }
            return "ok";
        }, (v) -> {
            System.out.println("Success" + v);
            ctx.setResult("1");
        }, (e) -> {
            System.out.println("Failure" + ":" + e.getMessage());
            ctx.setResult("0");
        });

        Assertions.assertEquals("0", ctx.getResult());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Tester {
        private Object result;
    }
}

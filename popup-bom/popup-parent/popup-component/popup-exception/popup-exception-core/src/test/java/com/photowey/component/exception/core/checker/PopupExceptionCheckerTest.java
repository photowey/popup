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
package com.photowey.component.exception.core.checker;

import com.photowey.component.common.util.ObjectUtils;
import com.photowey.component.exception.core.model.PopupException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * {@code PopupExceptionCheckerTest}
 *
 * @author photowey
 * @date 2023/03/02
 * @since 1.0.0
 */
class PopupExceptionCheckerTest {

    private static final long FIXED = 1677767626401L;
    private static final long FIXED_DUMMY = FIXED + 1L;

    @Test
    void testCheckNotNull() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkNotNull(null, "Null");
        });
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkNotNull(new ArrayList<>(0), "Null");
        });

        List<Object> objects = new ArrayList<>(1);
        objects.add(1);

        PopupExceptionChecker.checkNotNull(new Dummy(FIXED), "Not null");
        PopupExceptionChecker.checkNotNull(objects, "Not null");
    }

    @Test
    void testCheckNull() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkNull(new Dummy(FIXED), "Not null");
        });
        Assertions.assertThrows(PopupException.class, () -> {
            List<Object> objects = new ArrayList<>(1);
            objects.add(1);
            PopupExceptionChecker.checkNull(objects, "Not null");
        });

        PopupExceptionChecker.checkNull(null, "Null");
        PopupExceptionChecker.checkNull(new ArrayList<>(0), "Null");
    }

    @Test
    void testCheckTrue() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkTrue(1 != 1, "True");
        });

        PopupExceptionChecker.checkTrue(1 == 1, "True");
        PopupExceptionChecker.checkTrue(1 != 2, "False");
    }

    @Test
    void testCheckFalse() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkFalse(1 == 1, "True");
        });

        PopupExceptionChecker.checkFalse(1 != 1, "True");
        PopupExceptionChecker.checkFalse(1 == 2, "False");
    }

    @Test
    void testCheckExistBoolean() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkExistB(ObjectUtils.isNotNullOrEmpty(null), "Not exist");
        });

        PopupExceptionChecker.checkExistB(ObjectUtils.isNotNullOrEmpty(new Dummy(FIXED)), "Exist");
    }

    @Test
    void testCheckExist() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkExist(null, "Not exist");
        });

        PopupExceptionChecker.checkExist(new Dummy(FIXED), "Exist");
    }

    @Test
    void testCheckNotExistBoolean() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkNotExistB(ObjectUtils.isNotNullOrEmpty(new Dummy(FIXED)), "Exist");
        });

        PopupExceptionChecker.checkNotExistB(ObjectUtils.isNotNullOrEmpty(null), "Not exist");
    }

    @Test
    void testCheckNotExist() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkNotExist(new Dummy(FIXED), "Exist");
        });

        PopupExceptionChecker.checkNotExist(null, "Not exist");
    }

    @Test
    void testCheckEq() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkEq(1, 2, "Not Eq");
        });
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkEq(FIXED, FIXED_DUMMY, "Not Eq");
        });

        PopupExceptionChecker.checkEq(1, 1, "Eq");
        PopupExceptionChecker.checkEq(FIXED, FIXED, "Eq");
    }

    @Test
    void testCheckNotEq() {
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkNotEq(1, 1, "Eq");
        });
        Assertions.assertThrows(PopupException.class, () -> {
            PopupExceptionChecker.checkNotEq(FIXED, FIXED, "Eq");
        });

        PopupExceptionChecker.checkNotEq(1, 2, "Not Eq");
        PopupExceptionChecker.checkNotEq(FIXED, FIXED_DUMMY, "Not Eq");
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class Dummy implements Serializable {
        private Long id;
    }

}
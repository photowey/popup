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
package com.photowey.popup.starter.autoconfigure.validator;

import com.photowey.component.exception.core.checker.PopupExceptionChecker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

/**
 * {@code EntityValidator}
 *
 * @author photowey
 * @date 2023/03/03
 * @since 1.0.0
 */
public class EntityValidator {

    private final Validator validator;

    public EntityValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> void validate(T t) {
        this.validates(t);
    }

    public <T> void validates(T... ts) {
        this.validates(Arrays.asList(ts));
    }

    public <T> void validates(Collection<T> ts, Class<?>... groups) {
        ts.forEach((t) -> {
            Set<ConstraintViolation<T>> violations = this.validator.validate(t, groups);
            if (!ObjectUtils.isEmpty(violations)) {
                StringBuilder buf = new StringBuilder();
                violations.forEach(v -> {
                    buf.append(v.getMessage()).append(";");
                });

                PopupExceptionChecker.throwException(buf.toString().replaceAll(";*$", ""));
            }
        });
    }
}

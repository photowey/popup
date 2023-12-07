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
package com.photowey.popup.starter.validator.entity;

import com.photowey.component.exception.core.checker.PopupExceptionChecker;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

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

    @SuppressWarnings("unchecked")
    public <T> void validate(T t) {
        this.validates(t);
    }

    @SuppressWarnings("unchecked")
    public <T> void validate(Consumer<String> fx, T t) {
        this.validates(fx, t);
    }

    @SuppressWarnings("unchecked")
    public <T> void validates(T... ts) {
        this.validates(Arrays.asList(ts));
    }

    @SuppressWarnings("unchecked")
    public <T> void validates(Consumer<String> fx, T... ts) {
        this.validates(fx, Arrays.asList(ts));
    }

    public <T> void validates(Collection<T> ts) {
        this.validatez(ts);
    }

    public <T> void validates(Consumer<String> fx, Collection<T> ts) {
        this.validatez(fx, ts);
    }

    public <T> void validatez(Collection<T> ts, Class<?>... groups) {
        this.validatez(PopupExceptionChecker::throwUnchecked, ts, groups);
    }

    public <T> void validatez(Consumer<String> fx, Collection<T> ts, Class<?>... groups) {
        ts.forEach((t) -> {
            Set<ConstraintViolation<T>> violations = this.validator.validate(t, groups);
            StringBuilder buf = new StringBuilder();
            if (isNotEmpty(violations)) {
                violations.forEach(v -> {
                    buf.append(v.getMessage()).append(";");
                });

                String message = buf.toString().replaceAll(";*$", "");
                fx.accept(message);
            }
        });
    }

    private static <T> boolean isNotEmpty(Collection<T> ts) {
        return !ObjectUtils.isEmpty(ts);
    }
}

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
package com.photowey.popup.starter.autoconfigure.config;

import com.photowey.popup.starter.autoconfigure.validator.EntityValidator;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * {@code PopupAutoConfigure}
 *
 * @author photowey
 * @date 2023/03/03
 * @since 1.0.0
 */
@AutoConfiguration
@Import(value = {
        PopupAutoConfigure.ValidatorConfigure.class,
})
public class PopupAutoConfigure {

    @Configuration
    static class ValidatorConfigure {

        @Bean
        @ConditionalOnMissingBean(Validator.class)
        public Validator validator() {
            return Validation.buildDefaultValidatorFactory().getValidator();
        }

        @Bean
        public EntityValidator entityValidator(Validator validator) {
            return new EntityValidator(validator);
        }
    }

}

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
package com.photowey.component.test.cloud.core.app.controller;

import com.photowey.component.core.health.Status;
import com.photowey.component.test.cloud.core.app.annotation.MappingRestController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import static com.photowey.component.test.cloud.core.app.controller.MappingController.MAPPING_CONTROLLER_BEAN_NAME;

/**
 * {@code MappingController}
 *
 * @author photowey
 * @date 2023/06/18
 * @since 1.0.0
 */
@MappingRestController(value = MAPPING_CONTROLLER_BEAN_NAME, path = "/hello")
public class MappingController implements BeanFactoryAware {

    public static final String MAPPING_CONTROLLER_BEAN_NAME = "com.photowey.component.test.cloud.core.app.controller.MappingController";

    private ListableBeanFactory beanFactory;

    // dependency cycle
    //@Autowired
    //@Qualifier(MAPPING_CONTROLLER_BEAN_NAME)
    //private MappingController mappingController;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    /**
     * <pre>
     * {
     * "status": "com.photowey.component.test.cloud.core.app.controller.MappingController"
     * }
     * </pre>
     *
     * @see * http://127.0.0.1:11001/hello/world
     */
    @GetMapping("/world")
    public ResponseEntity<Status> healthz() {
        String[] beanNames = this.beanFactory.getBeanNamesForType(MappingController.class);
        return new ResponseEntity<>(Status.builder().status(StringUtils.arrayToDelimitedString(beanNames, ",")).build(), HttpStatus.OK);
    }

}

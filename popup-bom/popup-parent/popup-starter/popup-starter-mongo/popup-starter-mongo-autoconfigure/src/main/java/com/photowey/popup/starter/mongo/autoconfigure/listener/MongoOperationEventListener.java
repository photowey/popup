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
package com.photowey.popup.starter.mongo.autoconfigure.listener;

import com.photowey.component.common.util.ObjectUtils;
import com.photowey.popup.starter.mongo.core.document.MongoDocument;
import com.photowey.popup.starter.mongo.service.generator.MongoKeyGenerator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;

/**
 * {@code MongoOperationEventListener}
 *
 * @author photowey
 * @date 2023/02/06
 * @since 1.0.0
 */
public class MongoOperationEventListener extends AbstractMongoEventListener<MongoDocument> implements BeanFactoryAware {

    private ListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<MongoDocument> event) {
        MongoDocument document = event.getSource();
        if (ObjectUtils.isNotNullOrEmpty(document.getId())) {
            return;
        }

        if (document.supportCustoms()) {
            String customId = document.customDefine();
            this.checkNull(customId);

            document.setId(customId);
        } else {
            MongoKeyGenerator keyGenerator = this.beanFactory.getBean(MongoKeyGenerator.class);
            String objectId = keyGenerator.objectId();
            document.setId(objectId);
        }
    }

    private void checkNull(String customId) {
        if (ObjectUtils.isNullOrEmpty(customId)) {
            throw new RuntimeException("Define the objectId for document by document.customDefine()");
        }
    }
}

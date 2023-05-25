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
package com.photowey.popup.starter.mongo.service.ext;

import com.photowey.popup.starter.mongo.core.constant.MongoConstants;
import com.photowey.popup.starter.mongo.core.document.DocumentUpdateCounter;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ObjectUtils;

import java.util.function.Consumer;

/**
 * {@code MongoServiceExt}
 *
 * @author photowey
 * @date 2023/02/04
 * @since 1.0.0
 */
public interface MongoServiceExt<T, ID> {

    String DEFAULT_PK_ID = MongoConstants.DEFAULT_PK_ID;
    String DEFAULT_DELETED_KEY = MongoConstants.DEFAULT_DELETED_KEY;
    int DEFAULT_DELETED_VALUE = MongoConstants.DEFAULT_DELETED_VALUE;

    default MongoOperations mongoOperations() {
        return null;
    }

    default <PK, TARGET> void addToSet(TARGET target, PK pk, String node, Class<T> clazz) {
        MongoOperations mongoTemplate = this.mongoOperations();
        if (null == mongoTemplate) {
            return;
        }
        Query query = new Query(Criteria.where(DEFAULT_PK_ID).is(this.wrapPk(pk)));
        Update update = new Update();
        update.addToSet(node, target);

        mongoTemplate.updateFirst(query, update, clazz);
    }

    default <PK, TARGET> void pull(TARGET target, PK pk, String node, Class<T> clazz) {
        MongoOperations mongoTemplate = this.mongoOperations();
        if (null == mongoTemplate) {
            return;
        }
        Query query = new Query(Criteria.where(DEFAULT_PK_ID).is(this.wrapPk(pk)));
        Update update = new Update();
        update.pull(node, target);

        mongoTemplate.updateFirst(query, update, clazz);
    }

    default <PK> void doUpdate(PK pk, Document document, Class<T> clazz) {
        ID id = this.wrapPk(pk);
        Query query = this.createQueryById(id);
        Update update = Update.fromDocument(document);

        this.mongoOperations().updateFirst(query, update, clazz);
    }

    default <PK> void logicDelete(PK pk, Class<T> clazz) {
        ID id = this.wrapPk(pk);
        Query query = this.createQueryById(id);
        Update update = new Update();
        update.set(DEFAULT_DELETED_KEY, this.deleted());

        this.mongoOperations().updateFirst(query, update, clazz);
    }

    // ------------------------------------------------------------------------- fx

    default <V> boolean setIfPresent(V v, Consumer<V> then) {
        if (ObjectUtils.isEmpty(v)) {
            return false;
        }

        then.accept(v);

        return true;
    }

    default <V> boolean setIfPresent(V v, DocumentUpdateCounter counter, Consumer<V> then) {
        if (ObjectUtils.isEmpty(v)) {
            return false;
        }

        then.accept(v);
        counter.increment();

        return true;
    }

    default Document toBsonDocument(T t) {
        Document document = (Document) this.mongoOperations().getConverter().convertToMongoType(t);

        return document;
    }

    default Query createQueryById(ID pkV) {
        return new Query(Criteria.where(DEFAULT_PK_ID).is(pkV));
    }

    default <PK> ID wrapPk(PK pk) {
        return (ID) String.valueOf(pk);
    }

    default int deleted() {
        return DEFAULT_DELETED_VALUE;
    }
}

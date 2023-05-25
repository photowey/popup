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

import com.photowey.component.common.func.ThreeConsumer;
import com.photowey.component.common.geodesy.LocalGeodeticCalculator;
import com.photowey.component.common.number.BigDecimalUtils;
import com.photowey.component.common.number.NumberConstants;
import com.photowey.popup.starter.mongo.core.constant.MongoConstants;
import com.photowey.popup.starter.mongo.core.document.DocumentUpdateCounter;
import org.bson.Document;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
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

    default <PK, TARGET> void addToSet(
            TARGET target, PK pk,
            String node, Class<T> clazz) {
        this.addToSetOps(target, pk, node, (mongoTemplate, query, update) -> {
            mongoTemplate.updateFirst(query, update, clazz);
        });
    }

    default <V, TARGET> void addToSet(
            TARGET target, String key, V value,
            String node, Class<T> clazz) {
        this.addToSetOps(target, key, value, node, (mongoTemplate, query, update) -> {
            mongoTemplate.updateFirst(query, update, clazz);
        });
    }

    default <PK, TARGET> void batchAddToSet(
            TARGET target, PK pk,
            String node, Class<T> clazz) {
        this.addToSetOps(target, pk, node, (mongoTemplate, query, update) -> {
            mongoTemplate.updateMulti(query, update, clazz);
        });
    }

    default <V, TARGET> void batchAddToSet(
            TARGET target, String key, V value,
            String node, Class<T> clazz) {
        this.addToSetOps(target, key, value, node, (mongoTemplate, query, update) -> {
            mongoTemplate.updateMulti(query, update, clazz);
        });
    }

    default <PK, TARGET> void addToSetOps(
            TARGET target, PK pk,
            String node, ThreeConsumer<MongoOperations, Query, Update> fx) {
        this.addToSetOps(target, DEFAULT_PK_ID, this.wrapPk(pk), node, fx);
    }

    default <V, TARGET> void addToSetOps(
            TARGET target, String key, V value,
            String node, ThreeConsumer<MongoOperations, Query, Update> fx) {
        MongoOperations mongoTemplate = this.mongoOperations();
        if (null == mongoTemplate) {
            return;
        }
        Query query = new Query(Criteria.where(key).is(value));
        Update update = new Update();
        update.addToSet(node, target);

        fx.accept(mongoTemplate, query, update);
    }

    default <PK, TARGET> void pull(TARGET target, PK pk, String node, Class<T> clazz) {
        this.bullOps(target, pk, node, (mongoTemplate, query, update) -> {
            mongoTemplate.updateFirst(query, update, clazz);
        });
    }

    default <PK, TARGET> void batchPull(TARGET target, PK pk, String node, Class<T> clazz) {
        this.bullOps(target, pk, node, (mongoTemplate, query, update) -> {
            mongoTemplate.updateMulti(query, update, clazz);
        });
    }

    default <PK, TARGET> void bullOps(TARGET target, PK pk, String node, ThreeConsumer<MongoOperations, Query, Update> fx) {
        MongoOperations mongoTemplate = this.mongoOperations();
        if (null == mongoTemplate) {
            return;
        }
        Query query = new Query(Criteria.where(DEFAULT_PK_ID).is(this.wrapPk(pk)));
        Update update = new Update();
        update.pull(node, target);

        fx.accept(mongoTemplate, query, update);
    }


    default void update(Class<T> clazz, Consumer<Criteria> condition, Consumer<Update> updater) {
        this.doUpdate(condition, updater, (mongoTemplate, query, update) -> {
            mongoTemplate.updateFirst(query, update, clazz);
        });
    }

    default void batchUpdate(Class<T> clazz, Consumer<Criteria> condition, Consumer<Update> updater) {
        this.doUpdate(condition, updater, (mongoTemplate, query, update) -> {
            mongoTemplate.updateMulti(query, update, clazz);
        });
    }

    default void doUpdate(
            Consumer<Criteria> condition,
            Consumer<Update> updater,
            ThreeConsumer<MongoOperations, Query, Update> fx) {
        MongoOperations mongoTemplate = this.mongoOperations();
        if (null == mongoTemplate) {
            return;
        }

        Criteria criteria = new Criteria();
        condition.accept(criteria);

        Query query = new Query(criteria);
        Update update = new Update();
        updater.accept(update);

        fx.accept(mongoTemplate, query, update);
    }

    default <PK> void logicDelete(PK pk, Class<T> clazz) {
        ID id = this.wrapPk(pk);
        Query query = this.createQueryById(id);
        Update update = new Update();
        update.set(DEFAULT_DELETED_KEY, this.deleted());

        this.mongoOperations().updateFirst(query, update, clazz);
    }

    // ------------------------------------------------------------------------- geo

    default BigDecimal tryCalculateDistance(ID objectId, Point point, Class<T> clazz) {
        MongoOperations mongoTemplate = this.mongoOperations();
        if (null == mongoTemplate) {
            return BigDecimalUtils.newZeroBigDecimal();
        }

        Query query = this.createQueryById(objectId);
        GeoJsonPoint geo = point instanceof GeoJsonPoint ? (GeoJsonPoint) point : new GeoJsonPoint(point);
        NearQuery nearQuery = NearQuery.near(geo, Metrics.KILOMETERS);
        nearQuery.query(query);
        GeoResults<T> results = mongoTemplate.geoNear(nearQuery, clazz);

        for (GeoResult<T> result : results) {
            return this.formatKm(result.getDistance());
        }

        return BigDecimalUtils.newZeroBigDecimal();
    }

    // ------------------------------------------------------------------------- fx

    default <V> boolean setIfPresent(V v, Consumer<V> then) {
        if (ObjectUtils.isEmpty(v)) {
            return false;
        }

        then.accept(v);

        return true;
    }

    default <V> boolean setIfTrue(boolean expression, V v, Consumer<V> then) {
        if (expression) {
            then.accept(v);

            return true;
        }

        return false;
    }

    default boolean setIfTrue(boolean expression, Runnable then) {
        if (expression) {
            then.run();

            return true;
        }

        return false;
    }

    default <V> boolean setIfPresent(V v, DocumentUpdateCounter counter, Consumer<V> then) {
        if (ObjectUtils.isEmpty(v)) {
            return false;
        }

        then.accept(v);
        counter.increment();

        return true;
    }

    default <V> boolean setIfPresent(boolean expression, V v, DocumentUpdateCounter counter, Consumer<V> then) {
        if (expression) {
            then.accept(v);
            counter.increment();

            return true;
        }

        return false;
    }

    default boolean setIfPresent(boolean expression, DocumentUpdateCounter counter, Runnable then) {
        if (expression) {
            then.run();
            counter.increment();

            return true;
        }

        return false;
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

    default BigDecimal formatKm(Distance distance) {
        return this.formatKm(distance.getValue());
    }

    default BigDecimal formatKm(double distance) {
        return this.formatKm(distance, NumberConstants.FIVE_DECIMAL_POINTS);
    }

    default BigDecimal formatKm(double distance, String pattern) {
        return this.kmTom(distance, pattern);
    }

    default BigDecimal formatM(double distance) {
        return LocalGeodeticCalculator.getInstance().formatM(distance);
    }

    default BigDecimal kmTom(double distanceKm, String pattern) {
        return LocalGeodeticCalculator.getInstance().kmTom(distanceKm, pattern);
    }

    default BigDecimal formatDistance(double distance, String pattern) {
        return LocalGeodeticCalculator.getInstance().formatDistance(distance, pattern);
    }

    default BigDecimal formatDistance(BigDecimal distanceB, String pattern) {
        return LocalGeodeticCalculator.getInstance().formatDistance(distanceB, pattern);
    }
}

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
package com.photowey.popup.starter.mongo.core.operator;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.regex.Pattern;

/**
 * {@code MongoQueryBuilders}
 *
 * @author photowey
 * @date 2023/02/07
 * @since 1.0.0
 */
public class MongoQueryBuilders {

    public static final String MONGO_OBJECT_ID_FIELD = "_id";

    public static <PK> Query buildById(PK pk) {
        return buildByPk(MONGO_OBJECT_ID_FIELD, pk);
    }

    /**
     * database.id == bizId
     * bizId: Long
     * objectId: String
     */
    public static Query buildByLongId(Long bizId) {
        return buildByObjectId(wrapObjectId(bizId));
    }

    /**
     * objectId == document.id == _id
     */
    public static Query buildByObjectId(String objectId) {
        return buildByPk(MONGO_OBJECT_ID_FIELD, objectId);
    }

    public static <PK> Query buildByPk(String pkFiled, PK pkV) {
        return new Query(Criteria.where(pkFiled).is(pkV));
    }

    public static <PK> void buildByPk(Query query, String pkFiled, PK pkV) {
        eq(query, pkFiled, pkV);
    }

    public static <V> Query eq(String field, V fieldV) {
        return new Query(Criteria.where(field).is(fieldV));
    }

    public static <V> void eq(Query query, String field, V fieldV) {
        query.addCriteria(Criteria.where(field).is(fieldV));
    }

    public static <V> Query like(String field, V v) {
        Query query = new Query();
        like(query, field, v);

        return query;
    }

    public static <V> void like(Query query, String field, V fieldV) {
        Pattern pattern = Pattern.compile("^.*?" + fieldV + ".*$", Pattern.CASE_INSENSITIVE);
        query.addCriteria(Criteria.where(field).regex(pattern));
    }

    public static String wrapObjectId(Long bizId) {
        if (null == bizId) {
            return null;
        }

        return String.valueOf(bizId);
    }

    public static Long unwrapObjectId(String objectId) {
        if (isNumeric(objectId)) {
            return Long.parseLong(objectId);
        }

        return null;
    }

    /**
     * copy from {@code org.apache.commons.lang3.StringUtils#isEmpty(java.lang.CharSequence)}
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * {@code org.apache.commons.lang3.StringUtils#isNumeric(java.lang.CharSequence)}
     */
    public static boolean isNumeric(CharSequence cs) {
        if (isEmpty(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
                if (!Character.isDigit(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }
}


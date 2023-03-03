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

import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.query.Update;

/**
 * {@code MongoOperators}
 *
 * @author photowey
 * @date 2023/02/07
 * @since 1.0.0
 */
public class MongoOperators {

    public static <PK, V> void updateById(BulkOperations bulkOps, PK pkV, String field, V fieldV) {
        bulkOps.updateOne(MongoQueryBuilders.buildById(pkV), Update.update(field, fieldV));
    }
}


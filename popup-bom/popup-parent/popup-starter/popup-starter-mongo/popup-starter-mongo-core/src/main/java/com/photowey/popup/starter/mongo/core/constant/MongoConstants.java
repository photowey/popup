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
package com.photowey.popup.starter.mongo.core.constant;

/**
 * {@code MongoConstants}
 *
 * @author photowey
 * @date 2023/05/19
 * @since 1.0.0
 */
public interface MongoConstants {

    String DEFAULT_PK_ID = "_id";
    String DEFAULT_DELETED_KEY = "deleted";
    String DEFAULT_LOCATION_KEY = "location";
    int DEFAULT_DELETED_VALUE = 1;

    String MONGO_TRANSACTION_MANAGER_BEAN_NAME = "org.springframework.data.mongodb.MongoTransactionManager";
}

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
package com.photowey.popup.starter.mongo.repository.ext;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

/**
 * {@code MongoRepositoryExt}
 *
 * Example:
 * PK: Long
 * ID: String
 *
 * @author photowey
 * @date 2023/02/04
 * @since 1.0.0
 */
public interface MongoRepositoryExt<T, ID> extends MongoRepository<T, ID> {

    default <PK> T findByPk(PK pk) {
        ID id = this.wrapPk(pk);
        return this.findById(id).orElse(null);
    }

    List<T> findByPks(Collection<ID> ids);

    default <PK> void deleteByPk(PK pk) {
        ID id = this.wrapPk(pk);
        this.deleteById(id);
    }

    default <PK> boolean existsByPk(PK pk) {
        ID id = this.wrapPk(pk);
        return this.existsById(id);
    }

    default <PK> ID wrapPk(PK pk) {
        ID id = (ID) String.valueOf(pk);

        return id;
    }

}

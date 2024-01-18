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
package com.photowey.popup.starter.cache.redis.template;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * {@code RedisTemplateProxy}
 *
 * @author photowey
 * @date 2023/05/13
 * @since 1.0.0
 */
public interface RedisTemplateProxy extends IRedisTemplate {

    RedisTemplate<String, Object> redis();

    StringRedisTemplate stringRedis();

    default <T> List<T> toList(BiFunction<Object, Double, T> fx, Set<ZSetOperations.TypedTuple<Object>> typedTuples) {
        if (null == typedTuples) {
            return this.emptyList();
        }

        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();
        List<T> ts = new ArrayList<>(typedTuples.size());

        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            Object value = next.getValue();
            Double score = next.getScore();

            T t = fx.apply(value, score);
            ts.add(t);
        }

        return ts;
    }
}

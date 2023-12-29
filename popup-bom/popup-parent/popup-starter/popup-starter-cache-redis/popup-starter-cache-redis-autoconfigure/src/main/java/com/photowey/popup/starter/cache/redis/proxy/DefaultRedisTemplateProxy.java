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
package com.photowey.popup.starter.cache.redis.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.photowey.component.common.func.lambda.LambdaFunction;
import com.photowey.popup.starter.cache.redis.template.RedisTemplateProxy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * {@code DefaultRedisTemplateProxy}
 *
 * @author weichangjun
 * @date 2023/12/24
 * @since 1.0.0
 */
public class DefaultRedisTemplateProxy implements RedisTemplateProxy, BeanFactoryAware {

    private static final long NEGATIVE_SIGN = -1L;

    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    private ListableBeanFactory beanFactory;

    public DefaultRedisTemplateProxy(
            RedisTemplate<String, Object> redisTemplate,
            StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ListableBeanFactory) beanFactory;
    }

    public ObjectMapper objectMapper() {
        return this.beanFactory.getBean(ObjectMapper.class);
    }

    // ----------------------------------------------------------------

    public <T> T toBean(Map<Object, Object> entries, Class<T> clazz) {
        return this.objectMapper().convertValue(entries, clazz);
    }

    // ----------------------------------------------------------------

    public boolean expire(final String key, long expireTime) {
        return this.expire(key, expireTime, TimeUnit.SECONDS);
    }

    public boolean expire(final String key, long expireTime, TimeUnit timeUnit) {
        // Unboxing of 'this.redisTemplate.expire(key, expireTime, timeUnit)' may produce 'NullPointerException'
        return Boolean.TRUE.equals(this.redisTemplate.expire(key, expireTime, timeUnit));
    }

    @Override
    public boolean exists(final String key) {
        return Boolean.TRUE.equals(this.redisTemplate.hasKey(key));
    }

    @Override
    public void delete(final String key) {
        this.redisTemplate.delete(key);
    }

    @Override
    public <T> void set(final String key, T value) {
        this.redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public synchronized <T> void reset(final String key, T value) {
        this.delete(key);
        this.set(key, value);
    }

    public void set(final String key, Object value, long expireTime) {
        this.redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.SECONDS);
    }

    public synchronized void reset(final String key, Object value, long expireTime) {
        this.set(key, value, expireTime, TimeUnit.SECONDS);
    }

    @Override
    public void set(final String key, Object value, long expireTime, TimeUnit timeUnit) {
        this.redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    @Override
    public synchronized void reset(final String key, Object value, long expireTime, TimeUnit timeUnit) {
        this.set(key, value, expireTime, timeUnit);
    }

    @Override
    public void setString(String key, String value) {
        this.stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public synchronized void resetString(String key, String value) {
        this.setString(key, value);
    }

    @Override
    public void setString(String key, String value, long expires, TimeUnit timeUnit) {
        this.stringRedisTemplate.opsForValue().set(key, value, expires, timeUnit);
    }

    @Override
    public synchronized void resetString(String key, String value, long expires, TimeUnit timeUnit) {
        this.setString(key, value, expires, timeUnit);
    }

    @Override
    public <T> T get(final String key) {
        return (T) this.redisTemplate.opsForValue().get(key);
    }

    @Override
    public String getString(final String key) {
        return this.stringRedisTemplate.opsForValue().get(key);
    }

    // ---------------------------------------------------------------- incr

    @Override
    public Long incr(String key) {
        return this.redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Long incr(String key, Long delta) {
        return this.redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long hashIncr(String key, String filed) {
        return this.hashIncr(key, filed, 1L);
    }

    @Override
    public Long hashIncr(String key, String filed, Long delta) {
        return this.redisTemplate.opsForHash().increment(key, filed, Math.abs(delta));
    }

    @Override
    public <T> Long hashIncr(String key, LambdaFunction<T, ?> filed) {
        return this.hashIncr(key, filed, 1L);
    }

    @Override
    public <T> Long hashIncr(String key, LambdaFunction<T, ?> filed, Long delta) {
        return this.hashIncr(key, LambdaFunction.resolve(filed), delta);
    }

    // ---------------------------------------------------------------- decr

    @Override
    public Long decr(String key) {
        return this.redisTemplate.opsForValue().decrement(key);
    }

    @Override
    public Long decr(String key, Long delta) {
        return this.redisTemplate.opsForValue().decrement(key, delta);
    }

    @Override
    public Long hashDecr(String key, String filed) {
        return this.hashDecr(key, filed, 1L);
    }

    @Override
    public Long hashDecr(String key, String filed, Long delta) {
        return this.redisTemplate.opsForHash().increment(key, filed, Math.abs(delta) * NEGATIVE_SIGN);
    }

    @Override
    public <T> Long hashDecr(String key, LambdaFunction<T, ?> filed) {
        return this.hashDecr(key, filed, 1L);
    }

    @Override
    public <T> Long hashDecr(String key, LambdaFunction<T, ?> filed, Long delta) {
        return this.hashDecr(key, LambdaFunction.resolve(filed), delta);
    }

    // ---------------------------------------------------------------- redis

    @Override
    public void remove(String key) {
        if (this.exists(key)) {
            this.redisTemplate.delete(key);
        }
    }

    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            this.remove(key);
        }
    }

    @Override
    public void removePattern(String pattern) {
        Set<String> keys = this.redisTemplate.keys(pattern);
        if (null != keys) {
            if (Objects.requireNonNull(keys).size() > 0) {
                this.redisTemplate.delete(keys);
            }
        }
    }

    @Override
    public <T> long leftPush(final String key, T value) {
        Long length = this.redisTemplate.opsForList().leftPush(key, value);

        return length != null ? length : 0L;
    }

    @Override
    public <T> T rightPop(final String key) {
        return (T) this.redisTemplate.opsForList().rightPop(key);
    }

    @Override
    public long rightPush(final String key, Object value) {
        Long length = this.redisTemplate.opsForList().rightPush(key, value);
        return length != null ? length : 0L;
    }

    @Override
    public <T> T leftPop(final String key) {
        return (T) this.redisTemplate.opsForList().leftPop(key);
    }

    @Override
    public <T> List<T> range(String key, Integer start, Integer stop) {
        start = null != start ? start : 0;
        stop = null != stop ? stop : -1;

        List<T> objects = (List<T>) this.redisTemplate.opsForList().range(key, start, stop);
        if (null == objects) {
            objects = this.emptyList();
        }

        return objects;
    }

    @Override
    public <T> Long setAdd(final String key, T... values) {
        return this.redisTemplate.opsForSet().add(key, values);
    }

    @Override
    public <T> Long setRemove(final String key, T... values) {
        return this.redisTemplate.opsForSet().remove(key, values);
    }

    @Override
    public <T> T setPop(final String key) {
        return (T) this.redisTemplate.opsForSet().pop(key);
    }

    @Override
    public <T> Boolean setMove(final String key, T value, String destKey) {
        return this.redisTemplate.opsForSet().move(key, value, destKey);
    }

    @Override
    public Long setSize(String key) {
        return this.redisTemplate.opsForSet().size(key);
    }

    @Override
    public <T> Boolean setIsMember(String key, T value) {
        return this.redisTemplate.opsForSet().isMember(key, value);
    }

    @Override
    public <T> Set<T> setMembers(final String key) {
        Set<Object> objects = this.redisTemplate.opsForSet().members(key);
        if (null == objects) {
            objects = this.emptySet();
        }

        return (Set<T>) objects;
    }

    @Override
    public <T> T setRandomMember(final String key) {
        return (T) this.redisTemplate.opsForSet().randomMember(key);
    }

    @Override
    public <T> List<T> setRandomMembers(final String key, long count) {
        List<Object> objects = this.redisTemplate.opsForSet().randomMembers(key, count);
        if (null == objects) {
            objects = this.emptyList();
        }

        return (List<T>) objects;
    }

    @Override
    public Long zsetSize(final String key) {
        return this.determineCounter(() -> this.redisTemplate.opsForZSet().size(key));
    }

    @Override
    public boolean zsetExists(final String key, Object value) {
        Double score = this.redisTemplate.opsForZSet().score(key, value);
        return null != score;
    }

    @Override
    public void zsetTrim(final String key, long max) {
        long size = this.redisTemplate.opsForZSet().size(key);
        if (size > max) {
            this.zsetRemoveRange(key, 0, size - (max + 1));
        }
    }

    @Override
    public void zsetRemoveRange(final String key, long start, long end) {
        this.redisTemplate.opsForZSet().removeRange(key, start, end);
    }

    @Override
    public void zsetAdd(final String key, Object value, Double score) {
        this.redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void zsetAdd(final String key, Object value, Long score) {
        this.redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void zsetAdd(final String key, Object value, Integer score) {
        this.redisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public void zsetScoreIncr(String key, Object value, Double score) {
        this.redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    @Override
    public void zsetScoreIncr(String key, Object value, Long score) {
        this.redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    @Override
    public void zsetScoreIncr(String key, Object value, Integer score) {
        this.redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    @Override
    public <T> List<T> zsetRangeWithScores(final String key, Long start, Long end, BiFunction<Object, Double, T> fx) {
        start = null != start ? start : 0;
        end = null != end ? end : -1;

        Set<ZSetOperations.TypedTuple<Object>> typedTuples = this.redisTemplate.opsForZSet().rangeWithScores(key, start, end);
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

    @Override
    public <T> List<T> zsetReverseRangeWithScores(String key, Long start, Long end, BiFunction<Object, Double, T> fx) {
        start = null != start ? start : 0;
        end = null != end ? end : -1;
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = this.redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
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

    // ---------------------------------------------------------------- hash

    @Override
    public void hashSet(String key, String field, Object value) {
        this.redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public void hashmSet(String key, Map<Object, Object> entries) {
        this.redisTemplate.opsForHash().putAll(key, entries);
    }

    @Override
    public <T> T hashGet(final String key, final String field) {
        return (T) this.redisTemplate.opsForHash().get(key, field);
    }

    @Override
    public <T, R> R hashGet(final String key, LambdaFunction<T, ?> field) {
        return this.hashGet(key, LambdaFunction.resolve(field));
    }

    @Override
    public <T> T hashmGet(Class<T> clazz, final String key, final String... fields) {
        List<Object> fs = Arrays.asList(fields);
        Map<Object, Object> entries = this.hashmGet(key, fs);
        return this.toBean(entries, clazz);
    }

    @Override
    public <T> T hashmGet(final String key, Function<Map<Object, Object>, T> fx, final String... fields) {
        List<Object> fs = Arrays.asList(fields);
        Map<Object, Object> entries = this.hashmGet(key, fs);

        return fx.apply(entries);
    }

    @Override
    public <T, R> R hashmGet(final String key, Function<Map<Object, Object>, R> fx, LambdaFunction<T, ?>... fields) {
        List<Object> fs = Stream.of(fields).map(LambdaFunction::resolve).collect(Collectors.toList());
        Map<Object, Object> entries = this.hashmGet(key, fs);

        return fx.apply(entries);
    }

    @Override
    public <T, R> R hashmGet(Class<R> clazz, String key, LambdaFunction<T, ?>... fields) {
        List<Object> fs = Stream.of(fields).map(LambdaFunction::resolve).collect(Collectors.toList());
        Map<Object, Object> entries = this.hashmGet(key, fs);
        return this.toBean(entries, clazz);
    }

    @Override
    public <T> T hashEntries(Class<T> clazz, final String key) {
        Map<Object, Object> entries = this.redisTemplate.opsForHash().entries(key);
        return this.toBean(entries, clazz);
    }

    @Override
    public Map<Object, Object> hashmGet(String key, List<Object> fields) {
        List<Object> values = this.redisTemplate.opsForHash().multiGet(key, fields);

        Map<Object, Object> entries = new HashMap<>();

        for (int i = 0; i < fields.size(); i++) {
            Object k = fields.get(i);
            Object v = values.get(i);

            entries.put(k, v);
        }

        return entries;
    }

    // ---------------------------------------------------------------- template

    @Override
    public RedisTemplate<String, Object> redis() {
        return this.redisTemplate;
    }

    @Override
    public StringRedisTemplate stringRedis() {
        return this.stringRedisTemplate;
    }

    // ---------------------------------------------------------------- empty.collection

    private <T> List<T> emptyList() {
        return new ArrayList<>(0);
    }

    private <T> Set<T> emptySet() {
        return new HashSet<>(0);
    }
}

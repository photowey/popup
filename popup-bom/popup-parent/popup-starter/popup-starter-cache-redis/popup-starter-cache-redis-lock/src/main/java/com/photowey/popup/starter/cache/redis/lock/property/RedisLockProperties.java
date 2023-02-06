package com.photowey.popup.starter.cache.redis.lock.property;

import com.photowey.popup.starter.cache.redis.core.mode.RedisModeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code RedisLockProperties}
 *
 * @author photowey
 * @date 2023/01/31
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "spring.redis.lock.redisson")
public class RedisLockProperties {

    private RedisModeEnum mode = RedisModeEnum.STANDALONE;
    private String address = "redis://127.0.0.1:6379";
    private String password = null;
    private String masterName = "master";
    private String namespace = "";
    private int database = 0;
    private int timeout = 10_000;
    private boolean ignoreLockingExceptions = false;

}
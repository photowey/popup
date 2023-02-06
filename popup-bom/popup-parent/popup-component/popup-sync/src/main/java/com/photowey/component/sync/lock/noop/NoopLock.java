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
package com.photowey.component.sync.lock.noop;

import com.photowey.component.sync.lock.Lock;

import java.util.concurrent.TimeUnit;

/**
 * {@code NoopLock}
 *
 * @author photowey
 * @date 2023/01/31
 * @since 1.0.0
 */
public class NoopLock implements Lock {

    @Override
    public void acquireLock(String lockId) {}

    @Override
    public boolean acquireLock(String lockId, long waitTime, TimeUnit unit) {
        return true;
    }

    @Override
    public boolean acquireLock(String lockId, long waitTime, long leaseTime, TimeUnit unit) {
        return true;
    }

    @Override
    public void releaseLock(String lockId) {}

    @Override
    public void deleteLock(String lockId) {}
}

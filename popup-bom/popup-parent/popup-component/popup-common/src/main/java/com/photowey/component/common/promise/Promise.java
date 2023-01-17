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
package com.photowey.component.common.promise;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@code Promise}
 *
 * @author photowey
 * @date 2023/01/05
 * @since 1.0.0
 */
public abstract class Promise<V> {

    public static <V> Promise<V> run(Callable<V> command) {
        try {
            return success(command.call());
        } catch (Exception e) {
            return failure(e);
        }
    }

    public static <V> Promise<V> run(Callable<V> command, Predicate<V> tester) {
        try {
            V v = command.call();
            return tester.test(v) ? success(v) : failure(new EmptyException());
        } catch (Throwable e) {
            return failure(e);
        }
    }

    public static <V> Promise<V> run(Callable<V> command, Consumer<V> resolve, Consumer<Throwable> reject) {
        try {
            V v = command.call();
            resolve.accept(v);
            return Promise.success(v);
        } catch (Throwable e) {
            reject.accept(e);
            return Promise.failure(e);
        }
    }

    // -------------------------------------------------------------------------

    public static <V> Promise<V> call(Callable<V> command) {
        checkNotNull(command, "command");
        return of(() -> success(command.call()));
    }

    // -------------------------------------------------------------------------

    public static <V> Promise<V> success(V value) {
        return new Success<>(value);
    }

    public static <V> Promise<V> failure(Throwable cause) {
        return new Failure<>(checkNotNull(cause, "cause"));
    }

    // -------------------------------------------------------------------------

    private static <V> Promise<V> of(Callable<Promise<V>> command) {
        try {
            return command.call();
        } catch (Throwable e) {
            return failure(e);
        }
    }

    // -------------------------------------------------------------------------

    private static <T> T checkNotNull(T input, String title) {
        if (input == null) {
            throw new NullPointerException(title + " must not be null");
        }
        return input;
    }

    // -------------------------------------------------------------------------

    private Promise() {
        throw new AssertionError("No " + this.getClass().getName() + " instances for you!");
    }

    public abstract <U> Promise<U> andThen(Transformer<V, U> transformer);

    public abstract <U> Promise<U> then(Function<V, Promise<U>> function);

    public abstract Promise<V> orElse(Callable<V> command);

    public abstract Promise<V> orElse(Supplier<Promise<V>> supplier);

    public abstract V get() throws Throwable;

    public abstract <E extends Throwable> V getOrThrow(Function<? super Throwable, E> exceptionTransformer) throws E;

    public abstract Promise<V> ifSuccess(Consumer<V> valueConsumer);

    public abstract Promise<V> ifFailure(Consumer<Throwable> causeConsumer);

    public abstract Promise<V> quiet(Function<Throwable, Boolean> function);

    public abstract <E extends Throwable> Promise<V> throwable(Function<Throwable, E> function);

    public abstract Optional<V> toOptional();

    // -------------------------------------------------------------------------

    public static class EmptyException extends RuntimeException {
        public EmptyException() {
            super();
        }
    }

    // -------------------------------------------------------------------------

    @FunctionalInterface
    public interface Transformer<S, T> {

        T apply(S value) throws Exception;
    }

    // -------------------------------------------------------------------------

    private static class Success<V> extends Promise<V> {

        private final V value;

        Success(V value) {
            this.value = value;
        }

        @Override
        public <U> Promise<U> andThen(Transformer<V, U> transformer) {
            checkNotNull(transformer, "transformer");
            return call(() -> transformer.apply(this.value));
        }

        @Override
        public <U> Promise<U> then(Function<V, Promise<U>> function) {
            checkNotNull(function, "function");
            return of(() -> function.apply(this.value));
        }

        @Override
        public Promise<V> orElse(Callable<V> command) {
            return this;
        }

        @Override
        public Promise<V> orElse(Supplier<Promise<V>> supplier) {
            return this;
        }

        @Override
        public V get() {
            return this.value;
        }

        @Override
        public <E extends Throwable> V getOrThrow(Function<? super Throwable, E> exceptionTransformer) {
            return this.value;
        }

        @Override
        public Promise<V> ifSuccess(Consumer<V> valueConsumer) {
            checkNotNull(valueConsumer, "valueConsumer");
            valueConsumer.accept(this.value);
            return this;
        }

        @Override
        public Promise<V> ifFailure(Consumer<Throwable> causeConsumer) {
            return this;
        }

        @Override
        public Promise<V> quiet(Function<Throwable, Boolean> function) {
            return this;
        }

        @Override
        public <E extends Throwable> Promise<V> throwable(Function<Throwable, E> function) {
            return this;
        }

        @Override
        public Optional<V> toOptional() {
            return Optional.ofNullable(this.value);
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }
            if (that == null || this.getClass() != that.getClass()) {
                return false;
            }
            return Objects.equals(this.value, ((Success<?>) that).value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    // -------------------------------------------------------------------------

    private static class Failure<V> extends Promise<V> {

        private Throwable cause;
        private boolean rethrow;

        Failure(Throwable cause) {
            this.cause = cause;
            // default true.
            this.rethrow = true;
        }

        @Override
        public <U> Promise<U> andThen(Transformer<V, U> transformer) {
            return uncheckedCast();
        }

        @Override
        public <U> Promise<U> then(Function<V, Promise<U>> function) {
            return uncheckedCast();
        }

        @SuppressWarnings("unchecked")
        private <U> Promise<U> uncheckedCast() {
            return (Promise<U>) this;
        }

        @Override
        public Promise<V> orElse(Callable<V> command) {
            checkNotNull(command, "command");
            return call(command);
        }

        @Override
        public Promise<V> orElse(Supplier<Promise<V>> supplier) {
            checkNotNull(supplier, "supplier");
            return of(supplier::get);
        }

        @Override
        public V get() throws Throwable {
            if (this.rethrow) {
                throw this.cause;
            }

            return null;
        }

        @Override
        public <E extends Throwable> V getOrThrow(Function<? super Throwable, E> exceptionTransformer) throws E {
            checkNotNull(exceptionTransformer, "exceptionTransformer");
            throw exceptionTransformer.apply(this.cause);
        }

        @Override
        public Promise<V> ifSuccess(Consumer<V> valueConsumer) {
            return this;
        }

        @Override
        public Promise<V> ifFailure(Consumer<Throwable> causeConsumer) {
            checkNotNull(causeConsumer, "causeConsumer");
            causeConsumer.accept(this.cause);
            return this;
        }

        @Override
        public Promise<V> quiet(Function<Throwable, Boolean> function) {
            this.rethrow = function.apply(this.cause);
            return this;
        }

        @Override
        public <E extends Throwable> Promise<V> throwable(Function<Throwable, E> function) {
            this.cause = function.apply(this.cause);
            return this;
        }

        @Override
        public Optional<V> toOptional() {
            return Optional.empty();
        }

        @Override
        public boolean equals(Object that) {
            if (this == that) {
                return true;
            }
            if (that == null || this.getClass() != that.getClass()) {
                return false;
            }
            return Objects.equals(this.cause, ((Failure<?>) that).cause);
        }

        @Override
        public int hashCode() {
            return Objects.hash(cause);
        }
    }
}

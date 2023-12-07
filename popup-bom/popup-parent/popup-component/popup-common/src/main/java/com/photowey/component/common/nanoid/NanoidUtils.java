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
package com.photowey.component.common.nanoid;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.photowey.component.common.thrower.AssertionErrorThrower;

import java.security.SecureRandom;
import java.util.Random;

/**
 * {@code NanoidUtils}
 *
 * @author photowey
 * @date 2022/12/25
 * @since 1.0.0
 */
public final class NanoidUtils {

    public static final char[] PASSWORD_ALPHABET = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();
    public static final char[] NANOID_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    public static final char[] NANOID_LOWER_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();
    public static final char[] NANOID_NUMBER_ALPHABET = "0123456789".toCharArray();

    public static final String ALPHABET_NUMBER_STRING = "123456789";
    public static final String ALPHABET_NUMBER_WITH_ZERO_STRING = "0" + ALPHABET_NUMBER_STRING;
    public static final String NORMAL_ALPHABET_STRING = "0123456789abcdefghijklmnopqrstuvwxyABCDEFGHIJKLMNOPQRSTUVWXY";

    public static final char[] ALPHABET_NUMBER = ALPHABET_NUMBER_STRING.toCharArray();
    public static final char[] ALPHABET_NUMBER_WITH_ZERO = ALPHABET_NUMBER_WITH_ZERO_STRING.toCharArray();

    public static final char[] NORMAL_ALPHABET = NORMAL_ALPHABET_STRING.toCharArray();

    public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();
    public static final int DEFAULT_PASSWORD_SIZE = 16;

    private NanoidUtils() {
        // utility class; can't create
        AssertionErrorThrower.throwz(NanoidUtils.class);
    }

    // ----------------------------------------------------------------------------------------------------------------

    public static String randomNanoId() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, NANOID_ALPHABET, NanoIdUtils.DEFAULT_SIZE);
    }

    public static String randomNanoId(int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, NANOID_ALPHABET, size);
    }

    public static String randomNanoId(char[] alphabet, int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, alphabet, size);
    }

    // ----------------------------------------------------------------------------------------------------------------

    public static String randomLowerNanoId() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, NANOID_LOWER_ALPHABET, NanoIdUtils.DEFAULT_SIZE);
    }

    public static String randomLowerNanoId(int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, NANOID_LOWER_ALPHABET, size);
    }

    public static String randomLowerNanoId(char[] alphabet, int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, alphabet, size);
    }

    // ----------------------------------------------------------------------------------------------------------------

    public static String randomNormalNanoId(Integer size) {
        return NanoidUtils.randomNanoId(NanoidUtils.NORMAL_ALPHABET, size);
    }

    public static String randomShuffleNormalNanoId(Integer size) {
        return NanoidUtils.randomNanoId(shuffleChars(NanoidUtils.NORMAL_ALPHABET_STRING), size);
    }

    // ----------------------------------------------------------------------------------------------------------------

    public static String randomNumberNanoId() {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, NANOID_NUMBER_ALPHABET, NanoIdUtils.DEFAULT_SIZE);
    }

    public static String randomNumberNanoId(int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, NANOID_NUMBER_ALPHABET, size);
    }

    public static String randomNumberNanoId(char[] alphabet, int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, alphabet, size);
    }

    // ----------------------------------------------------------------------------------------------------------------

    public static String randomNanoId(final Random random, final char[] alphabet, final int size) {
        return NanoIdUtils.randomNanoId(random, alphabet, size);
    }

    // ----------------------------------------------------------------------------------------------------------------

    public static String randomPassword() {
        return randomPassword(DEFAULT_PASSWORD_SIZE);
    }

    public static String randomPassword(int size) {
        return randomNanoId(DEFAULT_NUMBER_GENERATOR, PASSWORD_ALPHABET, size);
    }

    public static String shuffle(String alphabet) {
        char[] arr = shuffleChars(alphabet);
        return String.valueOf(arr);
    }

    public static char[] shuffleChars(String alphabet) {
        char[] ctx = alphabet.toCharArray();
        SecureRandom random = new SecureRandom();

        char tmp;
        int j;

        for (int i = ctx.length; i > 1; i--) {
            j = random.nextInt(i);
            tmp = ctx[i - 1];
            ctx[i - 1] = ctx[j];
            ctx[j] = tmp;
        }

        return ctx;
    }
}

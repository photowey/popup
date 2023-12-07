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
package com.photowey.component.common.file.magic;

import com.photowey.component.common.enums.FileMagicNumberEnum;
import com.photowey.component.common.shared.apache.io.FilenameUtils;
import com.photowey.component.common.shared.apache.tomcat.HexUtils;

import java.io.InputStream;

import static com.photowey.component.common.file.magic.MagicNumberReaderThrower.sneakyThrow;

/**
 * {@code DefaultMagicNumberReader}
 *
 * @author photowey
 * @date 2023/03/04
 * @since 1.0.0
 */
public class DefaultMagicNumberReader implements MagicNumberReader {

    private static final int FILE_HEADER_LENGTH = 28;

    public FileMagicNumberEnum read(String fileName, InputStream input) {
        try {
            byte[] head = new byte[FILE_HEADER_LENGTH];
            input.read(head, 0, FILE_HEADER_LENGTH);
            String header = HexUtils.toHexString(head);
            String suffix = FilenameUtils.getExtension(fileName);

            return FileMagicNumberEnum.codeOf(header, suffix);
        } catch (Exception e) {
            return sneakyThrow(e);
        }
    }
}

interface MagicNumberReaderThrower {

    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }
}
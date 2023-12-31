/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.libcore.timezone.testing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Arbitrary static utility methods to help with testing.
 */
public final class TestUtils {

    private TestUtils() {}

    public static String createFile(Path dir, String... lines) throws IOException {
        Path tempFile = Files.createTempFile(dir, "tmp", null /* suffix */);
        Files.write(tempFile, Arrays.asList(lines), StandardCharsets.US_ASCII);
        return tempFile.toString();
    }

    public static void deleteDir(Path tempDir) throws IOException {
        FileVisitor<? super Path> deleter = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                return delete(file);
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                return delete(dir);
            }

            private FileVisitResult delete(Path file) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
        };
        Files.walkFileTree(tempDir, deleter);
    }

    public static void assertAbsent(String s, String... absents) {
        for (String absent : absents) {
            assertFalse(s + " must not contain " + absent, s.contains(absent));
        }
    }

    public static void assertContains(String s, String... expecteds) {
        for (String expected : expecteds) {
            assertTrue(s + " must contain " + expected, s.contains(expected));
        }
    }

    public static byte[] readFully(InputStream is) throws IOException {
        int read;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((read = is.read(buffer)) != -1) {
            baos.write(buffer, 0, read);
        }

        return baos.toByteArray();
    }
}

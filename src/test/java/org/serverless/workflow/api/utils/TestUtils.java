/*
 *
 *   Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.serverless.workflow.api.utils;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {

    private static final Path resourceDirectory = Paths.get("src",
                                                            "test",
                                                            "resources");
    private static final String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    public static Path getResourcePath(String file) {
        return Paths.get(absolutePath + File.separator + file);
    }

    public static InputStream getInputStreamFromPath(Path path) throws Exception {
        return Files.newInputStream(path);
    }
}

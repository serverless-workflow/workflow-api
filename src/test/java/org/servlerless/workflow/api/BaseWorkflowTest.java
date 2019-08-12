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

package org.servlerless.workflow.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeAll;
import org.serverless.workflow.api.Workflow;
import org.serverless.workflow.api.WorkflowObjectMapper;

public class BaseWorkflowTest {

    private static WorkflowObjectMapper objectMapper;
    Path resourceDirectory = Paths.get("src",
                                       "test",
                                       "resources");
    String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    @BeforeAll
    public static void init() {
        objectMapper = new WorkflowObjectMapper();
    }

    public String getResourcePathFor(String file) {
        return absolutePath + File.separator + file;
    }

    public Path getResourcePath(String file) {
        return Paths.get(absolutePath + File.separator + file);
    }

    public String toJsonString(Workflow workflow) {
        try {
            return objectMapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public Workflow toWorkflow(String json) {
        try {
            return objectMapper.readValue(json,
                                          Workflow.class);
        } catch (Exception e) {
            return null;
        }
    }

    public JsonNode toJson(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getFileContents(final Path path) {
        try {
            return new String(Files.readAllBytes(path),
                              StandardCharsets.UTF_8);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}
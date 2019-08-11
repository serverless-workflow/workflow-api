package org.servlerless.workflow;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.BeforeAll;
import org.serverless.workflow.Workflow;
import org.serverless.workflow.WorkflowObjectMapper;

public class BaseWorkflowTest {
    private static WorkflowObjectMapper objectMapper;
    Path resourceDirectory = Paths.get("src", "test", "resources");
    String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    @BeforeAll
    public static void init() {
        objectMapper = new WorkflowObjectMapper();
    }

    public String getResourcePathFor(String file) {
        return absolutePath + File.separator + file;
    }

    public String toJsonString(Workflow workflow) {
        try {
            return objectMapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            System.out.println("*********** ERROR: " + e.getMessage());
            return null;
        }

    }

    public Workflow toWorkflow(String json) {
        try {
            return objectMapper.readValue(json, Workflow.class);
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
}
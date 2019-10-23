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

package org.serverless.workflow.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.serverless.workflow.api.mapper.JsonObjectMapper;
import org.serverless.workflow.api.mapper.YamlObjectMapper;
import org.serverless.workflow.api.states.EndState;
import org.serverless.workflow.spi.WorkflowPropertySourceProvider;

public class ObjectMapperTest {

    private static final String testEndState = "{\n" +
            "  \"name\": \"test-wf\",\n" +
            "  \"states\": [\n" +
            "    {\n" +
            "      \"status\": \"SUCCESS\",\n" +
            "      \"name\": \"test-state\",\n" +
            "      \"type\": \"END\",\n" +
            "      \"start\": false\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private static final String testEndStatePropertySource = "{\n" +
            "  \"name\": \"wfname\",\n" +
            "  \"states\": [\n" +
            "    {\n" +
            "      \"status\": \"endstate.status\",\n" +
            "      \"name\": \"endstate.name\",\n" +
            "      \"type\": \"endstate.type\",\n" +
            "      \"start\": false\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private static final String testEndStateYaml = "name: test-wf\n" +
            "states:\n" +
            "- status: SUCCESS\n" +
            "  name: test-state\n" +
            "  type: END\n" +
            "  start: false";

    private static final String testEndStateYamlPropertySource= "name: wfname\n" +
            "states:\n" +
            "- status: endstate.status\n" +
            "  name: endstate.name\n" +
            "  type: endstate.type\n" +
            "  start: false";

    @Test
    public void testReadJson() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        JsonNode node = mapper.readTree(testEndState);
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test-wf",
                                node.get("name").textValue());
    }

    @Test
    public void testReadJsonToWorkflow() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper();

        Workflow workflow = mapper.readValue(testEndState,
                                             Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());
        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof EndState);
        EndState endstate = (EndState) workflow.getStates().get(0);
        Assertions.assertEquals("test-state",
                                endstate.getName());
        Assertions.assertEquals(EndState.Status.SUCCESS,
                                endstate.getStatus());
    }

    @Test
    public void testReadYaml() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper();
        JsonNode node = mapper.readTree(testEndStateYaml);
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test-wf",
                                node.get("name").textValue());
    }

    @Test
    public void testReadYamlToWorkflow() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper();

        Workflow workflow = mapper.readValue(testEndStateYaml,
                                             Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());

        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof EndState);
        EndState endstate = (EndState) workflow.getStates().get(0);
        Assertions.assertEquals("test-state",
                                endstate.getName());
        Assertions.assertEquals(EndState.Status.SUCCESS,
                                endstate.getStatus());
    }

    @Test
    public void testReadJsonPropertySource() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        JsonNode node = mapper.readTree(testEndStatePropertySource);

        Workflow workflow = mapper.treeToValue(node,
                                               Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());

        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof EndState);
        EndState endstate = (EndState) workflow.getStates().get(0);
        Assertions.assertEquals("test-state",
                                endstate.getName());
        Assertions.assertEquals(EndState.Status.SUCCESS,
                                endstate.getStatus());
    }

    @Test
    public void testReadJsonToWorkflowPropertySource() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        Workflow workflow = mapper.readValue(testEndStatePropertySource,
                                             Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());
        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof EndState);
        EndState endstate = (EndState) workflow.getStates().get(0);
        Assertions.assertEquals("test-state",
                                endstate.getName());
        Assertions.assertEquals(EndState.Status.SUCCESS,
                                endstate.getStatus());
    }

    @Test
    public void testReadYamlPropertySource() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper(WorkflowPropertySourceProvider.getInstance().get());
        JsonNode node = mapper.readTree(testEndStateYamlPropertySource);

        Workflow workflow = mapper.treeToValue(node,
                                               Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());

        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof EndState);
        EndState endstate = (EndState) workflow.getStates().get(0);
        Assertions.assertEquals("test-state",
                                endstate.getName());
        Assertions.assertEquals(EndState.Status.SUCCESS,
                                endstate.getStatus());
    }

    @Test
    public void testReadYamlToWorkflowPropertySource() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        Workflow workflow = mapper.readValue(testEndStateYamlPropertySource,
                                             Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());

        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof EndState);
        EndState endstate = (EndState) workflow.getStates().get(0);
        Assertions.assertEquals("test-state",
                                endstate.getName());
        Assertions.assertEquals(EndState.Status.SUCCESS,
                                endstate.getStatus());
    }

    @Test
    public void testGetWorkflowModule() throws Exception {
        JsonObjectMapper jsonObjectMapper = new JsonObjectMapper(WorkflowPropertySourceProvider.getInstance().get());
        YamlObjectMapper yamlObjectMapper = new YamlObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        Assertions.assertNotNull(jsonObjectMapper);
        Assertions.assertNotNull(jsonObjectMapper.getWorkflowModule());

        Assertions.assertNotNull(yamlObjectMapper);
        Assertions.assertNotNull(yamlObjectMapper.getWorkflowModule());

    }
}

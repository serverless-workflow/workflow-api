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

import java.nio.file.Path;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.serverless.workflow.api.mapper.JsonObjectMapper;
import org.serverless.workflow.api.mapper.YamlObjectMapper;
import org.serverless.workflow.api.states.EndState;
import org.serverless.workflow.api.utils.TestUtils;

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

    private static final String testEndStateInitContext = "{\n" +
            "  \"name\": \"$$.defaults.wfname\",\n" +
            "  \"states\": [\n" +
            "    {\n" +
            "      \"status\": \"$$.defaults.endstate.status\",\n" +
            "      \"name\": \"$$.defaults.endstate.name\",\n" +
            "      \"type\": \"$$.defaults.endstate.type\",\n" +
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

    private static final String testEndStateYamlInitContext = "name: $$.defaults.wfname\n" +
            "states:\n" +
            "- status: $$.defaults.endstate.status\n" +
            "  name: $$.defaults.endstate.name\n" +
            "  type: $$.defaults.endstate.type\n" +
            "  start: false";

    @Test
    public void testReadJson() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(getInputStreamBasedIntializationContext());

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
    public void testReadJsonInitContext() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(getInputStreamBasedIntializationContext());

        JsonNode node = mapper.readTree(testEndStateInitContext);

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
    public void testReadJsonToWorkflowInitContext() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(getInputStreamBasedIntializationContext());

        Workflow workflow = mapper.readValue(testEndStateInitContext,
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
    public void testReadYamlInitContext() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper(getInputStreamBasedIntializationContext());
        JsonNode node = mapper.readTree(testEndStateYamlInitContext);

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
    public void testReadYamlToWorkflowInitContext() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper(getInputStreamBasedIntializationContext());

        Workflow workflow = mapper.readValue(testEndStateYamlInitContext,
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

    private InitializingContext getInputStreamBasedIntializationContext() throws Exception {
        Path appPropertiesPath = TestUtils.getResourcePath("application.properties");
        InitializingContext context = new InitializingContext();
        context.setContext(TestUtils.getInputStreamFromPath(appPropertiesPath));

        return context;
    }
}

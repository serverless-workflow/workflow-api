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
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.serverless.workflow.api.mapper.WorkflowObjectMapper;
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

    private static final String testEndStateForInitializingContext = "{\n" +
            "  \"name\": \"$$.defaults.wfname\",\n" +
            "  \"states\": [\n" +
            "    {\n" +
            "      \"status\": \"SUCCESS\",\n" +
            "      \"name\": \"$$.defaults.endstate.name\",\n" +
            "      \"type\": \"END\",\n" +
            "      \"start\": false\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    public void testReadTreeToJson() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(null);

        JsonNode node = mapper.readTree(testEndState);
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test-wf",
                                node.get("name").textValue());
        Assertions.assertEquals("test-state",
                                node.get("states").get(0).get("name").textValue());
        Assertions.assertEquals("test-state",
                                node.get("states").get(0).get("name").textValue());
        Assertions.assertEquals("SUCCESS",
                                node.get("states").get(0).get("status").textValue());
    }

    @Test
    public void testReadTreeToWorkflow() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(null);

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
    public void testReadTreeToJsonWithStringInitializingContext() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(getStringBasedInitializngContext());

        JsonNode node = mapper.readTree(testEndStateForInitializingContext);
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
    public void testReadTreeToWorkflowWithStringInitializingContext() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(getStringBasedInitializngContext());

        Workflow workflow = mapper.readValue(testEndStateForInitializingContext,
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
    public void testReadTreeToJsonWithMapInitializingContext() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(getMapBasedInitializingContext());

        JsonNode node = mapper.readTree(testEndStateForInitializingContext);
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
    public void testReadTreeToWorkflowWithMapInitializingContext() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(getMapBasedInitializingContext());

        Workflow workflow = mapper.readValue(testEndStateForInitializingContext,
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
    public void testReadTreeToJsonWithPropertiesInitializingContext() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(getPropertiesBasedIntializationContext());

        JsonNode node = mapper.readTree(testEndStateForInitializingContext);
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
    public void testReadTreeToWorkflowWithPropertiesInitializingContext() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(getPropertiesBasedIntializationContext());

        Workflow workflow = mapper.readValue(testEndStateForInitializingContext,
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
    public void testReadTreeToJsonWithInputStreamInitializingContext() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(getInputStreamBasedIntializationContext());

        JsonNode node = mapper.readTree(testEndStateForInitializingContext);
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
    public void testReadTreeToWorkflowWithInputStreamInitializingContext() throws Exception {
        WorkflowObjectMapper mapper = new WorkflowObjectMapper(getInputStreamBasedIntializationContext());

        Workflow workflow = mapper.readValue(testEndStateForInitializingContext,
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

    private InitializingContext getStringBasedInitializngContext() {
        InitializingContext context = new InitializingContext();
        context.setContext("{  \n" +
                                   "   \"defaults\":{  \n" +
                                   "      \"wfname\":\"test-wf\",\n" +
                                   "      \"endstate\":{  \n" +
                                   "         \"name\":\"test-state\"\n" +
                                   "      }\n" +
                                   "   }\n" +
                                   "}");

        return context;
    }

    private InitializingContext getMapBasedInitializingContext() {
        InitializingContext context = new InitializingContext();
        Map<String, String> propMap = new HashMap<>();
        propMap.put("defaults.wfname",
                    "test-wf");
        propMap.put("defaults.endstate.name",
                    "test-state");
        context.setContext(propMap);

        return context;
    }

    private InitializingContext getPropertiesBasedIntializationContext() {
        InitializingContext context = new InitializingContext();
        Properties properties = new Properties();
        properties.put("defaults.wfname",
                       "test-wf");
        properties.put("defaults.endstate.name",
                       "test-state");
        context.setContext(properties);

        return context;
    }

    private InitializingContext getInputStreamBasedIntializationContext() throws Exception {
        Path appPropertiesPath = TestUtils.getResourcePath("application.properties");
        InitializingContext context = new InitializingContext();
        context.setContext(TestUtils.getInputStreamFromPath(appPropertiesPath));

        return context;
    }
}

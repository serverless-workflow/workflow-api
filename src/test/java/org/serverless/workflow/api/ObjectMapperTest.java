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
import org.serverless.workflow.api.states.DelayState;
import org.serverless.workflow.spi.WorkflowPropertySourceProvider;

public class ObjectMapperTest {

    private static final String testDelayState = "{\n" +
            "  \"name\": \"test-wf\",\n" +
            "  \"starts-at\": \"delay-state\",\n" +
            "  \"states\": [\n" +
            "    {\n" +
            "      \"name\": \"delay-state\",\n" +
            "      \"time-delay\": \"PT5S\",\n" +
            "      \"type\": \"DELAY\",\n" +
            "      \"end\": true\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private static final String testDelayStatePropertySource = "{\n" +
            "  \"name\": \"test-wf\",\n" +
            "  \"starts-at\": \"delay-state\",\n" +
            "  \"states\": [\n" +
            "    {\n" +
            "      \"name\": \"delaystate.name\",\n" +
            "      \"time-delay\": \"delaystate.timedelay\",\n" +
            "      \"type\": \"delaystate.type\",\n" +
            "      \"end\": true\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private static final String testDelayStateYaml =
            "name: test-wf\n" +
            "starts-at: delay-state\n" +
            "states:\n" +
            "- name: delay-state\n" +
            "  time-delay: PT5S\n" +
            "  type: DELAY\n" +
            "  end: true";

    private static final String testDelayStateYamlPropertySource =
            "name: test-wf\n" +
            "starts-at: delay-state\n" +
            "states:\n" +
            "- name: delaystate.name\n" +
            "  time-delay: delaystate.timedelay\n" +
            "  type: delaystate.type\n" +
            "  end: true";

    @Test
    public void testReadJson() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        JsonNode node = mapper.readTree(testDelayState);
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test-wf",
                                node.get("name").textValue());
    }

    @Test
    public void testReadJsonToWorkflow() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper();

        Workflow workflow = mapper.readValue(testDelayState,
                                             Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());
        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof DelayState);
        DelayState delayState = (DelayState) workflow.getStates().get(0);
        Assertions.assertEquals("delay-state",
                                delayState.getName());
        Assertions.assertEquals("PT5S",
                                delayState.getTimeDelay());
        Assertions.assertTrue(delayState.isEnd());
    }

    @Test
    public void testReadYaml() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper();
        JsonNode node = mapper.readTree(testDelayStateYaml);
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test-wf",
                                node.get("name").textValue());
    }

    @Test
    public void testReadYamlToWorkflow() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper();

        Workflow workflow = mapper.readValue(testDelayStateYaml,
                                             Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());
        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof DelayState);
        DelayState delayState = (DelayState) workflow.getStates().get(0);
        Assertions.assertEquals("delay-state",
                                delayState.getName());
        Assertions.assertEquals("PT5S",
                                delayState.getTimeDelay());
        Assertions.assertTrue(delayState.isEnd());
    }

    @Test
    public void testReadJsonPropertySource() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        JsonNode node = mapper.readTree(testDelayStatePropertySource);

        Workflow workflow = mapper.treeToValue(node,
                                               Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());
        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof DelayState);
        DelayState delayState = (DelayState) workflow.getStates().get(0);
        Assertions.assertEquals("delay-state",
                                delayState.getName());
        Assertions.assertEquals("PT5S",
                                delayState.getTimeDelay());
        Assertions.assertTrue(delayState.isEnd());
    }

    @Test
    public void testReadJsonToWorkflowPropertySource() throws Exception {
        JsonObjectMapper mapper = new JsonObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        Workflow workflow = mapper.readValue(testDelayStatePropertySource,
                                             Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());
        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof DelayState);
        DelayState delayState = (DelayState) workflow.getStates().get(0);
        Assertions.assertEquals("delay-state",
                                delayState.getName());
        Assertions.assertEquals("PT5S",
                                delayState.getTimeDelay());
        Assertions.assertTrue(delayState.isEnd());
    }

    @Test
    public void testReadYamlPropertySource() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper(WorkflowPropertySourceProvider.getInstance().get());
        JsonNode node = mapper.readTree(testDelayStateYamlPropertySource);

        Workflow workflow = mapper.treeToValue(node,
                                               Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());
        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof DelayState);
        DelayState delayState = (DelayState) workflow.getStates().get(0);
        Assertions.assertEquals("delay-state",
                                delayState.getName());
        Assertions.assertEquals("PT5S",
                                delayState.getTimeDelay());
        Assertions.assertTrue(delayState.isEnd());
    }

    @Test
    public void testReadYamlToWorkflowPropertySource() throws Exception {
        YamlObjectMapper mapper = new YamlObjectMapper(WorkflowPropertySourceProvider.getInstance().get());

        Workflow workflow = mapper.readValue(testDelayStateYamlPropertySource,
                                             Workflow.class);

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());

        Assertions.assertNotNull(workflow);
        Assertions.assertEquals("test-wf",
                                workflow.getName());
        Assertions.assertNotNull(workflow.getStates());
        Assertions.assertEquals(1,
                                workflow.getStates().size());
        Assertions.assertTrue(workflow.getStates().get(0) instanceof DelayState);
        DelayState delayState = (DelayState) workflow.getStates().get(0);
        Assertions.assertEquals("delay-state",
                                delayState.getName());
        Assertions.assertEquals("PT5S",
                                delayState.getTimeDelay());
        Assertions.assertTrue(delayState.isEnd());
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

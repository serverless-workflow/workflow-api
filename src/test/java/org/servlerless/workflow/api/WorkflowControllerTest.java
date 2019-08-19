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

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.serverless.workflow.api.Workflow;
import org.serverless.workflow.api.WorkflowController;
import org.serverless.workflow.api.actions.Action;
import org.serverless.workflow.api.choices.AndChoice;
import org.serverless.workflow.api.choices.DefaultChoice;
import org.serverless.workflow.api.interfaces.Choice;
import org.serverless.workflow.api.interfaces.State;
import org.serverless.workflow.api.states.EndState;
import org.serverless.workflow.api.states.EventState;
import org.serverless.workflow.api.states.OperationState;
import org.serverless.workflow.api.states.SwitchState;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkflowControllerTest {

    @Test
    public void testManagerFromJson() {

        String testString = "{\n" +
                "  \"id\" : \"1\",\n" +
                "  \"states\" : [ {\n" +
                "    \"action-mode\" : \"SEQUENTIAL\",\n" +
                "    \"actions\" : [ {\n" +
                "      \"function\" : {\n" +
                "        \"name\" : \"testFunction\"\n" +
                "      },\n" +
                "      \"timeout\" : 5,\n" +
                "      \"retry\" : {\n" +
                "        \"match\" : \"testMatch\",\n" +
                "        \"retry-interval\" : 2,\n" +
                "        \"max-retry\" : 10,\n" +
                "        \"next-state\" : \"testNextRetryState\"\n" +
                "      }\n" +
                "    } ],\n" +
                "    \"next-state\" : \"testnextstate\",\n" +
                "    \"id\" : \"2\",\n" +
                "    \"name\" : \"operationstate\",\n" +
                "    \"type\" : \"OPERATION\",\n" +
                "    \"start\" : true\n" +
                "  } ]\n" +
                "}";

        WorkflowController controller = new WorkflowController(testString);
        assertTrue(controller.isValid());

        Workflow workflow = controller.getWorkflow();
        assertNotNull(workflow);

        // set the id for tests
        workflow.setId("1");

        assertThat(workflow.getTriggerDefs().size(),
                   is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(),
                   is(1));
        assertTrue(workflow.getStates().get(0) instanceof OperationState);

        OperationState operationState = (OperationState) workflow.getStates().get(0);
        // set the id for test
        operationState.setId("2");
        assertEquals("testnextstate",
                     operationState.getNextState());
        assertEquals("operationstate",
                     operationState.getName());
        assertEquals(EventState.Type.OPERATION,
                     operationState.getType());

        assertNotNull(operationState.getActions());
        assertEquals(1,
                     operationState.getActions().size());

        Action action = operationState.getActions().get(0);
        assertEquals("testFunction",
                     action.getFunction().getName());
        assertNotNull(action.getRetry());
        assertEquals("testMatch",
                     action.getRetry().getMatch());

        assertEquals(testString, new WorkflowController(workflow).toJsonString());

    }

    @Test
    public void testManagerFromWorkflow() {
        String testString = "{\n" +
                "  \"id\" : \"1\",\n" +
                "  \"states\" : [ {\n" +
                "    \"choices\" : [ {\n" +
                "      \"And\" : [ {\n" +
                "        \"path\" : \"testpath\",\n" +
                "        \"value\" : \"testvalue\",\n" +
                "        \"operator\" : \"EQ\",\n" +
                "        \"next-state\" : \"testnextstate\"\n" +
                "      } ],\n" +
                "      \"next-state\" : \"testnextstate\"\n" +
                "    } ],\n" +
                "    \"default\" : \"defaultteststate\",\n" +
                "    \"id\" : \"2\",\n" +
                "    \"name\" : \"switchstate\",\n" +
                "    \"type\" : \"SWITCH\",\n" +
                "    \"start\" : true\n" +
                "  }, {\n" +
                "    \"status\" : \"SUCCESS\",\n" +
                "    \"id\" : \"3\",\n" +
                "    \"name\" : \"endstate\",\n" +
                "    \"type\" : \"END\",\n" +
                "    \"start\" : false\n" +
                "  } ]\n" +
                "}";

        Workflow workflow = new Workflow().withId("1").withStates(new ArrayList<State>() {{
            add(
                    new SwitchState().withId("2").withDefault("defaultteststate").withStart(true).withChoices(
                            new ArrayList<Choice>() {{
                                add(
                                        new AndChoice().withNextState("testnextstate").withAnd(
                                                Arrays.asList(
                                                        new DefaultChoice().withNextState("testnextstate")
                                                                .withOperator(DefaultChoice.Operator.EQ)
                                                                .withPath("testpath")
                                                                .withValue("testvalue")
                                                )
                                        )
                                );
                            }}
                    )
            );
            add(new EndState().withId("3").withStatus(EndState.Status.SUCCESS));
        }});

        WorkflowController controller = new WorkflowController(workflow);

        assertTrue(controller.isValid());
        assertEquals(testString, controller.toJsonString());

    }
}

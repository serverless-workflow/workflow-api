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
import java.util.List;

import org.junit.jupiter.api.Test;
import org.serverless.workflow.api.Workflow;
import org.serverless.workflow.api.WorkflowController;
import org.serverless.workflow.api.actions.Action;
import org.serverless.workflow.api.actions.Retry;
import org.serverless.workflow.api.events.Event;
import org.serverless.workflow.api.events.TriggerEvent;
import org.serverless.workflow.api.functions.Function;
import org.serverless.workflow.api.interfaces.State;
import org.serverless.workflow.api.states.EventState;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.servlerless.workflow.api.util.IsEqualJSON.equalToJSONInFile;

public class WorkflowControllerTest extends BaseWorkflowTest {

    @Test
    public void testManagerFromJson() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("controller/eventstatewithtrigger.json")));

        WorkflowController controller = new WorkflowController().forWorkflow(workflow);

        assertTrue(controller.isValid());

        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(),
                   is(1));
        assertTrue(workflow.getStates().get(0) instanceof EventState);

        EventState eventState = (EventState) workflow.getStates().get(0);
        assertEquals("eventstate",
                     eventState.getName());
        assertEquals(EventState.Type.EVENT,
                     eventState.getType());

        assertNotNull(eventState.getEvents());
        assertEquals(1,
                     eventState.getEvents().size());

        Event event = eventState.getEvents().get(0);
        assertEquals("testNextState",
                     event.getNextState());
        assertNotNull(event.getActions());

        assertEquals(1,
                     event.getActions().size());

        assertNotNull(workflow.getTriggerDefs());
        assertEquals(1,
                     workflow.getTriggerDefs().size());

        assertTrue(controller.haveTriggers());

        assertTrue(controller.haveStates());

        assertEquals(1,
                     controller.getUniqueStates().size());
        assertEquals(1,
                     controller.getUniqueTriggerEvents().size());

        TriggerEvent triggerEvent = controller.getUniqueTriggerEvents().get("2");
        assertNotNull(triggerEvent);

        List<EventState> eventStatesForTrigger = controller.getEventStatesForTriggerEvent(triggerEvent);
        assertNotNull(eventStatesForTrigger);
        assertEquals(1,
                     eventStatesForTrigger.size());
        EventState eventStateForTrigger = eventStatesForTrigger.get(0);
        assertEquals("3",
                     eventStateForTrigger.getId());
    }

    @Test
    public void testManagerFromWorkflow() {
        Workflow workflow = new Workflow().withId("1")
                .withTriggerDefs(
                        Arrays.asList(
                                new TriggerEvent().withId("2").withName("testtrigger").withEventID("testeventid")
                                        .withCorrelationToken("testcorrelationtoken").withSource("testsource")
                        )
                )
                .withStates(new ArrayList<State>() {{
                    add(new EventState().withId("3").withStart(true).withName("eventstate").withType(EventState.Type.EVENT)
                                .withEvents(Arrays.asList(
                                        new Event().withEventExpression("trigger.equals(\"testtrigger\")").withTimeout("testTimeout")
                                                .withActionMode(Event.ActionMode.SEQUENTIAL)
                                                .withNextState("testNextState")
                                                .withActions(Arrays.asList(
                                                        new Action().withFunction(new Function().withName("testFunction"))
                                                                .withTimeout(5)
                                                                .withRetry(new Retry().withMatch("testMatch").withMaxRetry(10)
                                                                                   .withRetryInterval(2)
                                                                                   .withNextState("testNextRetryState"))
                                                ))
                                )));
                }});

        WorkflowController controller = new WorkflowController().forWorkflow(workflow);

        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(),
                   is(1));
        assertTrue(workflow.getStates().get(0) instanceof EventState);

        EventState eventState = (EventState) workflow.getStates().get(0);
        assertEquals("eventstate",
                     eventState.getName());
        assertEquals(EventState.Type.EVENT,
                     eventState.getType());

        assertNotNull(eventState.getEvents());
        assertEquals(1,
                     eventState.getEvents().size());

        Event event = eventState.getEvents().get(0);
        assertEquals("testNextState",
                     event.getNextState());
        assertNotNull(event.getActions());

        assertEquals(1,
                     event.getActions().size());

        assertNotNull(workflow.getTriggerDefs());
        assertEquals(1,
                     workflow.getTriggerDefs().size());

        assertTrue(controller.haveTriggers());

        assertTrue(controller.haveStates());

        assertEquals(1,
                     controller.getUniqueStates().size());
        assertEquals(1,
                     controller.getUniqueTriggerEvents().size());

        TriggerEvent triggerEvent = controller.getUniqueTriggerEvents().get("2");
        assertNotNull(triggerEvent);

        List<EventState> eventStatesForTrigger = controller.getEventStatesForTriggerEvent(triggerEvent);
        assertNotNull(eventStatesForTrigger);
        assertEquals(1,
                     eventStatesForTrigger.size());
        EventState eventStateForTrigger = eventStatesForTrigger.get(0);
        assertEquals("3",
                     eventStateForTrigger.getId());

        assertThat(controller.toJsonString(),
                   equalToJSONInFile(getResourcePathFor("controller/eventstatewithtrigger.json")));
    }
}

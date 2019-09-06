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

import java.util.List;

import org.junit.jupiter.api.Test;
import org.serverless.workflow.api.SpelExpressionEvaluator;
import org.serverless.workflow.api.Workflow;
import org.serverless.workflow.api.WorkflowController;
import org.serverless.workflow.api.events.TriggerEvent;
import org.serverless.workflow.api.states.EventState;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpressionEvaluationTest extends BaseWorkflowTest {

    @Test
    public void testEventStateJexlExpressions() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("expressions/eventstatestriggers-jexl.json")));

        WorkflowController controller = new WorkflowController().forWorkflow(workflow);
        assertTrue(controller.isValid());

        assertTrue(controller.haveTriggers());
        assertTrue(controller.haveStates());

        assertThat(workflow.getTriggerDefs().size(),
                   is(3));

        assertThat(workflow.getStates().size(),
                   is(2));

        List<EventState> eventStatesForTrigger1 = controller.getEventStatesForTriggerEvent(controller.getUniqueTriggerEvents().get("2"));
        assertNotNull(eventStatesForTrigger1);
        assertEquals(2,
                     eventStatesForTrigger1.size());
        EventState eventStateForTrigger1 = eventStatesForTrigger1.get(0);
        assertEquals("5",
                     eventStateForTrigger1.getId());
        EventState eventStateForTrigger2 = eventStatesForTrigger1.get(1);
        assertEquals("6",
                     eventStateForTrigger2.getId());

        List<EventState> eventStatesForTrigger2 = controller.getEventStatesForTriggerEvent(controller.getUniqueTriggerEvents().get("3"));
        assertNotNull(eventStatesForTrigger2);
        assertEquals(1,
                     eventStatesForTrigger2.size());
        EventState eventStateForTrigger3 = eventStatesForTrigger2.get(0);
        assertEquals("6",
                     eventStateForTrigger3.getId());

        List<TriggerEvent> triggerEvents1 = controller.getTriggerEventsForEventState((EventState) controller.getUniqueStates().get("5"));
        assertNotNull(triggerEvents1);
        assertThat(triggerEvents1.size(),
                   is(1));
        assertEquals("2",
                     triggerEvents1.get(0).getId());

        List<TriggerEvent> triggerEvents2 = controller.getTriggerEventsForEventState((EventState) controller.getUniqueStates().get("6"));
        assertNotNull(triggerEvents2);
        assertThat(triggerEvents2.size(),
                   is(2));
        assertEquals("2",
                     triggerEvents2.get(0).getId());
        assertEquals("3",
                     triggerEvents2.get(1).getId());

        List<TriggerEvent> triggerEvents3 = controller.getAllTriggerEventsAssociatedWithEventStates();
        assertNotNull(triggerEvents3);
        assertEquals(2,
                     triggerEvents3.size());
        assertEquals("2",
                     triggerEvents3.get(0).getId());
        assertEquals("3",
                     triggerEvents3.get(1).getId());
    }

    @Test
    public void testEventStateSpelExpressions() {
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("expressions/eventstatestriggers-spel.json")));

        WorkflowController controller = new WorkflowController()
                .forWorkflow(workflow)
                .withExpressionEvaluator(new SpelExpressionEvaluator());
        assertTrue(controller.isValid());

        assertTrue(controller.haveTriggers());
        assertTrue(controller.haveStates());

        assertThat(workflow.getTriggerDefs().size(),
                   is(3));

        assertThat(workflow.getStates().size(),
                   is(2));

        List<EventState> eventStatesForTrigger1 = controller.getEventStatesForTriggerEvent(controller.getUniqueTriggerEvents().get("2"));
        assertNotNull(eventStatesForTrigger1);
        assertEquals(2,
                     eventStatesForTrigger1.size());
        EventState eventStateForTrigger1 = eventStatesForTrigger1.get(0);
        assertEquals("5",
                     eventStateForTrigger1.getId());
        EventState eventStateForTrigger2 = eventStatesForTrigger1.get(1);
        assertEquals("6",
                     eventStateForTrigger2.getId());

        List<EventState> eventStatesForTrigger2 = controller.getEventStatesForTriggerEvent(controller.getUniqueTriggerEvents().get("3"));
        assertNotNull(eventStatesForTrigger2);
        assertEquals(1,
                     eventStatesForTrigger2.size());
        EventState eventStateForTrigger3 = eventStatesForTrigger2.get(0);
        assertEquals("6",
                     eventStateForTrigger3.getId());

        List<TriggerEvent> triggerEvents1 = controller.getTriggerEventsForEventState((EventState) controller.getUniqueStates().get("5"));
        assertNotNull(triggerEvents1);
        assertThat(triggerEvents1.size(),
                   is(1));
        assertEquals("2",
                     triggerEvents1.get(0).getId());

        List<TriggerEvent> triggerEvents2 = controller.getTriggerEventsForEventState((EventState) controller.getUniqueStates().get("6"));
        assertNotNull(triggerEvents2);
        assertThat(triggerEvents2.size(),
                   is(2));
        assertEquals("2",
                     triggerEvents2.get(0).getId());
        assertEquals("3",
                     triggerEvents2.get(1).getId());

        List<TriggerEvent> triggerEvents3 = controller.getAllTriggerEventsAssociatedWithEventStates();
        assertNotNull(triggerEvents3);
        assertEquals(2,
                     triggerEvents3.size());
        assertEquals("2",
                     triggerEvents3.get(0).getId());
        assertEquals("3",
                     triggerEvents3.get(1).getId());
    }
}
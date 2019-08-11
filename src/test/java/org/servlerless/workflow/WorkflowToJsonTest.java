package org.servlerless.workflow;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.serverless.workflow.Workflow;
import org.serverless.workflow.actions.Action;
import org.serverless.workflow.actions.Retry;
import org.serverless.workflow.events.Event;
import org.serverless.workflow.interfaces.State;
import org.serverless.workflow.states.EndState;
import org.serverless.workflow.states.EndState.Status;
import org.serverless.workflow.states.EventState;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.servlerless.workflow.util.IsEqualJSON.*;


public class WorkflowToJsonTest extends BaseWorkflowTest {

    @Test
    public void emptyWorkflowTest() {
        Workflow workflow = new Workflow();

        assertNotNull(toJsonString(workflow));
        assertThat(toJsonString(workflow), equalToJSONInFile(getResourcePathFor("emptyworkflow.json")));
    }

    @Test
    public void endStateTest() {

        Workflow workflow = new Workflow().withStates(new ArrayList<State>() {{
            add(new EndState().withStatus(Status.SUCCESS));
        }});

        assertNotNull(toJsonString(workflow));
        assertThat(toJsonString(workflow), equalToJSONInFile(getResourcePathFor("singleendstate.json")));

    }

    @Test
    public void eventStateTest() {

        Workflow workflow = new Workflow().withStates(new ArrayList<State>() {{
            add(new EventState().withName("eventState").withStart(true)
                        .withEvents(Arrays.asList(
                                new Event().withEventExpression("testEventExpression").withTimeout("testTimeout")
                                        .withActionMode(Event.ActionMode.SEQUENTIAL)
                                        .withNextState("testNextState")
                                        .withActions(Arrays.asList(
                                                new Action().withFunction("testFunction")
                                                        .withTimeout(5)
                                                        .withRetry( new Retry().withMatch("testMatch").withMaxRetry(10)
                                                                            .withRetryInterval(2)
                                                                            .withNextState("testNextRetryState"))
                                        ))
                        )));
        }});

        assertNotNull(toJsonString(workflow));
        assertThat(toJsonString(workflow), equalToJSONInFile(getResourcePathFor("singlestateevent.json")));


    }
}
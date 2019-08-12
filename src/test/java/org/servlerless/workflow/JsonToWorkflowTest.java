package org.servlerless.workflow;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.serverless.workflow.Workflow;
import org.serverless.workflow.actions.Action;
import org.serverless.workflow.actions.Retry;
import org.serverless.workflow.branches.Branch;
import org.serverless.workflow.choices.AndChoice;
import org.serverless.workflow.choices.NotChoice;
import org.serverless.workflow.choices.OrChoice;
import org.serverless.workflow.events.Event;
import org.serverless.workflow.interfaces.State;
import org.serverless.workflow.states.DefaultState;
import org.serverless.workflow.states.DelayState;
import org.serverless.workflow.states.EndState;
import org.serverless.workflow.states.EventState;
import org.serverless.workflow.states.OperationState;
import org.serverless.workflow.states.ParallelState;
import org.serverless.workflow.states.SwitchState;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.servlerless.workflow.util.IsEqualJSON.equalToJSONInFile;

public class JsonToWorkflowTest extends BaseWorkflowTest {

    @Test
    public void testEmptyWorkflow() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("emptyworkflow.json")));

        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(0));
    }

    @Test
    public void testSimpleWorkflowWithInfo() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("workflowwithinfo.json")));
        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(0));

        assertEquals("testuid", workflow.getId());
        assertEquals("testname", workflow.getName());
        assertEquals("testversion", workflow.getVersion());
        assertEquals("testdescription", workflow.getDescription());
    }

    @Test
    public void testTrigger() {
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("singletriggerevent.json")));
        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(1));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(0));

        assertEquals("testtriggerevent", workflow.getTriggerDefs().get(0).getName());
        assertEquals("testsource", workflow.getTriggerDefs().get(0).getSource());
        assertEquals("testeventid", workflow.getTriggerDefs().get(0).getEventID());
        assertEquals("testcorrelationtoken", workflow.getTriggerDefs().get(0).getCorrelationToken());

    }

    @Test
    public void testEndState() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("singleendstate.json")));
        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(1));
        assertTrue(workflow.getStates().get(0) instanceof EndState);

        EndState endState = (EndState) workflow.getStates().get(0);
        assertEquals(EndState.Status.SUCCESS, endState.getStatus());
        assertFalse(endState.isStart());
        assertEquals(EndState.Type.END, endState.getType());
        assertEquals("endstate", endState.getName());

    }

    @Test
    public void testEventState() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("singlestateevent.json")));
        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(1));
        assertTrue(workflow.getStates().get(0) instanceof EventState);

        EventState eventState = (EventState) workflow.getStates().get(0);
        assertEquals("eventstate", eventState.getName());
        assertTrue(eventState.isStart());
        assertEquals(EventState.Type.EVENT, eventState.getType());

        assertNotNull(eventState.getEvents());
        assertEquals(1, eventState.getEvents().size());

        Event event = eventState.getEvents().get(0);
        assertEquals("testNextState", event.getNextState());
        assertEquals("testEventExpression", event.getEventExpression());
        assertEquals(Event.ActionMode.SEQUENTIAL, event.getActionMode());

        assertNotNull(event.getActions());
        assertEquals(1, event.getActions().size());
        assertEquals("testFunction", event.getActions().get(0).getFunction());
        assertNotNull(event.getActions().get(0).getRetry());

        assertEquals("testMatch", event.getActions().get(0).getRetry().getMatch());

    }

    @Test
    public void testDelayState() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("singledelaystate.json")));
        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(1));
        assertTrue(workflow.getStates().get(0) instanceof DelayState);

        DelayState delayState = (DelayState) workflow.getStates().get(0);
        assertEquals("testNextState", delayState.getNextState());
        assertEquals("delaystate", delayState.getName());
        assertEquals(EventState.Type.DELAY, delayState.getType());
    }


    @Test
    public void testOperationState() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("singleoperationstate.json")));
        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(1));
        assertTrue(workflow.getStates().get(0) instanceof OperationState);

        OperationState operationState = (OperationState) workflow.getStates().get(0);
        assertEquals("testnextstate", operationState.getNextState());
        assertEquals("operationstate", operationState.getName());
        assertEquals(EventState.Type.OPERATION, operationState.getType());

        assertNotNull(operationState.getActions());
        assertEquals(1, operationState.getActions().size());

        Action action = operationState.getActions().get(0);
        assertEquals("testFunction", action.getFunction());
        assertNotNull(action.getRetry());
        assertEquals("testMatch", action.getRetry().getMatch());

    }

    @Test
    public void testParallellState() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("singleparallelstate.json")));
        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(1));
        assertTrue(workflow.getStates().get(0) instanceof ParallelState);

        ParallelState parallelState = (ParallelState) workflow.getStates().get(0);

        assertEquals("testnextstate", parallelState.getNextState());
        assertEquals("parallelstate", parallelState.getName());
        assertEquals(EventState.Type.PARALLEL, parallelState.getType());

        assertNotNull(parallelState.getBranches());
        assertEquals(2, parallelState.getBranches().size());

        Branch branch1 = parallelState.getBranches().get(0);
        assertEquals("firsttestbranch", branch1.getName());
        assertNotNull(branch1.getStates());
        assertEquals(1, branch1.getStates().size());
        assertTrue(branch1.getStates().get(0) instanceof OperationState);
        assertEquals("operationstate", ((OperationState) branch1.getStates().get(0)).getName());
        assertEquals(1, ((OperationState) branch1.getStates().get(0)).getActions().size());
        assertEquals("testFunction", ((OperationState) branch1.getStates().get(0)).getActions().get(0).getFunction());


        Branch branch2 = parallelState.getBranches().get(1);
        assertEquals("secondtestbranch", branch2.getName());
        assertNotNull(branch2.getStates());
        assertEquals(1, branch2.getStates().size());
        assertTrue(branch2.getStates().get(0) instanceof DelayState);
        assertEquals("delaystate", ((DelayState) branch2.getStates().get(0)).getName());
        assertEquals("testNextState", ((DelayState) branch2.getStates().get(0)).getNextState());
        assertEquals(5, ((DelayState) branch2.getStates().get(0)).getTimeDelay());

    }



    @Test
    public void testSwitchState() {

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("singleswitchstateandchoice.json")));
        assertNotNull(workflow);
        assertThat(workflow.getTriggerDefs().size(), is(0));
        assertNotNull(workflow.getStates());
        assertThat(workflow.getStates().size(), is(1));
        assertTrue(workflow.getStates().get(0) instanceof SwitchState);

        SwitchState switchState = (SwitchState) workflow.getStates().get(0);
        assertEquals("switchstate", switchState.getName());
        assertEquals(DefaultState.Type.SWITCH, switchState.getType());
        assertEquals("defaultteststate", switchState.getDefault());

        assertNotNull(switchState.getChoices());
        assertThat(switchState.getChoices().size(), is(1));
        assertTrue(switchState.getChoices().get(0) instanceof AndChoice);


        workflow = toWorkflow(getFileContents(getResourcePath("singleswitchstatenotchoice.json")));
        assertNotNull(workflow);
        switchState = (SwitchState) workflow.getStates().get(0);
        assertTrue(switchState.getChoices().get(0) instanceof NotChoice);

        workflow = toWorkflow(getFileContents(getResourcePath("singleswitchstateorchoice.json")));
        assertNotNull(workflow);
        switchState = (SwitchState) workflow.getStates().get(0);
        assertTrue(switchState.getChoices().get(0) instanceof OrChoice);
    }
}

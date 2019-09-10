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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.serverless.workflow.api.actions.Action;
import org.serverless.workflow.api.events.Event;
import org.serverless.workflow.api.events.TriggerEvent;
import org.serverless.workflow.api.functions.Function;
import org.serverless.workflow.api.interfaces.State;
import org.serverless.workflow.api.states.EndState;
import org.serverless.workflow.api.states.EventState;

public abstract class WorkflowAdvice {

    private Map<String, TriggerEvent> triggerEvenMap = new HashMap<>();
    private Map<String, State> stateMap = new HashMap<>();

    protected abstract Workflow getWorkflow();

    protected abstract ExpressionEvaluator getExpressionEvaluator();

    public Map<String, TriggerEvent> getUniqueTriggerEvents() {
        if (getWorkflow().getTriggerDefs() != null) {
            triggerEvenMap = getWorkflow().getTriggerDefs().stream()
                .collect(Collectors.toMap(TriggerEvent::getName,
                                          tiggerEvent -> tiggerEvent));
        }

        return triggerEvenMap;
    }

    public boolean haveTriggers() {
        return getWorkflow().getTriggerDefs() != null && !getWorkflow().getTriggerDefs().isEmpty();
    }

    public boolean haveStates() {
        return getWorkflow().getStates() != null && !getWorkflow().getStates().isEmpty();
    }

    public Map<String, State> getUniqueStates() {
        if (getWorkflow().getTriggerDefs() != null) {
            stateMap = getWorkflow().getStates().stream()
                .collect(Collectors.toMap(State::getName,
                                          state -> state));
        }

        return stateMap;
    }

    public List<EventState> getEventStatesForTriggerEvent(TriggerEvent triggerEvent) {
        List<EventState> triggerStates = new ArrayList<>();

        for (State state : getWorkflow().getStates()) {
            if (state instanceof EventState) {
                EventState eventState = (EventState) state;
                List<Event> triggeredEvents = eventState.getEvents().stream()
                    .filter(event -> getExpressionEvaluator()
                        .evaluate(event.getEventExpression(),
                                  triggerEvent.getName())).collect(Collectors.toList());
                if (triggeredEvents != null && !triggeredEvents.isEmpty()) {
                    triggerStates.add(eventState);
                }
            }
        }

        return triggerStates;
    }

    public List<TriggerEvent> getTriggerEventsForEventState(EventState eventState) {
        List<TriggerEvent> eventStateTriggers = new ArrayList<>();

        for (TriggerEvent triggerEvent : getWorkflow().getTriggerDefs()) {
            List<Event> triggeredEvents = eventState.getEvents().stream()
                .filter(event -> getExpressionEvaluator()
                    .evaluate(event.getEventExpression(),
                              triggerEvent.getName())).collect(Collectors.toList());

            if (triggeredEvents != null && !triggeredEvents.isEmpty()) {
                eventStateTriggers.add(triggerEvent);
            }
        }

        return eventStateTriggers;
    }

    public List<TriggerEvent> getAllTriggerEventsAssociatedWithEventStates() {
        Map<String, TriggerEvent> associatedTriggersMap = new HashMap();
        for (State state : getWorkflow().getStates()) {
            if (state instanceof EventState) {
                EventState eventState = (EventState) state;
                for (TriggerEvent triggerEvent : getWorkflow().getTriggerDefs()) {
                    List<Event> triggeredEvents = eventState.getEvents().stream()
                        .filter(event -> getExpressionEvaluator()
                            .evaluate(event.getEventExpression(),
                                      triggerEvent.getName())).collect(Collectors.toList());
                    if (triggeredEvents != null && !triggeredEvents.isEmpty()) {
                        associatedTriggersMap.put(triggerEvent.getName(),
                                                  triggerEvent);
                    }
                }
            }
        }

        return new ArrayList<>(associatedTriggersMap.values());
    }

    // convenience method
    public List<Action> getAllActionsForEventState(EventState eventState) {
        List<Action> actions = new ArrayList<>();
        eventState.getEvents().stream().forEach(event -> {
            actions.addAll(event.getActions());
        });
        return actions;
    }

    // convenience method
    public List<Action> getAllActionsForEventStates(List<EventState> eventStates) {
        List<Action> actions = new ArrayList<>();
        eventStates.stream().forEach(eventState -> {
            actions.addAll(getAllActionsForEventState(eventState));
        });
        return actions;
    }

    // convenience method
    public List<Function> getAllFunctionsForActions(List<Action> actions) {
        List<Function> functions = new ArrayList<>();
        actions.stream().forEach(action -> {
            functions.add(action.getFunction());
        });

        return functions;
    }

    // convenience method
    public List<Function> getAllFunctionsForEventStates(List<EventState> eventStates) {
        List<Function> functions = new ArrayList<>();
        List<Action> actions = getAllActionsForEventStates(eventStates);
        return getAllFunctionsForActions(actions);
    }

    // convenience method
    public State getStartState() {
        return getWorkflow().getStates().stream().filter(state -> state.isStart())
            .findFirst().orElse(null);
    }

    // convenience method
    public State getStateByNAme(String stateName) {
        return getWorkflow().getStates().stream().filter(state -> state.getName().equals(stateName))
            .findFirst().orElse(null);
    }

    // convenience method
    public boolean haveEndState() {
        return getWorkflow().getStates().stream()
            .anyMatch(state -> state instanceof EndState);
    }
}

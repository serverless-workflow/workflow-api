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

import org.serverless.workflow.api.events.Event;
import org.serverless.workflow.api.events.TriggerEvent;
import org.serverless.workflow.api.interfaces.State;
import org.serverless.workflow.api.states.EventState;

public abstract class WorkflowAdvice {

    private Map<String, TriggerEvent> triggerEvenMap = new HashMap<>();
    private Map<String, State> stateMap = new HashMap<>();

    protected abstract Workflow getWorkflow();

    protected abstract void setExpressionEvaluator(ExpressionEvaluator expressionEvaluator);

    protected abstract ExpressionEvaluator getExpressionEvaluator();

    public Map<String, TriggerEvent> getUniqueTriggerEvents() {
        if (getWorkflow().getTriggerDefs() != null) {
            triggerEvenMap = getWorkflow().getTriggerDefs().stream()
                    .collect(Collectors.toMap(TriggerEvent::getId,
                                              tiggerEvent -> tiggerEvent));
        }

        return triggerEvenMap;
    }

    public boolean haveTriggers() {
        return getWorkflow().getTriggerDefs() != null && getWorkflow().getTriggerDefs().size() > 0;
    }

    public boolean haveStates() {
        return getWorkflow().getStates() != null && getWorkflow().getStates().size() > 0;
    }

    public Map<String, State> getUniqueStates() {
        if (getWorkflow().getTriggerDefs() != null) {
            stateMap = getWorkflow().getStates().stream()
                    .collect(Collectors.toMap(State::getId,
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
                if (triggeredEvents != null && triggeredEvents.size() > 0) {
                    triggerStates.add(eventState);
                }
            }
        }

        return triggerStates;
    }

    public List<TriggerEvent> getTriggerEventsForEventState(EventState eventState) {
        List<TriggerEvent> eventStateTriggers = new ArrayList<>();

        for(TriggerEvent triggerEvent : getWorkflow().getTriggerDefs()) {
            List<Event> triggeredEvents = eventState.getEvents().stream()
                    .filter(event -> getExpressionEvaluator()
                            .evaluate(event.getEventExpression(),
                                      triggerEvent.getName())).collect(Collectors.toList());

            if (triggeredEvents != null && triggeredEvents.size() > 0) {
                eventStateTriggers.add(triggerEvent);
            }
        }

        return eventStateTriggers;
    }
}

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

package org.serverless.workflow.api.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.everit.json.schema.loader.internal.DefaultSchemaClient;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.serverless.workflow.api.Workflow;
import org.serverless.workflow.api.schemaclient.ResourceSchemaClient;
import org.serverless.workflow.api.states.DelayState;
import org.serverless.workflow.api.states.EndState;
import org.serverless.workflow.api.states.EventState;
import org.serverless.workflow.api.states.OperationState;
import org.serverless.workflow.api.states.ParallelState;
import org.serverless.workflow.api.states.SwitchState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowValidator {

    private List<ValidationError> validationErrors = new ArrayList<>();
    private JSONObject workflowSchema;
    private Workflow workflow;

    private boolean schemaValidation = true;
    private String workflowJson;
    private boolean enabled = true;
    private boolean strictMode = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowValidator.class);

    public WorkflowValidator() {
        this.workflowSchema = new JSONObject(new JSONTokener(getClass().getResourceAsStream("/schema/workflow-01/workflow.json")));
    }

    public WorkflowValidator forWorkflowJson(String workflowJson) {
        this.workflowJson = workflowJson;
        return this;
    }

    public WorkflowValidator withSchemaValidation(boolean schemaValidation) {
        this.schemaValidation = schemaValidation;
        return this;
    }

    public WorkflowValidator withStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }

    public WorkflowValidator withEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public WorkflowValidator forWorkflow(Workflow workflow) {
        this.workflow = workflow;
        return this;
    }

    public JSONObject getWorkflowSchema() {
        return workflowSchema;
    }

    public List<ValidationError> validate() {
        validationErrors.clear();
        if (enabled) {
            try {
                if (schemaValidation && workflowJson != null) {
                    SchemaLoader schemaLoader = SchemaLoader.builder()
                            .schemaClient(new ResourceSchemaClient(new DefaultSchemaClient()))
                            .schemaJson(workflowSchema)
                            .resolutionScope("classpath:schema/workflow-01/") // setting the default resolution scope
                            .build();
                    Schema schema = schemaLoader.load().build();

                    try {
                        schema.validate(new JSONObject(workflowJson));
                    } catch (ValidationException e) {
                        // main error
                        addValidationError(e.getMessage(),
                                           ValidationError.SCHEMA_VALIDATION);
                        // suberrors
                        e.getCausingExceptions().stream()
                                .map(ValidationException::getMessage)
                                .forEach(m -> {
                                    addValidationError(m,
                                                       ValidationError.SCHEMA_VALIDATION);
                                });
                    }
                }

                if (workflow != null) {
                    // make sure we have at least one state
                    if (workflow.getStates() == null || workflow.getStates().size() < 1) {
                        addValidationError("No states found.",
                                           ValidationError.WORKFLOW_VALIDATION);
                    }

                    // make sure we have one start state and check for null next id and next-state
                    final Boolean[] foundStartState = {false};
                    final Integer[] startStatesCount = {0};
                    final Boolean[] foundEndState = {false};
                    final Integer[] endStatesCount = {0};
                    if (workflow.getStates() != null) {
                        workflow.getStates().stream().forEach(s -> {

                            if (s.getId() == null || s.getId().trim().length() < 1) {
                                addValidationError("Id should not be empty.",
                                                   ValidationError.WORKFLOW_VALIDATION);
                            }

                            if (s.getName() == null || s.getName().trim().length() < 1) {
                                addValidationError("Name should not be empty.",
                                                   ValidationError.WORKFLOW_VALIDATION);
                            }

                            if (s instanceof EventState) {
                                EventState eventState = (EventState) s;
                                if (eventState.isStart()) {
                                    foundStartState[0] = true;
                                    startStatesCount[0]++;
                                }
                            }
                            if (s instanceof OperationState) {
                                OperationState operationState = (OperationState) s;

                                if (operationState.getNextState() == null || operationState.getNextState().trim().length() < 1) {
                                    addValidationError("Next state should not be empty.",
                                                       ValidationError.WORKFLOW_VALIDATION);
                                }

                                if (operationState.isStart()) {
                                    foundStartState[0] = true;
                                    startStatesCount[0]++;
                                }
                            }
                            if (s instanceof SwitchState) {
                                SwitchState switchState = (SwitchState) s;

                                if (switchState.getDefault() == null || switchState.getDefault().trim().length() < 1) {
                                    addValidationError("Default should not be empty.",
                                                       ValidationError.WORKFLOW_VALIDATION);
                                }

                                if (switchState.isStart()) {
                                    foundStartState[0] = true;
                                    startStatesCount[0]++;
                                }
                            }
                            if (s instanceof ParallelState) {
                                ParallelState parallelState = (ParallelState) s;

                                if (parallelState.getNextState() == null || parallelState.getNextState().trim().length() < 1) {
                                    addValidationError("Next state should not be empty.",
                                                       ValidationError.WORKFLOW_VALIDATION);
                                }

                                if (parallelState.isStart()) {
                                    foundStartState[0] = true;
                                    startStatesCount[0]++;
                                }
                            }
                            if (s instanceof DelayState) {
                                DelayState delayState = (DelayState) s;

                                if (delayState.getNextState() == null || delayState.getNextState().trim().length() < 1) {
                                    addValidationError("Next state should not be empty.",
                                                       ValidationError.WORKFLOW_VALIDATION);
                                }

                                if (delayState.isStart()) {
                                    foundStartState[0] = true;
                                    startStatesCount[0]++;
                                }
                            }
                        });

                        workflow.getStates().stream().forEach(s -> {
                            if (s instanceof EndState) {
                                foundEndState[0] = true;
                                endStatesCount[0]++;
                            }
                        });
                    }

                    if (!foundStartState[0].booleanValue()) {
                        addValidationError("No start state found.",
                                           ValidationError.WORKFLOW_VALIDATION);
                    }

                    if (startStatesCount[0] > 1) {
                        addValidationError("Multiple start states found.",
                                           ValidationError.WORKFLOW_VALIDATION);
                    }

                    if (strictMode) {
                        if (!foundEndState[0].booleanValue()) {
                            addValidationError("No end state found.",
                                               ValidationError.WORKFLOW_VALIDATION);
                        }

                        if (endStatesCount[0] > 1) {
                            addValidationError("Multiple end states found.",
                                               ValidationError.WORKFLOW_VALIDATION);
                        }
                    }

                    // make sure if we have trigger events that they unique name and
                    // event id
                    if (workflow.getTriggerDefs() != null) {
                        Map<String, String> uniqueNames = new HashMap<>();
                        Map<String, String> uniqueEventIds = new HashMap();
                        workflow.getTriggerDefs().stream().forEach(triggerEvent -> {
                            if (triggerEvent.getName() == null || triggerEvent.getName().length() < 1) {
                                addValidationError("Trigger Event has no name",
                                                   ValidationError.WORKFLOW_VALIDATION);
                            }
                            if (triggerEvent.getEventID() == null || triggerEvent.getEventID().length() < 1) {
                                addValidationError("Trigger Event has no event id",
                                                   ValidationError.WORKFLOW_VALIDATION);
                            }

                            if (uniqueNames.containsKey(triggerEvent.getName())) {
                                addValidationError("Trigger Event does not have unique name.",
                                                   ValidationError.WORKFLOW_VALIDATION);
                            } else {
                                uniqueNames.put(triggerEvent.getName(),
                                                "");
                            }

                            if (uniqueEventIds.containsKey(triggerEvent.getEventID())) {
                                addValidationError("Trigger Event does not have unique eventid.",
                                                   ValidationError.WORKFLOW_VALIDATION);
                            } else {
                                uniqueEventIds.put(triggerEvent.getEventID(),
                                                   "");
                            }
                        });
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error loading schema: " + e.getMessage());
            }
        }

        return validationErrors;
    }

    private void addValidationError(String message,
                                    String type) {
        ValidationError mainError = new ValidationError();
        mainError.setMessage(message);
        mainError.setType(type);
        validationErrors.add(mainError);
    }
}

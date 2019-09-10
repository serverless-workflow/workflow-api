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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<ValidationError> validate() {
        validationErrors.clear();
        if (enabled) {
            try {
                if (schemaValidation && workflowJson != null) {
                    SchemaLoader schemaLoader = SchemaLoader.builder()
                        .schemaClient(new ResourceSchemaClient(new DefaultSchemaClient()))
                        .schemaJson(workflowSchema)
                        .resolutionScope("classpath:schema/workflow-01/") // setting the default resolution scope
                        .draftV7Support()
                        .addFormatValidator(new StateTypeValidator())
                        .addFormatValidator(new ActionModeValidator())
                        .addFormatValidator(new OperatorValidator())
                        .addFormatValidator(new StatusValidator())
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
                            .forEach(m -> addValidationError(m, ValidationError.SCHEMA_VALIDATION));
                    }
                }

                if (workflow != null) {
                    if (workflow.getName() == null || workflow.getName().trim().isEmpty()) {
                        addValidationError("Workflow name should not be empty", ValidationError.WORKFLOW_VALIDATION);
                    }
                    // make sure we have at least one state
                    if (workflow.getStates() == null || workflow.getStates().isEmpty()) {
                        addValidationError("No states found.",
                                           ValidationError.WORKFLOW_VALIDATION);
                    }

                    // make sure we have one start state and check for null next id and next-state
                    final Validation validation = new Validation();
                    if (workflow.getStates() != null) {
                        workflow.getStates().forEach(s -> {
                            if (s.getName() != null && s.getName().trim().isEmpty()) {
                                addValidationError("Name should not be empty.",
                                                   ValidationError.WORKFLOW_VALIDATION);
                            } else {
                                validation.addState(s.getName());
                            }
                            if (s.isStart()) {
                                validation.addStartState();
                            }

                            if (s instanceof OperationState) {
                                OperationState operationState = (OperationState) s;

                                if (operationState.getNextState() == null || operationState.getNextState().trim().isEmpty()) {
                                    addValidationError("Next state should not be empty.",
                                                       ValidationError.WORKFLOW_VALIDATION);
                                }
                            }
                            if (s instanceof SwitchState) {
                                SwitchState switchState = (SwitchState) s;

                                if (switchState.getDefault() == null || switchState.getDefault().trim().isEmpty()) {
                                    addValidationError("Default should not be empty.",
                                                       ValidationError.WORKFLOW_VALIDATION);
                                }
                            }
                            if (s instanceof ParallelState) {
                                ParallelState parallelState = (ParallelState) s;

                                if (parallelState.getNextState() == null || parallelState.getNextState().trim().isEmpty()) {
                                    addValidationError("Next state should not be empty.",
                                                       ValidationError.WORKFLOW_VALIDATION);
                                }
                            }
                            if (s instanceof DelayState) {
                                DelayState delayState = (DelayState) s;

                                if (delayState.getNextState() == null || delayState.getNextState().trim().isEmpty()) {
                                    addValidationError("Next state should not be empty.",
                                                       ValidationError.WORKFLOW_VALIDATION);
                                }
                            }
                            if (s instanceof EndState) {
                                validation.addEndState();
                            }
                        });
                    }

                    if (validation.startStates == 0) {
                        addValidationError("No start state found.",
                                           ValidationError.WORKFLOW_VALIDATION);
                    }

                    if (validation.startStates > 1) {
                        addValidationError("Multiple start states found.",
                                           ValidationError.WORKFLOW_VALIDATION);
                    }

                    if (strictMode) {
                        if (validation.endStates == 0) {
                            addValidationError("No end state found.",
                                               ValidationError.WORKFLOW_VALIDATION);
                        }

                        if (validation.endStates > 1) {
                            addValidationError("Multiple end states found.",
                                               ValidationError.WORKFLOW_VALIDATION);
                        }
                    }

                    // make sure if we have trigger events that they unique name
                    if (workflow.getTriggerDefs() != null) {
                        workflow.getTriggerDefs().forEach(triggerEvent -> {
                            if (triggerEvent.getName() == null || triggerEvent.getName().isEmpty()) {
                                addValidationError("Trigger Event has no name",
                                                   ValidationError.WORKFLOW_VALIDATION);
                            } else {
                                validation.addEvent(triggerEvent.getName());
                            }
                            if (triggerEvent.getEventID() == null || triggerEvent.getEventID().isEmpty()) {
                                addValidationError("Trigger Event has no event id",
                                                   ValidationError.WORKFLOW_VALIDATION);
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

    private class Validation {

        final Set<String> events = new HashSet<>();
        final Set<String> states = new HashSet<>();
        Integer startStates = 0;
        Integer endStates = 0;

        void addEvent(String name) {
            if (events.contains(name)) {
                addValidationError("Trigger Event does not have unique name: " + name,
                                   ValidationError.WORKFLOW_VALIDATION);
            } else {
                events.add(name);
            }
        }

        void addState(String name) {
            if (states.contains(name)) {
                addValidationError("State does not have a unique name: " + name,
                                   ValidationError.WORKFLOW_VALIDATION);
            } else {
                states.add(name);
            }
        }

        void addStartState() {
            startStates++;
        }

        void addEndState() {
            endStates++;
        }
    }
}

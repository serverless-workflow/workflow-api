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

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.serverless.workflow.api.mapper.WorkflowObjectMapper;
import org.serverless.workflow.api.validation.ValidationError;
import org.serverless.workflow.api.validation.WorkflowValidator;

public class WorkflowController {

    private WorkflowValidator validator;
    private List<ValidationError> validationErrors;
    private String json;
    private WorkflowObjectMapper objectMapper = new WorkflowObjectMapper();
    private Workflow workflow;

    public WorkflowController() {

    }

    public WorkflowController(Workflow workflow) {
        this.json = "";
        this.workflow = workflow;
        this.validator = new WorkflowValidator().forWorkflow(workflow);
        this.validationErrors = this.validator.validate();
    }

    public WorkflowController(String workflowJSON) {
        this.json = workflowJSON;
        this.workflow = toWorkflow(json);
        this.validator = new WorkflowValidator().forWorkflowJson(json);
        this.validationErrors = this.validator.validate();
    }

    public boolean isValid() {
        return validationErrors.size() < 1;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public String toJsonString() {
        try {
            return objectMapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public JsonNode toJson() {
        try {
            return objectMapper.readTree(json);
        } catch (Exception e) {
            return null;
        }
    }

    private Workflow toWorkflow(String json) {
        try {
            return objectMapper.readValue(json,
                                          Workflow.class);
        } catch (Exception e) {
            return null;
        }
    }
}

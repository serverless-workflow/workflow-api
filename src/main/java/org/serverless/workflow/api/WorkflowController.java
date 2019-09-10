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

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.serverless.workflow.api.mapper.WorkflowObjectMapper;
import org.serverless.workflow.api.validation.ValidationError;
import org.serverless.workflow.api.validation.WorkflowValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkflowController extends WorkflowAdvice {

    private WorkflowValidator validator = new WorkflowValidator();
    private String workflowJSON = "";
    private WorkflowObjectMapper objectMapper = new WorkflowObjectMapper();
    private Workflow workflow;
    private ExpressionEvaluator expressionEvaluator = new DefaultExpressionEvaluator();

    private static Logger logger = LoggerFactory.getLogger(WorkflowController.class);

    public WorkflowController forWorkflow(Workflow workflow) {
        this.workflow = workflow;
        this.validator = this.validator.forWorkflow(workflow);
        return this;
    }

    public WorkflowController forJson(String workflowJSON) {
        this.workflowJSON = workflowJSON;
        this.workflow = toWorkflow(workflowJSON);
        this.validator = this.validator.forWorkflow(this.workflow);
        return this;
    }

    public WorkflowController withExpressionEvaluator(ExpressionEvaluator expressionEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
        return this;
    }

    public WorkflowController withValidationEnabled(boolean validationEnabled) {
        this.validator = this.validator.withEnabled(validationEnabled);
        return this;
    }

    public WorkflowController withSchemaValidation(boolean schemaValidation) {
        this.validator = this.validator.withSchemaValidation(schemaValidation);
        return this;
    }

    public WorkflowController withStrictValidation(boolean strictValidation) {
        this.validator = this.validator.withStrictMode(strictValidation);
        return this;
    }

    public boolean isValid() {
        return this.validator.validate().isEmpty();
    }

    public List<ValidationError> getValidationErrors() {
        return this.validator.validate();
    }

    @Override
    public ExpressionEvaluator getExpressionEvaluator() {
        return expressionEvaluator;
    }

    public String displayValidationErrors() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.writeValue(out,
                              this.validator.validate());
        } catch (Exception e) {
            logger.error("Unable to display validation errors: " + e.getMessage());
        }

        final byte[] data = out.toByteArray();

        return new String(data);
    }

    @Override
    public Workflow getWorkflow() {
        return workflow;
    }

    public String toJsonString() {
        try {
            return objectMapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            logger.error("Error mapping to json: " + e.getMessage());
            return null;
        }
    }

    public JsonNode toJson() {
        try {
            return objectMapper.readTree(workflowJSON);
        } catch (Exception e) {
            return null;
        }
    }

    private Workflow toWorkflow(String json) {
        try {
            return objectMapper.readValue(json,
                                          Workflow.class);
        } catch (Exception e) {
            logger.error("Error converting to workflow: " + e.getMessage());
            return null;
        }
    }
}

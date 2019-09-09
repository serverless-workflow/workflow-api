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
import org.serverless.workflow.api.Workflow;
import org.serverless.workflow.api.validation.ValidationError;
import org.serverless.workflow.api.validation.WorkflowValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest extends BaseWorkflowTest {

    @Test
    public void testEmptyJson() {

        // there should be one schema validation error
        // no workflow errors (workflow not give)
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson("{}");
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();
        assertEquals(1,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "#: required key [states] not found",
                                  ValidationError.SCHEMA_VALIDATION));

        // if workflow given there should be schema errors and workflow errors
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        validator = validator.forWorkflow(workflow);
        validationErrorList = validator.validate();
        assertEquals(3,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "#: required key [states] not found",
                                  ValidationError.SCHEMA_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "No states found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "No start state found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        //assertTrue(constainsError(validationErrorList, "No end state found.", ValidationError.WORKFLOW_VALIDATION));

    }

    @Test
    public void testNoData() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        assertNotNull(validator);

        assertEquals(0,
                     validator.validate().size());

        // if workflow given there should be chema error and workflow error2
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        validator = validator.forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();
        assertEquals(2,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "No states found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "No start state found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        //assertTrue(constainsError(validationErrorList, "No end state found.", ValidationError.WORKFLOW_VALIDATION));

    }

    @Test
    public void testInvalidTriggerEvent() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidtrigger.json")));
        assertNotNull(validator);

        assertEquals(1,
                     validator.validate().size());
        assertEquals("#/trigger-defs/0: required key [name] not found",
                     validator.validate().get(0).getMessage());

        // if workflow given there should be schema error too
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/invalidtrigger.json")));
        validator = validator.forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();
        assertEquals(1,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "#/trigger-defs/0: required key [name] not found",
                                  ValidationError.SCHEMA_VALIDATION));
    }

    @Test
    public void testInvalidTriggerEventNotUniqueProperties() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidtriggerproperties.json")));
        assertNotNull(validator);

        // no schema errors
        assertEquals(0,
                     validator.validate().size());

        // there are workflow validation errors
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/invalidtriggerproperties.json")));
        validator = validator.forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();
        assertEquals(4,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "No states found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "No start state found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "Trigger Event does not have unique name.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "Trigger Event does not have unique eventid.",
                                  ValidationError.WORKFLOW_VALIDATION));
    }

    @Test
    public void testMultipleStartStates() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/multiplestartstates.json")));
        assertNotNull(validator);

        // no schema errors
        assertEquals(0,
                     validator.validate().size());

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/multiplestartstates.json")));
        validator = validator.forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "Multiple start states found.",
                                  ValidationError.WORKFLOW_VALIDATION));
    }

    @Test
    public void testMultipleEndStatesInStrictMode() {
        WorkflowValidator validator = new WorkflowValidator()
                .forWorkflowJson(getFileContents(getResourcePath("validation/multipleendstates.json")))
                .withStrictMode(true);
        assertNotNull(validator);

        // no schema errors
        assertEquals(0,
                     validator.validate().size());

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/multipleendstates.json")));
        validator = validator.forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(2,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "No start state found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "Multiple end states found.",
                                  ValidationError.WORKFLOW_VALIDATION));
    }

    @Test
    public void testValidationDisabled() {
        WorkflowValidator validator = new WorkflowValidator()
                .forWorkflowJson("{}")
                .withEnabled(false);
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(0,
                     validationErrorList.size());
    }

    @Test
    public void testSchemaValidationDisabled() {
        WorkflowValidator validator = new WorkflowValidator()
                .forWorkflowJson("{}")
                .withSchemaValidation(false);
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();
        // with schema validation disabled there should be no schema errors
        assertEquals(0,
                     validationErrorList.size());

        // if workflow given there should be workflow errors
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        validator = validator.forWorkflow(workflow);
        validationErrorList = validator.validate();
        assertEquals(2,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "No states found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "No start state found.",
                                  ValidationError.WORKFLOW_VALIDATION));
    }

    @Test
    public void testStrictValidationEnabled() {
        WorkflowValidator validator = new WorkflowValidator()
                .forWorkflowJson("{}")
                .withSchemaValidation(false)
                .withStrictMode(true);
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();
        // with schema validation disabled there should be no schema errors
        assertEquals(0,
                     validationErrorList.size());

        // if workflow given there should be workflow errors + additional strict mode error
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        validator = validator.forWorkflow(workflow);
        validationErrorList = validator.validate();
        assertEquals(3,
                     validationErrorList.size());
        assertTrue(constainsError(validationErrorList,
                                  "No states found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "No start state found.",
                                  ValidationError.WORKFLOW_VALIDATION));
        assertTrue(constainsError(validationErrorList,
                                  "No end state found.",
                                  ValidationError.WORKFLOW_VALIDATION));
    }

    @Test
    public void testEmptyId() {

        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/emptyid.json")));
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        assertTrue(constainsError(validationErrorList,
                                  "#/id: expected minLength: 1, actual: 0",
                                  ValidationError.SCHEMA_VALIDATION));
    }

    @Test
    public void testEmptyNextState() {
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/emptynextstate.json")));
        WorkflowValidator validator = new WorkflowValidator().forWorkflow(workflow);
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        assertTrue(constainsError(validationErrorList,
                                  "Next state should not be empty.",
                                  ValidationError.WORKFLOW_VALIDATION));
    }

    @Test
    public void testEmptyName() {
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/emptyname.json")));
        WorkflowValidator validator = new WorkflowValidator().forWorkflow(workflow);
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        assertTrue(constainsError(validationErrorList,
                                  "Name should not be empty.",
                                  ValidationError.WORKFLOW_VALIDATION));
    }

    @Test
    public void testInvalidStateDefinition() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidstate.json")));
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(3,
                     validationErrorList.size());
    }
}

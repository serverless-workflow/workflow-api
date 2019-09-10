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

public class ValidatorTest extends BaseWorkflowTest {

    @Test
    public void testEmptyJson() {

        // there should be two schema validation errors
        // no workflow errors (workflow not give)
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson("{}");
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();
        assertEquals(3,
                     validationErrorList.size());
        expectError(validationErrorList,
                    "#: required key [states] not found",
                    ValidationError.SCHEMA_VALIDATION);
        expectError(validationErrorList,
                    "#: required key [name] not found",
                    ValidationError.SCHEMA_VALIDATION);
        expectError(validationErrorList,
                    "#: 2 schema violations found",
                    ValidationError.SCHEMA_VALIDATION);

        // if workflow given there should be schema errors and workflow errors
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));

        validator = new WorkflowValidator().forWorkflow(workflow);
        validationErrorList = validator.validate();
        assertEquals(2,
                     validationErrorList.size());
        expectError(validationErrorList,
                    "No states found.",
                    ValidationError.WORKFLOW_VALIDATION);
        expectError(validationErrorList,
                    "No start state found.",
                    ValidationError.WORKFLOW_VALIDATION);
    }

    @Test
    public void testNoData() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        assertNotNull(validator);

        assertEquals(0,
                     validator.validate().size());

        // if workflow given there should be chema error and workflow error2
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        validator = new WorkflowValidator().forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();
        assertEquals(2,
                     validationErrorList.size());
        expectError(validationErrorList,
                    "No states found.",
                    ValidationError.WORKFLOW_VALIDATION);
        expectError(validationErrorList,
                    "No start state found.",
                    ValidationError.WORKFLOW_VALIDATION);
    }

    @Test
    public void testInvalidTriggerEvent() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidtrigger.json")));
        assertNotNull(validator);
        List<ValidationError> validationErrorList = validator.validate();
        assertEquals(1, validationErrorList.size());
        expectError(validationErrorList,
                    "#/trigger-defs/0: required key [name] not found",
                    ValidationError.SCHEMA_VALIDATION);
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
        validator = new WorkflowValidator().forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();
        assertEquals(3,
                     validationErrorList.size());
        expectError(validationErrorList,
                    "No states found.",
                    ValidationError.WORKFLOW_VALIDATION);
        expectError(validationErrorList,
                    "No start state found.",
                    ValidationError.WORKFLOW_VALIDATION);
        expectError(validationErrorList,
                    "Trigger Event does not have unique name: testtriggerevent",
                    ValidationError.WORKFLOW_VALIDATION);
    }

    @Test
    public void testMultipleStartStates() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/multiplestartstates.json")));
        assertNotNull(validator);

        // no schema errors
        assertEquals(0,
                     validator.validate().size());

        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/multiplestartstates.json")));
        validator = new WorkflowValidator().forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());
        expectError(validationErrorList,
                    "Multiple start states found.",
                    ValidationError.WORKFLOW_VALIDATION);
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
        validator = new WorkflowValidator().withStrictMode(true).forWorkflow(workflow);
        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(2,
                     validationErrorList.size());
        expectError(validationErrorList,
                    "No start state found.",
                    ValidationError.WORKFLOW_VALIDATION);
        expectError(validationErrorList,
                    "Multiple end states found.",
                    ValidationError.WORKFLOW_VALIDATION);
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
        validator = new WorkflowValidator().forWorkflow(workflow);
        validationErrorList = validator.validate();
        assertEquals(2,
                     validationErrorList.size());
        expectError(validationErrorList,
                    "No states found.",
                    ValidationError.WORKFLOW_VALIDATION);
        expectError(validationErrorList,
                    "No start state found.",
                    ValidationError.WORKFLOW_VALIDATION);
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
        validator = new WorkflowValidator().withStrictMode(true).forWorkflow(workflow);
        validationErrorList = validator.validate();
        assertEquals(3,
                     validationErrorList.size());
        expectError(validationErrorList,
                    "No states found.",
                    ValidationError.WORKFLOW_VALIDATION);
        expectError(validationErrorList,
                    "No start state found.",
                    ValidationError.WORKFLOW_VALIDATION);
        expectError(validationErrorList,
                    "No end state found.",
                    ValidationError.WORKFLOW_VALIDATION);
    }

    @Test
    public void testEmptyNextState() {
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/emptynextstate.json")));
        WorkflowValidator validator = new WorkflowValidator().forWorkflow(workflow);
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        expectError(validationErrorList,
                    "Next state should not be empty.",
                    ValidationError.WORKFLOW_VALIDATION);
    }

    @Test
    public void testEmptyName() {
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/emptyname.json")));
        WorkflowValidator validator = new WorkflowValidator().forWorkflow(workflow);
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        expectError(validationErrorList,
                    "Workflow name should not be empty",
                    ValidationError.WORKFLOW_VALIDATION);
    }

    @Test
    public void testInvalidStateDefinition() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidstate.json")));
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(3,
                     validationErrorList.size());
    }

    @Test
    public void testInvalidStateType() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidstatetype.json")));
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        expectError(validationErrorList,
                    "#: Invalid state type: CUSTOMSTATETYPE",
                    ValidationError.SCHEMA_VALIDATION);
    }

    @Test
    public void testInvalidActionMode() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidactionmode.json")));
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        expectError(validationErrorList,
                    "#: Invalid action mode: CUSTOMACTIONMODE",
                    ValidationError.SCHEMA_VALIDATION);
    }

    @Test
    public void testInvalidOperator() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidoperator.json")));
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        expectError(validationErrorList,
                    "#: Invalid operator: CUSTOMOPERATOR",
                    ValidationError.SCHEMA_VALIDATION);
    }

    @Test
    public void testInvalidStatus() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidstatus.json")));
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        expectError(validationErrorList,
                    "#: Invalid status: CUSTOMSTATUS",
                    ValidationError.SCHEMA_VALIDATION);
    }

    @Test
    public void testUniqueStateId() {
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("validation/duplicatedstateid.json")));
        WorkflowValidator validator = new WorkflowValidator().forWorkflow(workflow);
        assertNotNull(validator);

        List<ValidationError> validationErrorList = validator.validate();

        assertEquals(1,
                     validationErrorList.size());

        expectError(validationErrorList, "State does not have a unique name: duplicated", ValidationError.WORKFLOW_VALIDATION);
    }
}

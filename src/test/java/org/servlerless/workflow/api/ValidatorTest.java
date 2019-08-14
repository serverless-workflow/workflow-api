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

import org.junit.jupiter.api.Test;
import org.serverless.workflow.api.Workflow;
import org.serverless.workflow.api.validation.WorkflowValidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ValidatorTest extends BaseWorkflowTest {

    @Test
    public void emptyJsonTest() {

        // there should be one schema validation error
        // no workflow errors (workflow not give)
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson("{}");
        assertNotNull(validator);

        assertEquals(1, validator.validate().size());
        assertEquals("#: required key [states] not found", validator.validate().get(0).getMessage());

        // if workflow given there should be 1 schema error and 2 workflow error
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        validator = validator.forWorkflow(workflow);
        assertEquals(3, validator.validate().size());
        assertEquals("#: required key [states] not found", validator.validate().get(0).getMessage());
        assertEquals("No states present.", validator.validate().get(1).getMessage());
        assertEquals("No start state found.", validator.validate().get(2).getMessage());

    }

    @Test
    public void noDataTest() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        assertNotNull(validator);

        assertEquals(0, validator.validate().size());

        // if workflow given there should be 0 schema error and 2 workflow error
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        validator = validator.forWorkflow(workflow);
        assertEquals(2, validator.validate().size());
        assertEquals("No states present.", validator.validate().get(0).getMessage());
        assertEquals("No start state found.", validator.validate().get(1).getMessage());
    }

    @Test
    public void invalidTriggerEvent() {
        WorkflowValidator validator = new WorkflowValidator().forWorkflowJson(getFileContents(getResourcePath("validation/invalidtrigger.json")));
        assertNotNull(validator);

        assertEquals(1, validator.validate().size());
        assertEquals("#/trigger-defs/0: required key [name] not found", validator.validate().get(0).getMessage());

        // if workflow given there should be 1 schema error and 2 workflow error
        Workflow workflow = toWorkflow(getFileContents(getResourcePath("basic/emptyworkflow.json")));
        validator = validator.forWorkflow(workflow);
        assertEquals(3, validator.validate().size());
        assertEquals("#/trigger-defs/0: required key [name] not found", validator.validate().get(0).getMessage());
        assertEquals("No states present.", validator.validate().get(1).getMessage());
        assertEquals("No start state found.", validator.validate().get(2).getMessage());
    }
}

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

package org.serverless.workflow.api.testproviders;

import java.util.List;

import org.serverless.workflow.api.WorkflowManager;
import org.serverless.workflow.api.WorkflowValidator;
import org.serverless.workflow.api.validation.ValidationError;

public class TestWorkflowValidator implements WorkflowValidator {

    @Override
    public void setWorkflowManager(WorkflowManager workflowManager) {
        
    }

    @Override
    public List<ValidationError> validate() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {

    }

    @Override
    public void setSchemaValidationEnabled(boolean schemaValidationEnabled) {

    }

    @Override
    public void setStrictValidationEnabled(boolean strictValidationEnabled) {

    }

    @Override
    public void reset() {

    }
}

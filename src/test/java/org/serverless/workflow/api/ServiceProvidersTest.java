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

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.serverless.workflow.api.testproviders.TestExpressionEvaluator;
import org.serverless.workflow.api.testproviders.TestWorkflowManager;
import org.serverless.workflow.api.testproviders.TestWorkflowValidator;
import org.serverless.workflow.spi.ExpressionEvaluatorProvider;
import org.serverless.workflow.spi.WorkflowManagerProvider;
import org.serverless.workflow.spi.WorkflowValidatorProvider;

public class ServiceProvidersTest {

    @Test
    public void testExpressionEvaluatorProvider() {
        Map<String, ExpressionEvaluator> evaluators = ExpressionEvaluatorProvider.getInstance().get();
        Assertions.assertNotNull(evaluators);
        Assertions.assertEquals(1,
                                evaluators.size());
        Assertions.assertNotNull(evaluators.get("test"));
        Assertions.assertTrue(evaluators.get("test") instanceof TestExpressionEvaluator);
    }

    @Test
    public void testWorkflowManagerProvider() {
        WorkflowManager manager = WorkflowManagerProvider.getInstance().get();
        Assertions.assertNotNull(manager);
        Assertions.assertTrue(manager instanceof TestWorkflowManager);
    }

    @Test
    public void testWorkflowValidatorProvider() {
        WorkflowValidator validator = WorkflowValidatorProvider.getInstance().get();
        Assertions.assertNotNull(validator);
        Assertions.assertTrue(validator instanceof TestWorkflowValidator);
    }
}

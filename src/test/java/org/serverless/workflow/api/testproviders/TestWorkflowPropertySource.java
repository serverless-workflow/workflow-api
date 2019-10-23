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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.serverless.workflow.api.WorkflowPropertySource;

public class TestWorkflowPropertySource implements WorkflowPropertySource {

    private Properties source = new Properties();

    @Override
    public Properties getPropertySource() {
        Map<String, String> propertySourcetMap = new HashMap<>();
        propertySourcetMap.put("wfname",
                           "test-wf");
        propertySourcetMap.put("endstate.name",
                           "test-state");
        propertySourcetMap.put("endstate.status",
                           "SUCCESS");
        propertySourcetMap.put("endstate.type",
                           "END");

        source.putAll(propertySourcetMap);

        return source;
    }

    @Override
    public void setPropertySource(Properties source) {
        this.source = source;
    }
}

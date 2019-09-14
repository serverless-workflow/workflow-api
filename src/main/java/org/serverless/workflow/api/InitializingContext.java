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

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import pl.jalokim.propertiestojson.util.PropertiesToJsonConverter;

public class InitializingContext {

    private String context;
    private String workflowName;

    public String getContext() {
        return context;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public void setContext(Map<String, String> context) {
        this.context = new PropertiesToJsonConverter().convertToJson(context);
    }

    public void setContext(Map<String, String> context,
                           String workflowName) {
        this.context = new PropertiesToJsonConverter().convertToJson(context,
                                                                     workflowName);
        this.workflowName = workflowName;
    }

    public void setContext(Properties properties) {
        this.context = new PropertiesToJsonConverter().convertToJson(properties);
    }

    public void setContext(Properties properties,
                           String workflowName) {
        this.context = new PropertiesToJsonConverter().convertToJson(properties,
                                                                     workflowName);
        this.workflowName = workflowName;
    }

    public void setContext(InputStream inputStream) {
        this.context = new PropertiesToJsonConverter().convertToJson(inputStream);
    }

    public void setContext(InputStream inputStream,
                           String workflowName) {
        this.context = new PropertiesToJsonConverter().convertToJson(inputStream,
                                                                     workflowName);
        this.workflowName = workflowName;
    }
}

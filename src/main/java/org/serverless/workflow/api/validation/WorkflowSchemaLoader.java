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

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.everit.json.schema.loader.internal.DefaultSchemaClient;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.serverless.workflow.api.schemaclient.ResourceSchemaClient;

public class WorkflowSchemaLoader {

    private static final JSONObject workflowSchema = new JSONObject(new JSONTokener(WorkflowSchemaLoader.class.getResourceAsStream("/schema/workflow-01/workflow.json")));

    public static Schema getWorkflowSchema() {
        SchemaLoader schemaLoader = SchemaLoader.builder()
                .schemaClient(new ResourceSchemaClient(new DefaultSchemaClient()))
                .schemaJson(workflowSchema)
                .resolutionScope("classpath:schema/workflow-01/")
                .draftV7Support()
                .build();
        return schemaLoader.load().build();
    }
}

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.serverless.workflow.api.deserializers.ChoiceDeserializer;
import org.serverless.workflow.api.deserializers.StateDeserializer;
import org.serverless.workflow.api.interfaces.Choice;
import org.serverless.workflow.api.interfaces.State;
import org.serverless.workflow.api.serializers.DelayStateSerializer;
import org.serverless.workflow.api.serializers.EndStateSerializer;
import org.serverless.workflow.api.serializers.EventStateSerializer;
import org.serverless.workflow.api.serializers.OperationStateSerializer;
import org.serverless.workflow.api.serializers.ParallelStateSerializer;
import org.serverless.workflow.api.serializers.SwitchStateSerializer;
import org.serverless.workflow.api.serializers.WorkflowSerializer;

public class WorkflowObjectMapper extends ObjectMapper {

    public WorkflowObjectMapper() {
        super();
        configure(SerializationFeature.INDENT_OUTPUT,
                  true);

        // serializers
        SimpleModule module = new SimpleModule("workflow-module");
        module.addSerializer(new WorkflowSerializer());
        module.addSerializer(new EndStateSerializer());
        module.addSerializer(new EventStateSerializer());
        module.addSerializer(new DelayStateSerializer());
        module.addSerializer(new OperationStateSerializer());
        module.addSerializer(new ParallelStateSerializer());
        module.addSerializer(new SwitchStateSerializer());

        // deserializers
        module.addDeserializer(State.class,
                               new StateDeserializer());
        module.addDeserializer(Choice.class,
                               new ChoiceDeserializer());

        registerModule(module);
    }
}

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

package org.serverless.workflow.api.mapper;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.serverless.workflow.api.InitContext;
import org.serverless.workflow.api.choices.DefaultChoice;
import org.serverless.workflow.api.deserializers.ChoiceDeserializer;
import org.serverless.workflow.api.deserializers.DefaultChoiceOperatorDeserializer;
import org.serverless.workflow.api.deserializers.DefaultStateTypeDeserializer;
import org.serverless.workflow.api.deserializers.EndStateStatusDeserializer;
import org.serverless.workflow.api.deserializers.EventActionModeDeserializer;
import org.serverless.workflow.api.deserializers.ExtensionDeserializer;
import org.serverless.workflow.api.deserializers.OperationStateActionModeDeserializer;
import org.serverless.workflow.api.deserializers.StateDeserializer;
import org.serverless.workflow.api.deserializers.StringValueDeserializer;
import org.serverless.workflow.api.events.Event;
import org.serverless.workflow.api.interfaces.Choice;
import org.serverless.workflow.api.interfaces.Extension;
import org.serverless.workflow.api.interfaces.State;
import org.serverless.workflow.api.serializers.DelayStateSerializer;
import org.serverless.workflow.api.serializers.EndStateSerializer;
import org.serverless.workflow.api.serializers.EventStateSerializer;
import org.serverless.workflow.api.serializers.ExtensionSerializer;
import org.serverless.workflow.api.serializers.OperationStateSerializer;
import org.serverless.workflow.api.serializers.ParallelStateSerializer;
import org.serverless.workflow.api.serializers.SwitchStateSerializer;
import org.serverless.workflow.api.serializers.TriggerEventSerializer;
import org.serverless.workflow.api.serializers.WorkflowSerializer;
import org.serverless.workflow.api.states.DefaultState;
import org.serverless.workflow.api.states.DelayState;
import org.serverless.workflow.api.states.EndState;
import org.serverless.workflow.api.states.EventState;
import org.serverless.workflow.api.states.OperationState;
import org.serverless.workflow.api.states.ParallelState;
import org.serverless.workflow.api.states.SwitchState;

public class WorkflowModule extends SimpleModule {

    private InitContext initContext;
    private ExtensionSerializer extensionSerializer;
    private ExtensionDeserializer extensionDeserializer;

    public WorkflowModule() {
        this(null);
    }

    public WorkflowModule(InitContext initContext) {
        super("workflow-module");
        this.initContext = initContext;
        extensionSerializer = new ExtensionSerializer();
        extensionDeserializer = new ExtensionDeserializer(initContext);
        addDefaultSerializers();
        addDefaultDeserializers();
    }

    private void addDefaultSerializers() {
        addSerializer(new WorkflowSerializer());
        addSerializer(new EndStateSerializer());
        addSerializer(new EventStateSerializer());
        addSerializer(new DelayStateSerializer());
        addSerializer(new OperationStateSerializer());
        addSerializer(new ParallelStateSerializer());
        addSerializer(new SwitchStateSerializer());
        addSerializer(new TriggerEventSerializer());
        addSerializer(extensionSerializer);
    }

    private void addDefaultDeserializers() {
        addDeserializer(State.class,
                        new StateDeserializer(initContext));
        addDeserializer(Choice.class,
                        new ChoiceDeserializer());
        addDeserializer(String.class,
                        new StringValueDeserializer(initContext));
        addDeserializer(Event.ActionMode.class,
                        new EventActionModeDeserializer(initContext));
        addDeserializer(OperationState.ActionMode.class,
                        new OperationStateActionModeDeserializer(initContext));
        addDeserializer(EndState.Status.class,
                        new EndStateStatusDeserializer(initContext));
        addDeserializer(DefaultState.Type.class,
                        new DefaultStateTypeDeserializer(initContext));
        addDeserializer(DelayState.Type.class,
                        new DefaultStateTypeDeserializer(initContext));
        addDeserializer(EndState.Type.class,
                        new DefaultStateTypeDeserializer(initContext));
        addDeserializer(EventState.Type.class,
                        new DefaultStateTypeDeserializer(initContext));
        addDeserializer(OperationState.Type.class,
                        new DefaultStateTypeDeserializer(initContext));
        addDeserializer(ParallelState.Type.class,
                        new DefaultStateTypeDeserializer(initContext));
        addDeserializer(SwitchState.Type.class,
                        new DefaultStateTypeDeserializer(initContext));
        addDeserializer(DefaultChoice.Operator.class,
                        new DefaultChoiceOperatorDeserializer(initContext));
        addDeserializer(Extension.class, extensionDeserializer);
    }

    public ExtensionSerializer getExtensionSerializer() {
        return extensionSerializer;
    }

    public ExtensionDeserializer getExtensionDeserializer() {
        return extensionDeserializer;
    }
}

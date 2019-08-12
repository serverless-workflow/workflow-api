package org.serverless.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.serverless.workflow.deserializers.ChoiceDeserializer;
import org.serverless.workflow.deserializers.StateDeserializer;
import org.serverless.workflow.interfaces.Choice;
import org.serverless.workflow.interfaces.State;
import org.serverless.workflow.serializers.DelayStateSerializer;
import org.serverless.workflow.serializers.EndStateSerializer;
import org.serverless.workflow.serializers.EventStateSerializer;
import org.serverless.workflow.serializers.OperationStateSerializer;
import org.serverless.workflow.serializers.ParallelStateSerializer;
import org.serverless.workflow.serializers.SwitchStateSerializer;
import org.serverless.workflow.serializers.WorkflowSerializer;

public class WorkflowObjectMapper extends ObjectMapper {
    public WorkflowObjectMapper()
    {
        super();
        configure(SerializationFeature.INDENT_OUTPUT, true);

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
        module.addDeserializer(State.class, new StateDeserializer());
        module.addDeserializer(Choice.class, new ChoiceDeserializer());

        registerModule(module);
    }
}

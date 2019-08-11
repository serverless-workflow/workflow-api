package org.serverless.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.serverless.workflow.serializers.DelayStateCustomSerializer;
import org.serverless.workflow.serializers.EndStateCustomSerializer;
import org.serverless.workflow.serializers.EventStateCustomSerializer;
import org.serverless.workflow.serializers.OperationStateCustomSerializer;
import org.serverless.workflow.serializers.ParallelStateCustomSerializer;
import org.serverless.workflow.serializers.SwitchStateCustomSerializer;
import org.serverless.workflow.serializers.WorkflowCustomSerializer;

public class WorkflowObjectMapper extends ObjectMapper {
    public WorkflowObjectMapper()
    {
        super();
        configure(SerializationFeature.INDENT_OUTPUT, true);

        SimpleModule module = new SimpleModule("workflow-module");
        module.addSerializer(new WorkflowCustomSerializer());
        module.addSerializer(new EndStateCustomSerializer());
        module.addSerializer(new EventStateCustomSerializer());
        module.addSerializer(new DelayStateCustomSerializer());
        module.addSerializer(new OperationStateCustomSerializer());
        module.addSerializer(new ParallelStateCustomSerializer());
        module.addSerializer(new SwitchStateCustomSerializer());

        registerModule(module);
    }
}

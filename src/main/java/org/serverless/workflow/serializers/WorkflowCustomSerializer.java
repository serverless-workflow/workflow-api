package org.serverless.workflow.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.serverless.workflow.Workflow;
import org.serverless.workflow.events.TriggerEvent;
import org.serverless.workflow.interfaces.State;

public class WorkflowCustomSerializer extends StdSerializer<Workflow> {
    public WorkflowCustomSerializer() {
        this(Workflow.class);
    }

    protected WorkflowCustomSerializer(Class<Workflow> t) {
        super(t);
    }

    @Override
    public void serialize(Workflow workflow, JsonGenerator gen, SerializerProvider provider) throws IOException {

        gen.writeStartObject();
        if(workflow.getTriggerDefs() != null && workflow.getTriggerDefs().size() > 0) {
            gen.writeArrayFieldStart("trigger-defs");
            for (TriggerEvent triggerEvent: workflow.getTriggerDefs()) {
                gen.writeObject(triggerEvent);
            }
            gen.writeEndArray();
        }

        if(workflow.getStates() != null && workflow.getStates().size() > 0) {
            gen.writeArrayFieldStart("states");
            for (State state: workflow.getStates()) {
                gen.writeObject(state);
            }
            gen.writeEndArray();
        } else {
            gen.writeArrayFieldStart("states");
            gen.writeEndArray();
        }

        gen.writeEndObject();

    }
}

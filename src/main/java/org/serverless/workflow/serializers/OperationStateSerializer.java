package org.serverless.workflow.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.serverless.workflow.states.DefaultState;
import org.serverless.workflow.states.OperationState;

public class OperationStateSerializer extends StdSerializer<OperationState> {
    public OperationStateSerializer() {
        this(OperationState.class);
    }

    protected OperationStateSerializer(Class<OperationState> t) {
        super(t);
    }

    @Override
    public void serialize(OperationState operationState, JsonGenerator gen, SerializerProvider provider) throws IOException {

        // set defaults for delay state
        operationState.setType(DefaultState.Type.OPERATION);
        if(operationState.getName() == null || operationState.getName().length() < 1) {
            operationState.setName("operationstate");
        }

        // serialize after setting default bean values...
        BeanSerializerFactory.instance.createSerializer(provider, SimpleType.construct(OperationState.class)).serialize(operationState, gen, provider);
    }
}

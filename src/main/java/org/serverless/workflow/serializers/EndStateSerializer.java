package org.serverless.workflow.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.serverless.workflow.states.DefaultState;
import org.serverless.workflow.states.EndState;

public class EndStateSerializer extends StdSerializer<EndState> {
    public EndStateSerializer() {
        this(EndState.class);
    }

    protected EndStateSerializer(Class<EndState> t) {
        super(t);
    }

    @Override
    public void serialize(EndState endState, JsonGenerator gen, SerializerProvider provider) throws IOException {

        // set defaults for end state
        endState.setStart(false);
        endState.setType(DefaultState.Type.END);
        if(endState.getName() == null || endState.getName().length() < 1) {
            endState.setName("endstate");
        }

        // serialize after setting default bean values...
        BeanSerializerFactory.instance.createSerializer(provider, SimpleType.construct(EndState.class)).serialize(endState, gen, provider);
    }
}

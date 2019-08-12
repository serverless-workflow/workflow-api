package org.serverless.workflow.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.serverless.workflow.states.DefaultState;
import org.serverless.workflow.states.DelayState;

public class DelayStateSerializer extends StdSerializer<DelayState> {
    public DelayStateSerializer() {
        this(DelayState.class);
    }

    protected DelayStateSerializer(Class<DelayState> t) {
        super(t);
    }

    @Override
    public void serialize(DelayState delayState, JsonGenerator gen, SerializerProvider provider) throws IOException {

        // set defaults for delay state
        delayState.setType(DefaultState.Type.DELAY);
        if(delayState.getName() == null || delayState.getName().length() < 1) {
            delayState.setName("delaystate");
        }

        // serialize after setting default bean values...
        BeanSerializerFactory.instance.createSerializer(provider, SimpleType.construct(DelayState.class)).serialize(delayState, gen, provider);
    }
}

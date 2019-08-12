package org.serverless.workflow.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.serverless.workflow.states.DefaultState;
import org.serverless.workflow.states.EventState;

public class EventStateSerializer extends StdSerializer<EventState> {

    public EventStateSerializer() {
        this(EventState.class);
    }

    protected EventStateSerializer(Class<EventState> t) {
        super(t);
    }

    @Override
    public void serialize(EventState eventState,
                          JsonGenerator gen,
                          SerializerProvider provider) throws IOException {

        // set defaults for end state
        eventState.setType(DefaultState.Type.EVENT);
        if (eventState.getName() == null || eventState.getName().length() < 1) {
            eventState.setName("eventstate");
        }

        // serialize after setting default bean values...
        BeanSerializerFactory.instance.createSerializer(provider,
                                                        SimpleType.construct(EventState.class)).serialize(eventState,
                                                                                                          gen,
                                                                                                          provider);
    }
}

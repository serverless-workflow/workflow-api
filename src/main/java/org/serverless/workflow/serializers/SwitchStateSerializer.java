package org.serverless.workflow.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.serverless.workflow.states.DefaultState;
import org.serverless.workflow.states.SwitchState;

public class SwitchStateSerializer extends StdSerializer<SwitchState> {

    public SwitchStateSerializer() {
        this(SwitchState.class);
    }

    protected SwitchStateSerializer(Class<SwitchState> t) {
        super(t);
    }

    @Override
    public void serialize(SwitchState switchState,
                          JsonGenerator gen,
                          SerializerProvider provider) throws IOException {

        // set defaults for end state
        switchState.setType(DefaultState.Type.SWITCH);
        if (switchState.getName() == null || switchState.getName().length() < 1) {
            switchState.setName("switchstate");
        }

        // serialize after setting default bean values...
        BeanSerializerFactory.instance.createSerializer(provider,
                                                        SimpleType.construct(SwitchState.class)).serialize(switchState,
                                                                                                           gen,
                                                                                                           provider);
    }
}

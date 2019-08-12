package org.serverless.workflow.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.serverless.workflow.interfaces.State;
import org.serverless.workflow.states.DefaultState;
import org.serverless.workflow.states.DelayState;
import org.serverless.workflow.states.EndState;
import org.serverless.workflow.states.EventState;
import org.serverless.workflow.states.OperationState;
import org.serverless.workflow.states.ParallelState;
import org.serverless.workflow.states.SwitchState;

public class StateDeserializer extends StdDeserializer<State> {

    public StateDeserializer() {
        this(null);
    }

    public StateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public State deserialize(JsonParser jp,
                             DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = jp.getCodec().readTree(jp);
        String type = node.get("type").asText();

        // based on statetype return the specific state impl
        if (type.equals("EVENT")) {
            return mapper.treeToValue(node,
                                      EventState.class);
        } else if (type.equalsIgnoreCase("OPERATION")) {
            return mapper.treeToValue(node,
                                      OperationState.class);
        } else if (type.equalsIgnoreCase("SWITCH")) {
            return mapper.treeToValue(node,
                                      SwitchState.class);
        } else if (type.equalsIgnoreCase("DELAY")) {
            return mapper.treeToValue(node,
                                      DelayState.class);
        } else if (type.equalsIgnoreCase("PARALLEL")) {
            return mapper.treeToValue(node,
                                      ParallelState.class);
        } else if (type.equalsIgnoreCase("END")) {
            return mapper.treeToValue(node,
                                      EndState.class);
        } else {
            return mapper.treeToValue(node,
                                      DefaultState.class);
        }
    }
}

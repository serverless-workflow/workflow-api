package org.serverless.workflow.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.serverless.workflow.choices.AndChoice;
import org.serverless.workflow.choices.DefaultChoice;
import org.serverless.workflow.choices.NotChoice;
import org.serverless.workflow.choices.OrChoice;
import org.serverless.workflow.interfaces.Choice;

public class ChoiceDeserializer extends StdDeserializer<Choice> {

    public ChoiceDeserializer() {
        this(null);
    }

    public ChoiceDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Choice deserialize(JsonParser jp,
                              DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = jp.getCodec().readTree(jp);

        if (node.get("And") != null) {
            return mapper.treeToValue(node,
                                      AndChoice.class);
        } else if (node.get("Not") != null) {
            return mapper.treeToValue(node,
                                      NotChoice.class);
        } else if (node.get("Or") != null) {
            return mapper.treeToValue(node,
                                      OrChoice.class);
        } else {
            return mapper.treeToValue(node,
                                      DefaultChoice.class);
        }
    }
}

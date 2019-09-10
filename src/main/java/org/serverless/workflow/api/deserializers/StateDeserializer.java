/*
 *
 *   Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package org.serverless.workflow.api.deserializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.serverless.workflow.api.interfaces.State;
import org.serverless.workflow.api.states.DefaultState;
import org.serverless.workflow.api.states.DelayState;
import org.serverless.workflow.api.states.EndState;
import org.serverless.workflow.api.states.EventState;
import org.serverless.workflow.api.states.OperationState;
import org.serverless.workflow.api.states.ParallelState;
import org.serverless.workflow.api.states.SwitchState;

public class StateDeserializer extends StdDeserializer<State> {

    public StateDeserializer() {
        this(null);
    }

    public StateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public State deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = jp.getCodec().readTree(jp);
        String typeValue = node.get("type").asText();

        // based on statetype return the specific state impl
        DefaultState.Type type = DefaultState.Type.fromValue(typeValue);
        switch (type) {
            case EVENT:
                return mapper.treeToValue(node, EventState.class);
            case OPERATION:
                return mapper.treeToValue(node, OperationState.class);
            case SWITCH:
                return mapper.treeToValue(node, SwitchState.class);
            case DELAY:
                return mapper.treeToValue(node, DelayState.class);
            case PARALLEL:
                return mapper.treeToValue(node, ParallelState.class);
            case END:
                return mapper.treeToValue(node, EndState.class);
            default:
                return mapper.treeToValue(node, DefaultState.class);
        }
    }
}

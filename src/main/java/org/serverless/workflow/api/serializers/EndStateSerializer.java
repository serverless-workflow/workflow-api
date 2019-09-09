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

package org.serverless.workflow.api.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.serverless.workflow.api.states.DefaultState;
import org.serverless.workflow.api.states.EndState;

public class EndStateSerializer extends StdSerializer<EndState> {

    public EndStateSerializer() {
        this(EndState.class);
    }

    protected EndStateSerializer(Class<EndState> t) {
        super(t);
    }

    @Override
    public void serialize(EndState endState,
                          JsonGenerator gen,
                          SerializerProvider provider) throws IOException {

        // set the id
        if (endState.getId() == null || endState.getId().length() < 1) {
            endState.setId(WorkflowSerializer.generateUniqueId());
        }
        // set defaults for end state
        endState.setStart(false);
        endState.setType(DefaultState.Type.END);
        if (endState.getName() == null || endState.getName().length() < 1) {
            endState.setName("endstate");
        }

        // serialize after setting default bean values...
        BeanSerializerFactory.instance.createSerializer(provider,
                                                        TypeFactory.defaultInstance().constructType(EndState.class)).serialize(endState,
                                                                                                                               gen,
                                                                                                                               provider);
    }
}

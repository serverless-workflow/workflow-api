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
import com.fasterxml.jackson.databind.type.SimpleType;
import org.serverless.workflow.api.states.DefaultState;
import org.serverless.workflow.api.states.DelayState;

public class DelayStateSerializer extends StdSerializer<DelayState> {

    public DelayStateSerializer() {
        this(DelayState.class);
    }

    protected DelayStateSerializer(Class<DelayState> t) {
        super(t);
    }

    @Override
    public void serialize(DelayState delayState,
                          JsonGenerator gen,
                          SerializerProvider provider) throws IOException {

        // set the id
        if (delayState.getId() == null || delayState.getId().length() < 1) {
            delayState.setId(WorkflowSerializer.generateUniqueId());
        }
        // set defaults for delay state
        delayState.setType(DefaultState.Type.DELAY);
        if (delayState.getName() == null || delayState.getName().length() < 1) {
            delayState.setName("delaystate");
        }

        // serialize after setting default bean values...
        BeanSerializerFactory.instance.createSerializer(provider,
                                                        SimpleType.construct(DelayState.class)).serialize(delayState,
                                                                                                          gen,
                                                                                                          provider);
    }
}

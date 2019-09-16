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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.serverless.workflow.api.InitializingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringValueDeserializer extends StdDeserializer<String> {

    private InitializingContext context;
    private static Logger logger = LoggerFactory.getLogger(StringValueDeserializer.class);

    public StringValueDeserializer() {
        this(String.class);
    }

    public StringValueDeserializer(InitializingContext context) {
        this(String.class);
        this.context = context;
    }

    public StringValueDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser jp,
                              DeserializationContext ctxt) throws IOException {

        String value = jp.getText();
        if (context != null && value.trim().startsWith("$$")) {
            try {
                String path = value.substring(1);
                String result = JsonPath.using(Configuration.defaultConfiguration())
                        .parse(context.getContext())
                        .read(path);

                if (result != null) {
                    return result;
                } else {
                    logger.warn("Unable to evaluate json path: " + path);
                    return jp.getText();
                }
            } catch (Exception e) {
                logger.info("Exception trying to evaluate json path: " + e.getMessage());
                return jp.getText();
            }
        } else {
            return jp.getText();
        }
    }
}
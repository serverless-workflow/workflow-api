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

package org.serverless.workflow.api.validation;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.everit.json.schema.FormatValidator;
import org.everit.json.schema.ValidationException;

public class StateTypeValidator implements FormatValidator {

    private static List<String> validStateTypes = Arrays.asList("EVENT",
                                                                "OPERATION",
                                                                "SWITCH",
                                                                "DELAY",
                                                                "PARALLEL",
                                                                "END");

    @Override
    public Optional<String> validate(final String subject) {

        if (validStateTypes.contains(subject)) {
            return Optional.empty();
        } else {
            // since not in workflow.json schema we have to actually throw exception....will raise issue on this
            throw new ValidationException(String.format("Invalid state type: %s",
                                                        subject));
        }
    }

    @Override
    public String formatName() {
        return "validstatetype";
    }
}

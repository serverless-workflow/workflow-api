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

package org.serverless.workflow.api;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultExpressionEvaluator implements ExpressionEvaluator {

    public JexlEngine jexl = new JexlBuilder().create();

    private static Logger logger = LoggerFactory.getLogger(DefaultExpressionEvaluator.class);

    public boolean evaluate(String expression,
                            String triggerName) {
        try {
            JexlExpression e = jexl.createExpression(expression);

            JexlContext jc = new MapContext();
            jc.set("trigger",
                   triggerName);

            return (Boolean) e.evaluate(jc);
        } catch (Exception e) {
            logger.error("Unable to evaluate expression: " + expression + " with error: " + e.getMessage());
            return false;
        }
    }
}

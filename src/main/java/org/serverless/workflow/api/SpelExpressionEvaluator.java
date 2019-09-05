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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpelExpressionEvaluator implements ExpressionEvaluator {

    private static Logger logger = LoggerFactory.getLogger(SpelExpressionEvaluator.class);

    public boolean evaluate(String expression,
                            String triggerName) {

        try {
            ExpressionParser spelExpressionParser = new SpelExpressionParser();
            Expression spelExpression = spelExpressionParser.parseExpression(expression);

            EvaluationContext context = new StandardEvaluationContext(new SpelRootObject(triggerName));

            return (Boolean) spelExpression.getValue(context);
        } catch (Exception e) {
            logger.error("Unable to evaluate expression: " + expression + " with error: " + e.getMessage());
            return false;
        }
    }

    protected class SpelRootObject {

        private String trigger;

        public SpelRootObject(String trigger) {
            this.trigger = trigger;
        }

        public String getTrigger() {
            return trigger;
        }

        public void setTrigger(String trigger) {
            this.trigger = trigger;
        }
    }
}

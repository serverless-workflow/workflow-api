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
package org.serverless.workflow.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

import org.serverless.workflow.api.InitContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitContextProvider {

    private InitContext initContext;

    private static Logger logger = LoggerFactory.getLogger(WorkflowValidatorProvider.class);

    public InitContextProvider() {
        ServiceLoader<InitContext> foundInitContext = ServiceLoader.load(InitContext.class);
        Iterator<InitContext> it = foundInitContext.iterator();
        if (it.hasNext()) {
            initContext = it.next();
            logger.info("Found init context: " + initContext.toString());
        }
    }

    private static class LazyHolder {

        static final InitContextProvider INSTANCE = new InitContextProvider();
    }

    public static InitContextProvider getInstance() {
        return InitContextProvider.LazyHolder.INSTANCE;
    }

    public InitContext get() {
        return initContext;
    }
}

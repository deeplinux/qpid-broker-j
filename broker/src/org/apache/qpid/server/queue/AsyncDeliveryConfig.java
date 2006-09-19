/*
 *
 * Copyright (c) 2006 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.apache.qpid.server.queue;

import org.apache.qpid.configuration.Configured;
import org.apache.qpid.server.registry.ApplicationRegistry;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncDeliveryConfig
{
    private Executor _executor;

    @Configured(path = "delivery.poolsize", defaultValue = "0")
    public int poolSize;

    public Executor getExecutor()
    {
        if (_executor == null)
        {
            if (poolSize > 0)
            {
                _executor = Executors.newFixedThreadPool(poolSize);
            }
            else
            {
                _executor = Executors.newCachedThreadPool();
            }
        }
        return _executor;
    }

    public static Executor getAsyncDeliveryExecutor()
    {
        return ApplicationRegistry.getInstance().getConfiguredObject(AsyncDeliveryConfig.class).getExecutor();
    }
}

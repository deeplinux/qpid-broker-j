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
package org.apache.qpid.jms;

public interface BrokerDetails
{

    /*
     * Known URL Options
     * @see ConnectionURL
    */
    public static final String OPTIONS_RETRY = "retries";
    public static final String OPTIONS_SSL = ConnectionURL.OPTIONS_SSL;
    public static final String OPTIONS_CONNECT_TIMEOUT = "connecttimeout";
    public static final int DEFAULT_PORT = 5672;
    public static final String DEFAULT_TRANSPORT = "tcp";

    public static final String URL_FORMAT_EXAMPLE =
            "<transport>://<hostname>[:<port Default=\""+DEFAULT_PORT+"\">][?<option>='<value>'[,<option>='<value>']]";

    public static final long DEFAULT_CONNECT_TIMEOUT = 30000L;

    String getHost();
    void setHost(String host);

    int getPort();
    void setPort(int port);

    String getTransport();
    void setTransport(String transport);

    boolean useSSL();
    void useSSL(boolean ssl);

    String getOption(String key);
    void setOption(String key,String value);

    long getTimeout();
    void setTimeout(long timeout);

    String toString();

    boolean equals(Object o);
}

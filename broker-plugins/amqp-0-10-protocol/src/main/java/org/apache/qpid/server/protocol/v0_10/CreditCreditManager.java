/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
package org.apache.qpid.server.protocol.v0_10;


public class CreditCreditManager implements FlowCreditManager_0_10
{
    private volatile long _bytesCredit;
    private volatile long _messageCredit;

    public CreditCreditManager(long bytesCredit, long messageCredit)
    {
        _bytesCredit = bytesCredit;
        _messageCredit = messageCredit;
    }

    @Override
    public synchronized void restoreCredit(final long messageCredit, final long bytesCredit)
    {
    }


    @Override
    public synchronized void addCredit(final long messageCredit, final long bytesCredit)
    {
        if(_messageCredit >= 0L && messageCredit > 0L)
        {
            _messageCredit += messageCredit;
        }

        if(_bytesCredit >= 0L && bytesCredit > 0L)
        {
            _bytesCredit += bytesCredit;
        }
    }

    @Override
    public synchronized void clearCredit()
    {
        _bytesCredit = 0l;
        _messageCredit = 0l;
    }


    @Override
    public synchronized boolean hasCredit()
    {
        // Note !=, if credit is < 0 that indicates infinite credit
        return (_bytesCredit != 0L  && _messageCredit != 0L);
    }

    @Override
    public synchronized boolean useCreditForMessage(long msgSize)
    {
        if(_messageCredit >= 0L)
        {
            if(_messageCredit > 0)
            {
                if(_bytesCredit < 0L)
                {
                    _messageCredit--;

                    return true;
                }
                else if(msgSize <= _bytesCredit)
                {
                    _messageCredit--;
                    _bytesCredit -= msgSize;

                    return true;
                }
                else
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else if(_bytesCredit >= 0L)
        {
            if(msgSize <= _bytesCredit)
            {
                 _bytesCredit -= msgSize;

                return true;
            }
            else
            {
                return false;
            }

        }
        else
        {
            return true;
        }

    }

}

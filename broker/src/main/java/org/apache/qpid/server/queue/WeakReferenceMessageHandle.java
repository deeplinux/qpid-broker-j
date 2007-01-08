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
package org.apache.qpid.server.queue;

import org.apache.qpid.AMQException;
import org.apache.qpid.framing.BasicContentHeaderProperties;
import org.apache.qpid.framing.BasicPublishBody;
import org.apache.qpid.framing.ContentBody;
import org.apache.qpid.framing.ContentHeaderBody;
import org.apache.qpid.server.store.MessageStore;
import org.apache.qpid.server.store.StoreContext;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

/**
 * @author Robert Greig (robert.j.greig@jpmorgan.com)
 */
public class WeakReferenceMessageHandle implements AMQMessageHandle
{
    private WeakReference<ContentHeaderBody> _contentHeaderBody;

    private WeakReference<BasicPublishBody> _publishBody;

    private List<WeakReference<ContentBody>> _contentBodies;

    private boolean _redelivered;

    private final MessageStore _messageStore;

    public WeakReferenceMessageHandle(MessageStore messageStore)
    {
        _messageStore = messageStore;
    }

    public ContentHeaderBody getContentHeaderBody(long messageId) throws AMQException
    {
        ContentHeaderBody chb = (_contentHeaderBody != null?_contentHeaderBody.get():null);
        if (chb == null)
        {
            MessageMetaData mmd = _messageStore.getMessageMetaData(messageId);
            chb = mmd.getContentHeaderBody();
            _contentHeaderBody = new WeakReference<ContentHeaderBody>(chb);
            _publishBody = new WeakReference<BasicPublishBody>(mmd.getPublishBody());
        }
        return chb;
    }

    public int getBodyCount(long messageId) throws AMQException
    {
        if (_contentBodies == null)
        {
            MessageMetaData mmd = _messageStore.getMessageMetaData(messageId);
            int chunkCount = mmd.getContentChunkCount();
            _contentBodies = new ArrayList<WeakReference<ContentBody>>(chunkCount);
            for (int i = 0; i < chunkCount; i++)
            {
                _contentBodies.add(new WeakReference<ContentBody>(null));
            }
        }
        return _contentBodies.size();
    }

    public long getBodySize(long messageId) throws AMQException
    {
        return getContentHeaderBody(messageId).bodySize;
    }

    public ContentBody getContentBody(long messageId, int index) throws AMQException, IllegalArgumentException
    {
        if (index > _contentBodies.size() - 1)
        {
            throw new IllegalArgumentException("Index " + index + " out of valid range 0 to " +
                                               (_contentBodies.size() - 1));
        }
        WeakReference<ContentBody> wr = _contentBodies.get(index);
        ContentBody cb = wr.get();
        if (cb == null)
        {
            cb = _messageStore.getContentBodyChunk(messageId, index);
            _contentBodies.set(index, new WeakReference<ContentBody>(cb));
        }
        return cb;
    }

    /**
     * Content bodies are set <i>before</i> the publish and header frames
     * @param storeContext
     * @param messageId
     * @param contentBody
     * @throws AMQException
     */
    public void addContentBodyFrame(StoreContext storeContext, long messageId, ContentBody contentBody) throws AMQException
    {
        if (_contentBodies == null)
        {
            _contentBodies = new LinkedList<WeakReference<ContentBody>>();
        }
        _contentBodies.add(new WeakReference<ContentBody>(contentBody));
        _messageStore.storeContentBodyChunk(storeContext, messageId, _contentBodies.size() - 1, contentBody);
    }

    public BasicPublishBody getPublishBody(long messageId) throws AMQException
    {
        BasicPublishBody bpb = (_publishBody != null?_publishBody.get():null);
        if (bpb == null)
        {
            MessageMetaData mmd = _messageStore.getMessageMetaData(messageId);
            bpb = mmd.getPublishBody();
            _publishBody = new WeakReference<BasicPublishBody>(bpb);
            _contentHeaderBody = new WeakReference<ContentHeaderBody>(mmd.getContentHeaderBody());
        }
        return bpb;
    }

    public boolean isRedelivered()
    {
        return _redelivered;
    }

    public void setRedelivered(boolean redelivered)
    {
        _redelivered = redelivered;
    }

    public boolean isPersistent(long messageId) throws AMQException
    {
        //todo remove literal values to a constant file such as AMQConstants in common
        ContentHeaderBody chb = getContentHeaderBody(messageId);
        return chb.properties instanceof BasicContentHeaderProperties &&
               ((BasicContentHeaderProperties) chb.properties).getDeliveryMode() == 2;
    }

    /**
     * This is called when all the content has been received.
     * @param publishBody
     * @param contentHeaderBody
     * @throws AMQException
     */
    public void setPublishAndContentHeaderBody(StoreContext storeContext, long messageId, BasicPublishBody publishBody,
                                               ContentHeaderBody contentHeaderBody)
            throws AMQException
    {
        // if there are no content bodies the list will be null so we must
        // create en empty list here
        if (contentHeaderBody.bodySize == 0)
        {
            _contentBodies = new LinkedList<WeakReference<ContentBody>>();
        }
        _messageStore.storeMessageMetaData(storeContext, messageId, new MessageMetaData(publishBody, contentHeaderBody,
                                                                                        _contentBodies.size()));
        _publishBody = new WeakReference<BasicPublishBody>(publishBody);
        _contentHeaderBody = new WeakReference<ContentHeaderBody>(contentHeaderBody);
    }

    public void removeMessage(StoreContext storeContext, long messageId) throws AMQException
    {
        _messageStore.removeMessage(storeContext, messageId);
    }

    public void enqueue(StoreContext storeContext, long messageId, AMQQueue queue) throws AMQException
    {
        _messageStore.enqueueMessage(storeContext, queue.getName().toString(), messageId);
    }

    public void dequeue(StoreContext storeContext, long messageId, AMQQueue queue) throws AMQException
    {
        _messageStore.dequeueMessage(storeContext, queue.getName().toString(), messageId);
    }
}

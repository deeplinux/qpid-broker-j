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
package org.apache.qpid.framing;

import org.apache.mina.common.ByteBuffer;
import org.apache.qpid.AMQChannelException;

public abstract class AMQMethodBody extends AMQBody
{
    public static final byte TYPE = 1;    

    /** unsigned short */
    protected abstract int getBodySize();

    /**
     * @return unsigned short
     */
    protected abstract int getClazz();

    /**
     * @return unsigned short
     */
    protected abstract int getMethod();

    protected abstract void writeMethodPayload(ByteBuffer buffer);

    protected byte getType()
    {
        return TYPE;
    }

    protected int getSize()
    {
        return 2 + 2 + getBodySize();
    }

    protected void writePayload(ByteBuffer buffer)
    {
        EncodingUtils.writeUnsignedShort(buffer, getClazz());
        EncodingUtils.writeUnsignedShort(buffer, getMethod());
        writeMethodPayload(buffer);
    }

    protected abstract void populateMethodBodyFromBuffer(ByteBuffer buffer) throws AMQFrameDecodingException;

    protected void populateFromBuffer(ByteBuffer buffer, long size) throws AMQFrameDecodingException
    {
        populateMethodBodyFromBuffer(buffer);
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer(getClass().toString());
        buf.append(" Class: ").append(getClazz());
        buf.append(" Method: ").append(getMethod());
        return buf.toString();
    }

    /**
     * Creates an AMQChannelException for the corresponding body type (a channel exception
     * should include the class and method ids of the body it resulted from).
     */
    public AMQChannelException getChannelException(int code, String message)
    {
        return new AMQChannelException(code, message, getClazz(), getMethod());
    }

    public AMQChannelException getChannelException(int code, String message, Throwable cause)
    {
        return new AMQChannelException(code, message, getClazz(), getMethod(), cause);
    }
}

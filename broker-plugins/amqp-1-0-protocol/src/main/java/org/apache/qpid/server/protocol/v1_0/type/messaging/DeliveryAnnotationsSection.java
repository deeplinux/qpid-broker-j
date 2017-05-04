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

package org.apache.qpid.server.protocol.v1_0.type.messaging;

import java.util.Map;

import org.apache.qpid.server.protocol.v1_0.codec.AbstractDescribedTypeConstructor;
import org.apache.qpid.server.protocol.v1_0.codec.DescribedTypeConstructorRegistry;
import org.apache.qpid.server.protocol.v1_0.messaging.SectionEncoder;
import org.apache.qpid.server.protocol.v1_0.type.Symbol;
import org.apache.qpid.server.protocol.v1_0.type.messaging.codec.DeliveryAnnotationsConstructor;

public class DeliveryAnnotationsSection extends AbstractSection<Map<Symbol,Object>, DeliveryAnnotations>
{
    public DeliveryAnnotationsSection(final DescribedTypeConstructorRegistry describedTypeRegistry)
    {
        super(describedTypeRegistry);
    }

    DeliveryAnnotationsSection(final DeliveryAnnotations deliveryAnnotations, final SectionEncoder sectionEncoder)
    {
        super(deliveryAnnotations, sectionEncoder);
    }

    @Override
    protected AbstractDescribedTypeConstructor<DeliveryAnnotations> createNonEncodingRetainingSectionConstructor()
    {
        return new DeliveryAnnotationsConstructor();
    }

}

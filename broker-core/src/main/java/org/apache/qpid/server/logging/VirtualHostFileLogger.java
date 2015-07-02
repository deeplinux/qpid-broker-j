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
package org.apache.qpid.server.logging;


import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.qpid.server.model.DerivedAttribute;
import org.apache.qpid.server.model.ManagedAttribute;
import org.apache.qpid.server.model.ManagedObject;
import org.apache.qpid.server.model.ManagedOperation;
import org.apache.qpid.server.model.Param;
import org.apache.qpid.server.model.Content;
import org.apache.qpid.server.model.VirtualHostLogger;

@ManagedObject( category = false, type = VirtualHostFileLogger.TYPE)
public interface VirtualHostFileLogger<X extends VirtualHostFileLogger<X>> extends VirtualHostLogger<X>
{
    String TYPE = "File";
    String FILE_NAME = "fileName";

    @ManagedAttribute( defaultValue = "${virtualhost.work_dir}${file.separator}log${file.separator}${this:name}.log")
    String getFileName();

    @ManagedAttribute( defaultValue = "false")
    boolean isRollDaily();

    @ManagedAttribute( defaultValue = "false")
    boolean isRollOnRestart();

    @ManagedAttribute( defaultValue = "false")
    boolean isCompressOldFiles();

    @ManagedAttribute( defaultValue = "1")
    int getMaxHistory();

    @ManagedAttribute( defaultValue = "100mb")
    String getMaxFileSize();

    @ManagedAttribute(defaultValue = "%d %-5p [%t] \\(%c{2}\\) - %m%n")
    String getLayout();

    @DerivedAttribute
    Collection<LogFileDetails> getLogFiles();

    @ManagedOperation(nonModifying = true)
    Content getFile(@Param(name = "fileName") String fileName);

    @ManagedOperation(nonModifying = true)
    Content getFiles(@Param(name = "fileName") Set<String> fileName);

    @ManagedOperation(nonModifying = true)
    Content getAllFiles();
}

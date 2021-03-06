/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lealone.orm.property;

import java.sql.Timestamp;

/**
 * Property for java sql Timestamp.
 *
 * @param <R> the root model bean type
 */
public class PTimestamp<R> extends PBaseDate<R, Timestamp> {

    /**
     * Construct with a property name and root instance.
     *
     * @param name property name
     * @param root the root model bean instance
     */
    public PTimestamp(String name, R root) {
        super(name, root);
    }

    /**
     * Construct with additional path prefix.
     */
    public PTimestamp(String name, R root, String prefix) {
        super(name, root, prefix);
    }
}

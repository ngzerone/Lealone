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

import java.io.IOException;
import java.util.HashMap;

import org.lealone.db.value.Value;
import org.lealone.db.value.ValueInt;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;

/**
 * Integer property.
 *
 * @param <R> the root model bean type
 */
public class PInteger<R> extends PBaseNumber<R, Integer> {

    private int value;

    /**
     * Construct with a property name and root instance.
     *
     * @param name property name
     * @param root the root model bean instance
     */
    public PInteger(String name, R root) {
        super(name, root);
    }

    /**
     * Construct with additional path prefix.
     */
    public PInteger(String name, R root, String prefix) {
        super(name, root, prefix);
    }

    public final R set(int value) {
        if (!areEqual(this.value, value)) {
            this.value = value;
            // if (isReady()) {
            expr().set(name, ValueInt.get(value));
            // }
        }
        return root;
    }

    @Override
    public R set(Object value) {
        return set(Integer.valueOf(value.toString()).intValue());
    }

    public final int get() {
        return value;
    }

    @Override
    public R serialize(JsonGenerator jgen) throws IOException {
        jgen.writeNumberField(getName(), value);
        return root;
    }

    @Override
    public R deserialize(JsonNode node) {
        node = getJsonNode(node);
        if (node == null) {
            return root;
        }
        set(((NumericNode) node).asInt());
        return root;
    }

    @Override
    public R deserialize(HashMap<String, Value> map) {
        Value v = map.get(name);
        if (v != null) {
            value = v.getInt();
        }
        return root;
    }
}

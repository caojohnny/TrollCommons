/*
 * Copyright 2014 AgentTroll
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gmail.woodyc40.commons.instrument.refs;

import com.gmail.woodyc40.commons.collect.HashStructSet;
import com.gmail.woodyc40.commons.instrument.experimental.ConstantNotFoundException;
import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import sun.reflect.ConstantPool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * The reference to the entire constant pool itself
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
public class PoolRef { // TODO
    /** The reference to the constant references */
    private final Set<ConstantRef> refs = new HashStructSet<>();
    /** The reference to the actual Constant Pool */
    private final ConstantPool constantPool;
    /** The holder of the constant pool */
    private final Class<?>     clazz;

    /**
     * Builds a new constant pool reference from the javassist constant pool
     *
     * @param pool the constant pool
     */
    public PoolRef(Class<?> clazz, ConstantPool pool) {
        this.constantPool = pool;
        this.clazz = clazz;
        for (int i = 0; i < this.constantPool.getSize(); i++) this.refs.add(this.read(i));
    }

    /**
     * Adds an entry to the constant pool
     *
     * @param ref the reference to the entry to add
     */
    public void addRef(ConstantRef ref) {
        this.refs.add(ref);
        // TODO how are we gonna add this?
    }

    /**
     * Read the value at the given index
     *
     * @param i the index to read the constant pool value
     * @return the reference to the constant given at the index
     */
    public ConstantRef read(int i) {
        Class<?> cls = null;
        Double d = null;
        Field fld = null;
        Float f = null;
        Integer in = null;
        Long l = null;
        String[] memberRefInfo = null;
        Method method = null;
        String s = null;
        String utf = null;

        try {
            cls = this.constantPool.getClassAt(i);
            d = this.constantPool.getDoubleAt(i);
            fld = this.constantPool.getFieldAt(i);
            f = this.constantPool.getFloatAt(i);
            in = this.constantPool.getIntAt(i);
            l = this.constantPool.getLongAt(i);
            memberRefInfo = this.constantPool.getMemberRefInfoAt(i);
            method = (Method) this.constantPool.getMethodAt(i);
            s = this.constantPool.getStringAt(i);
            utf = this.constantPool.getUTF8At(i);
        } catch (Exception ignored) {
        }

        if (cls != null) return new ConstantRef(ConstantRef.Type.CLASS, cls, i);
        else if (d != null) return new ConstantRef(ConstantRef.Type.DOUBLE, d, i);
        else if (fld != null) return new ConstantRef(ConstantRef.Type.FIELD, fld, i);
        else if (f != null) return new ConstantRef(ConstantRef.Type.FLOAT, f, i);
        else if (in != null) return new ConstantRef(ConstantRef.Type.INTEGER, in, i);
        else if (l != null) return new ConstantRef(ConstantRef.Type.LONG, l, i);
        else if (memberRefInfo != null) return new ConstantRef(ConstantRef.Type.MEMBER_REF, memberRefInfo, i);
        else if (method != null) return new ConstantRef(ConstantRef.Type.METHOD, method, i);
        else if (s != null) return new ConstantRef(ConstantRef.Type.STRING, s, i);
        else if (utf != null) return new ConstantRef(ConstantRef.Type.UTF8, utf, i);
        else {
            UnsafeProvider.exception(new ConstantNotFoundException(this.clazz, i));
            return null;
        }
    }

    /**
     * Gets the maximum index of the wrapped constant pool
     *
     * @return the last index of which a constant entry can be found at
     */
    public int getSize() {
        return this.constantPool.getSize();
    }

    /**
     * Complete adding the values to the constant pool and push changes to the javassist long vector
     */
    public void finish() {

    }
}

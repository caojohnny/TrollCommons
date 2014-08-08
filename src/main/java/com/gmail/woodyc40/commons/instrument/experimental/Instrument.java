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

package com.gmail.woodyc40.commons.instrument.experimental;

import com.gmail.woodyc40.commons.instrument.CpTransformer;
import com.gmail.woodyc40.commons.instrument.refs.ConstantRef;
import com.gmail.woodyc40.commons.instrument.refs.PoolRef;
import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import sun.misc.SharedSecrets;
import sun.reflect.ConstantPool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Base accessor for experimental assembly instrumentation
 *
 * @author AgentTroll
 * @version 1.0
 */
public class Instrument implements com.gmail.woodyc40.commons.instrument.Instrument {
    /** The class to instrument */
    private final Class<?>     base;
    /** The class constant pool */
    private final ConstantPool constantPool;
    /** The references to the constants */
    private final List<ConstantRef>  refs         = new ArrayList<>();
    /** The transformers for the constant pool */
    private final Set<CpTransformer> transformers = new HashSet<>(); // Work on this ! TODO

    /**
     * Builds a new instrumentation handler
     *
     * @param base the class to instrument
     */
    public Instrument(Class<?> base) {
        this.base = base;
        this.constantPool = SharedSecrets.getJavaLangAccess().getConstantPool(this.base);

        for (int i = 0; i < this.constantPool.getSize(); i++) {
            try {
            } catch (Exception ignored) {
            }
        }
    }

    @Override public void acceptTransformer(CpTransformer transformer) {
        this.transformers.add(transformer);
    }

    @Override public PoolRef getConstantPool() {
        return null; // TODO
    }

    @Override public void finish() {
        for (int i = 0; i < this.constantPool.getSize(); i++) {
            for (CpTransformer trans : this.transformers) {
                Class<?> cls = null;
                Double d = null;
                Field fld = null;
                Float f = null;
                Integer in = null;
                Long l = null;
                String[] memberRefInfo = new String[0];
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
                } catch (Exception ignored) {}

                if (cls != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.CLASS, cls, i));
                    UnsafeProvider.getProvider().putObject(cls, 8L, ref.getValue());
                } else if (d != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.DOUBLE, d, i));
                    UnsafeProvider.getProvider().putObject(d, 8L, ref.getValue());
                } else if (fld != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.FIELD, fld, i));
                    UnsafeProvider.getProvider().putObject(fld, 8L, ref.getValue());
                } else if (f != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.FLOAT, f, i));
                    UnsafeProvider.getProvider().putObject(f, 8L, ref.getValue());
                } else if (in != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.INTEGER, in, i));
                    UnsafeProvider.getProvider().putObject(i, 8L, ref.getValue());
                } else if (l != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.LONG, l, i));
                    UnsafeProvider.getProvider().putObject(l, 8L, ref.getValue());
                } else if (memberRefInfo != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.MEMBER_REF, memberRefInfo, i));
                    UnsafeProvider.getProvider().putObject(memberRefInfo, 8L, ref.getValue());
                } else if (method != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.METHOD, method, i));
                    UnsafeProvider.getProvider().putObject(method, 8L, ref.getValue());
                } else if (s != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.STRING, s, i));
                    UnsafeProvider.getProvider().putObject(s, 8L, ref.getValue());
                } else if (utf != null) {
                    ConstantRef ref = trans.transform(this, new ConstantRef(ConstantRef.Type.UTF8, utf, i));
                    UnsafeProvider.getProvider().putObject(utf, 8L, ref.getValue());
                } else {
                    UnsafeProvider.exception(new ConstantNotFoundException(this.base, i));
                }
            }
        }
    }
}

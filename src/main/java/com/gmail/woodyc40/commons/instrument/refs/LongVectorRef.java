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

import com.gmail.woodyc40.commons.reflection.*;
import com.gmail.woodyc40.commons.reflection.impl.ReflectAccess;
import com.gmail.woodyc40.commons.reflection.impl.ReflectionCache;

import java.util.*;

/**
 * A reference to the long vector from javassist.bytecode.LongVector
 *
 * @author AgentTroll
 * @version 1.0
 */
public class LongVectorRef {
    static final  int                           ASIZE      = 128;
    static final  int                           ABITS      = 7;
    static final  int                           VSIZE      = 8;
    private final MethodManager<Object, Object> size       =
            ReflectAccess.accessMethod(
                    ReflectionTool.forMethod("size", ReflectionCache.getClass("javassist.bytecode.LongVector")));
    private final MethodManager<Object, Object> elementAt  =
            ReflectAccess.accessMethod(
                    ReflectionTool.forMethod("elementAt",
                                             ReflectionCache.getClass("javassist.bytecode.LongVector"),
                                             int.class));
    private final MethodManager<Object, Object> addElement =
            ReflectAccess.accessMethod(
                    ReflectionTool.forMethod("elementAt",
                                             ReflectionCache.getClass("javassist.bytecode.LongVector"),
                                             ReflectionCache.getClass("javassist.bytecode.ConstPool$ConstInfo")));
    private final FieldManager<Object, Object>  object     = ReflectAccess.accessField(
            ReflectionTool.forField("objects", ReflectionCache.getClass("javassist.bytecode.LongVector"))
    );
    private final Object          longVector;
    private       ConstantRef[][] objects;
    private       int             elements;

    public LongVectorRef(Object longVector) {
        int size = (int) this.size.invoke(longVector);
        this.longVector = longVector;

        int vsize = (size >> LongVectorRef.ABITS & ~(LongVectorRef.VSIZE - 1)) + LongVectorRef.VSIZE;
        this.objects = new ConstantRef[vsize][];
        this.elements = 0;

        for (int i = 0; i < size; i++) this.addElement(new ConstantRef(this.elementAt.invoke(longVector, i), i));
    }

    public int size() { return this.elements; }

    public int capacity() { return this.objects.length * LongVectorRef.ASIZE; }

    public ConstantRef elementAt(int i) {
        if (i < 0 || this.elements <= i)
            return null;

        return this.objects[i >> LongVectorRef.ABITS][i & LongVectorRef.ASIZE - 1];
    }

    public void addElement(ConstantRef value) {
        int nth = this.elements >> LongVectorRef.ABITS;
        int offset = this.elements & LongVectorRef.ASIZE - 1;
        int len = this.objects.length;
        if (nth >= len) {
            ConstantRef[][] newObj = new ConstantRef[len + LongVectorRef.VSIZE][];
            System.arraycopy(this.objects, 0, newObj, 0, len);
            this.objects = newObj;
        }

        if (this.objects[nth] == null)
            this.objects[nth] = new ConstantRef[LongVectorRef.ASIZE];

        this.objects[nth][offset] = value;
        this.elements++;
    }

    public List<ConstantRef> asList() {
        List<ConstantRef> list = new ArrayList<>();
        for (ConstantRef[] object1 : this.objects) {
            Collections.addAll(list, object1);
        }
        return list;
    }

    public void finish() {
        Object[][] array = (Object[][]) this.object.get(this.longVector);
        int size = (int) this.size.invoke(this.longVector);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = null;
            }
        }

        List<ConstantRef> list = this.asList();
        for (ConstantRef ref : list) {
            this.addElement.invoke(this.longVector, ref.toConstInfo());
        }
    }
}

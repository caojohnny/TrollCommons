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

package com.gmail.woodyc40.commons;

import com.gmail.woodyc40.commons.instrument.CpTransformer;
import com.gmail.woodyc40.commons.instrument.Instrument;
import com.gmail.woodyc40.commons.instrument.refs.ConstantRef;
import com.gmail.woodyc40.commons.instrument.refs.PoolRef;
import com.gmail.woodyc40.commons.misc.Pair;
import javassist.*;
import javassist.bytecode.Descriptor;
import org.bukkit.Bukkit;

public final class InstrumentTest {
    private InstrumentTest() {}

    public static void main(String... args) throws NotFoundException {
        Instrument instrument = new com.gmail.woodyc40.commons.instrument.asm.Instrument(InstrumentTest.class);
        instrument.acceptTransformer(new InstrumentTest.Transformer());
    }

    public void getOnline() {
        Bukkit.getServer().getOnlinePlayers();
    }

    private static class Transformer implements CpTransformer {
        private PoolRef ref;
        private int transformIndex;

        @Override public ConstantRef transform(ConstantRef ref) {
            if (ref.getValue() instanceof Pair && ref.getType().getTag() == ConstantRef.Type.METHOD.getTag()) {
                // Get ref at transformIndex
                ConstantRef ref1 = new ConstantRef(ConstantRef.Type.NAME_AND_TYPE, 0);
                Pair pair = (Pair) ref1.getValue();
                int desc = (int) pair.getValue();

                // Get another ref at the desc
                ConstantRef ref2 = new ConstantRef(ConstantRef.Type.UTF8, 1);
                try {
                    ref2.setValue(Descriptor.ofMethod(
                            ClassPool.getDefault().get("java.util.Collection"),
                            new CtClass[0]
                    )); // Set the descriptor to return Collection, instead of possibly Player[]
                } catch (NotFoundException e) {
                    e.printStackTrace();
                }
            }

            return ref;
        }

        @Override public PoolRef transform(PoolRef ref) {
            this.ref = ref;
            return ref;
        }
    }
}

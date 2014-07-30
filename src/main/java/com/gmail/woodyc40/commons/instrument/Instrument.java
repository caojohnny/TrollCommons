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

package com.gmail.woodyc40.commons.instrument;

/**
 * Instrumentation access that forms the framework layer for instrumentation utilities
 *
 * @author AgentTroll
 * @version 1.0
 */
public interface Instrument {
    /**
     * Adds a constant pool iteration check transformer
     *
     * @param transformer the transformer to add
     */
    void acceptTransformer(CpTransformer transformer);

    /**
     * Finish transforming the class and reload it, saving the changes.
     */
    void finish();
}

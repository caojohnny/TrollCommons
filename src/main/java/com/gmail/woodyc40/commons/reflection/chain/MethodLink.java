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

package com.gmail.woodyc40.commons.reflection.chain;

/**
 *
 */
public interface MethodLink extends Link {
    @Override void last(int index);

    @Override void param(Object... obj);

    /**
     * The invocation handler
     *
     * @return the means to invoke the method
     */
    Invoker invoker();

    /**
     * Adds a method to the chain
     *
     * @param name the name of the method
     * @param args the method parameters
     * @return the invoker
     */
    MethodLink invoke(String name, Class[] args);

    /**
     * Adds a method to the chain
     *
     * @param type the return type of the method
     * @param args the method parameter count
     * @param index the index of the method list to link the next chain
     * @return the invoker
     */
    MethodLink invokeFuzzy(Class<Object> type, int args, int index);

    /**
     * Adds a method to the chain
     *
     * @param type the return type of the method
     * @param index the index of the method list to link the next chain
     * @return the invoker
     */
    MethodLink invokeFuzzy(Class<Object> type, int index);

    /**
     * Adds a method to the chain
     *
     * @param args the method parameters
     * @param index the index of the method list to link the next chain
     * @return the invoker
     */
    MethodLink invokeFuzzy(Class[] args, int index);

    /**
     * Adds a method to the chain
     *
     * @param args the method parameter count
     * @param index the index of the method list to link the next chain
     * @return the invoker
     */
    MethodLink invokeFuzzy(int args, int index);

    /**
     * Invokes the method and returns the next link builder
     *
     * @author AgentTroll
     * @version 1.0
     */
    interface Invoker {
        /**
         * Invokes the method
         *
         * @param params the instance to invoke with
         * @return the next link
         */
        ReflectionChain invoke(Object instance);
    }
}

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

/**
 * Implementations of Managers in {@link com.gmail.woodyc40.commons.reflection} package for faster reflection than
 * legacy Java Reflection API.
 *
 * <p>Uses a lot of backing C calls in {@link sun.misc.Unsafe}, hence the JNI overhead induced doing so.
 * {@link com.gmail.woodyc40.commons.reflection.impl.FieldImpl} uses only Unsafe to get/set fields.</p>
 *
 * <p>Methods and constructors use {@link sun.reflect} to access the Java low level reflect API.</p>
 */

package com.gmail.woodyc40.commons.reflection.impl;

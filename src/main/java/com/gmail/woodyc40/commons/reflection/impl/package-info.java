/**
 * Implementations of Managers in {@link com.gmail.woodyc40.commons.reflection} package for faster reflection than
 * legacy Java Reflection API.
 *
 * <p>
 * Uses a lot of backing C calls in {@link sun.misc.Unsafe}, hence the JNI overhead induced doing so.
 * {@link com.gmail.woodyc40.commons.reflection.impl.FieldImpl} uses only Unsafe to get/set fields.
 *
 * <p>
 * Methods and constructors use ASM methods to add bytecode to class and unsafe cast in order to invoke or
 * instantiate.
 */

package com.gmail.woodyc40.commons.reflection.impl;
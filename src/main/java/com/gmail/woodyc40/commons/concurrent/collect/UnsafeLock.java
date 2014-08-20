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

package com.gmail.woodyc40.commons.concurrent.collect;

import com.gmail.woodyc40.commons.providers.UnsafeProvider;
import com.gmail.woodyc40.commons.reflection.ReflectionTool;
import lombok.Getter;
import sun.misc.Unsafe;

/**
 * Implementation of the internal lock structure
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.1
 */
public class UnsafeLock implements InternalLock {
    /** Provided unsafe access to increase the throughput of concurrent ops */
    private static final Unsafe UNSAFE   = UnsafeProvider.getProvider();
    /** Lock state for "held by other thread" */
    private static final int    LOCKED   = 1;
    /** Lock state for "free to acquire" */
    private static final int    UNLOCKED = 0;

    static {
        UnsafeLock.stateOff = UnsafeProvider.fieldOffset0(ReflectionTool.forField("state", UnsafeLock.class));
        UnsafeLock.tailOff = UnsafeProvider.fieldOffset0(ReflectionTool.forField("tail", UnsafeLock.class));
    }

    /** The tail offset */
    private static long tailOff;
    /** Offset for the lock state, used for atomic updates */
    private static long stateOff;

    /** Thread node linked list tail */
    private volatile UnsafeLock.ThreadNode tail = new UnsafeLock.ThreadNode(null);
    /** The lock state */
    private volatile int state;

    @Override public void lock() {
        while (!this.updateState(UnsafeLock.UNLOCKED, UnsafeLock.LOCKED)) {
            Thread thread = Thread.currentThread();
            if (!thread.equals(this.tail.getThread())) {
                this.updateTail(this.tail, new UnsafeLock.ThreadNode(thread));
                UnsafeLock.UNSAFE.park(false, 0L);
            }
        }

        this.updateTail(this.tail, new UnsafeLock.ThreadNode(Thread.currentThread()));
    }

    @Override public void unlock() {
        if (this.updateState(UnsafeLock.LOCKED, UnsafeLock.UNLOCKED)) {
            UnsafeLock.ThreadNode previous = this.tail.getPrevious();
            if (previous == null) return;

            this.updateTail(this.tail, previous);
            UnsafeLock.UNSAFE.unpark(this.tail.getThread());
        }
    }

    /**
     * Performs a CAS operation on the state
     *
     * @param expected the expected state
     * @param state    the new state to be set
     * @return {@code true} if the operation succeeds
     */
    private boolean updateState(int expected, int state) {
        return UnsafeLock.UNSAFE.compareAndSwapInt(this, UnsafeLock.stateOff, expected, state);
    }

    /**
     * Performs CAS operation to replace the tail
     *
     * @param expected the expected tail
     * @param tail     the new tail to set
     */
    private void updateTail(UnsafeLock.ThreadNode expected, UnsafeLock.ThreadNode tail) {
        while (!UnsafeLock.UNSAFE.compareAndSwapObject(this, UnsafeLock.tailOff, expected, tail))
            UnsafeLock.UNSAFE.putOrderedObject(this, UnsafeLock.tailOff, tail);
    }

    /**
     * Linked-list node part of the waiting list of threads acquiring the lock
     *
     * @author AgentTroll
     * @version 1.0
     * @since 1.1
     */
    private class ThreadNode {
        /** The previous node in the linked list */
        @Getter private final UnsafeLock.ThreadNode previous;
        /** The thread held by the node */
        @Getter private final Thread                thread;

        public ThreadNode(Thread thread) {
            this.thread = thread;
            this.previous = UnsafeLock.this.tail;
        }
    }
}

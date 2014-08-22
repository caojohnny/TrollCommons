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
import lombok.RequiredArgsConstructor;
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
        UnsafeLock.stateOff = UnsafeLock.UNSAFE.objectFieldOffset(ReflectionTool.forField("state", UnsafeLock.class));
        UnsafeLock.tailOff = UnsafeLock.UNSAFE.objectFieldOffset(ReflectionTool.forField("tail", UnsafeLock.class));
        UnsafeLock.headOff = UnsafeLock.UNSAFE.objectFieldOffset(ReflectionTool.forField("head", UnsafeLock.class));
        UnsafeLock.currentOff = UnsafeLock.UNSAFE.objectFieldOffset(ReflectionTool.forField("current",
                                                                                            UnsafeLock.class));

        UnsafeLock.nextOff = UnsafeLock.UNSAFE.objectFieldOffset(ReflectionTool.forField("next",
                                                                                         UnsafeLock.ThreadNode.class));
    }

    /** The tail offset */
    private static long tailOff;
    /** The head offset */
    private static long headOff;
    /** The head offset */
    private static long stateOff;
    /** The current offset */
    private static long currentOff;

    /** The next node offset in ThreadNode */
    private static long nextOff;

    /** Thread node linked list head */
    private volatile UnsafeLock.ThreadNode head = new UnsafeLock.ThreadNode(null); // Do not listen to "can make final"
    /** Thread node linked list tail */
    private volatile UnsafeLock.ThreadNode tail = this.head;
    /** The lock state */
    private volatile int state;
    /** The current holder of the lock */
    private volatile UnsafeLock.ThreadNode current = new UnsafeLock.ThreadNode(null);

    @Override public void lock() {
        Thread thread = Thread.currentThread();
        boolean interrupt = false;

        while (this.state == UnsafeLock.LOCKED && thread != this.current.getThread()) {
            this.insert(thread);

            if (Thread.interrupted())
                interrupt = true;
            UnsafeLock.UNSAFE.park(false, 0L);
        }

        if (this.updateState(UnsafeLock.UNLOCKED, UnsafeLock.LOCKED))
            this.updateCurrent(this.current, new UnsafeLock.ThreadNode(thread));

        if (interrupt)
            thread.interrupt();
    }

    @Override public void unlock() {
        if (this.updateState(UnsafeLock.LOCKED, UnsafeLock.UNLOCKED)) {
            UnsafeLock.ThreadNode head = this.readAndRemove();
            if (head == null) return;

            UnsafeLock.UNSAFE.unpark(head.getThread());
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
     * Performs a CAS operation on the current thread
     *
     * @param expected the expected current node
     * @param current  the new state to be set
     * @return {@code true} if the operation succeeds
     */
    private boolean updateCurrent(UnsafeLock.ThreadNode expected, UnsafeLock.ThreadNode current) {
        return UnsafeLock.UNSAFE.compareAndSwapObject(this, UnsafeLock.currentOff, expected, current);
    }

    /**
     * Performs CAS operation to replace the tail
     *
     * @param expected the expected tail
     * @param tail     the new tail to set
     */
    private boolean updateTail(UnsafeLock.ThreadNode expected, UnsafeLock.ThreadNode tail) {
        return UnsafeLock.UNSAFE.compareAndSwapObject(this, UnsafeLock.tailOff, expected, tail);
    }

    /**
     * Performs CAS operation to replace the head
     *
     * @param expected the expected head
     * @param next the next node to set
     */
    private boolean updateHead(UnsafeLock.ThreadNode expected, UnsafeLock.ThreadNode next) {
        return UnsafeLock.UNSAFE.compareAndSwapObject(this, UnsafeLock.headOff, expected, next);
    }

    /**
     * Performs CAS operation to replace the next node in the thread node
     *
     * @param inst     the instance of ThreadNode to set the next
     * @param expected the expected next node
     * @param next     the next node to set
     */
    private boolean updateNext(UnsafeLock.ThreadNode inst, UnsafeLock.ThreadNode expected, UnsafeLock.ThreadNode next) {
        return UnsafeLock.UNSAFE.compareAndSwapObject(inst, UnsafeLock.nextOff, expected, next);
    }

    /**
     * Inserts the node into the end of the linked list using the Michael & Scott algorithm found on
     * <a href="http://goo.gl/oUaQUT">DevWorks</a>
     *
     * @param thread the thread to add to the linked list
     */
    private void insert(Thread thread) {
        UnsafeLock.ThreadNode n = new UnsafeLock.ThreadNode(thread);
        for (; ; ) {
            UnsafeLock.ThreadNode t = this.tail;
            UnsafeLock.ThreadNode s = t.getNext();
            if (t == this.tail) {
                if (s == null) {
                    if (this.updateNext(t, null, n)) {
                        this.updateTail(t, n);
                        return;
                    }
                } else {
                    this.updateTail(t, s);
                }
            }
        }
    }

    /**
     * Reads the head of the linked list, remove it, and return the original head. Based off of the algorithm found
     * <a href="http://goo.gl/oUaQUT">here</a>
     *
     * @return the original head of the linked list, before replacement with the next
     */
    private UnsafeLock.ThreadNode readAndRemove() {
        for (; ; ) {
            UnsafeLock.ThreadNode h = this.head;
            UnsafeLock.ThreadNode t = this.tail;
            UnsafeLock.ThreadNode first = h.getNext();
            if (h == this.head) {
                if (h == t) {
                    if (first == null)
                        return null;
                    else
                        this.updateTail(t, first);
                } else if (this.updateHead(h, first)) {
                    return first;
                }
            }
        }
    }

    /**
     * Linked-list node part of the waiting list of threads acquiring the lock
     *
     * @author AgentTroll
     * @version 1.0
     * @since 1.1
     */
    @RequiredArgsConstructor
    private class ThreadNode {
        /** The thread held by the node */
        @Getter private final    Thread                thread;
        /** The next node in the linked list */
        @Getter private volatile UnsafeLock.ThreadNode next;
    }
}

package com.gmail.woodyc40.commons.concurrent;

import com.gmail.woodyc40.commons.providers.UnsafeProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * Concurrent locks using, whether directly or indirectly {@link sun.misc.Unsafe}
 *
 * @author AgentTroll
 * @version 1.0
 */
public abstract class UnsafeLock {
    private UnsafeLock() {}
    
    /**
     * Creates a new lock under condition to block
     *
     * @return a blocking lock
     */
    public static final BlockingLock newBlockingLock() {
        return new BlockingLock();
    }

    /**
     * Creates a new lock that will acquire the monitor and hold it until release
     *
     * @param lock the object to lock
     * @return a monitor acquire semantic lock
     */
    public static final MonitorLock newMonitorLock(Object lock) {
        return new MonitorLock(lock);
    }

    /**
     * Returns a locked lock, useful for one-line create and lock
     *
     * <p>
     * Sample usage:
     * <pre>
     * UnsafeLock lock = UnsafeLock.lockFor(UnsafeLock.newBlockingLock());
     * // Critical section
     * lock.unlock();
     * </pre>
     *
     * @param lock the lock to lock for
     * @return the lock that was automatically locked
     */
    public static final UnsafeLock lockFor(UnsafeLock lock) {
        lock.lock();
        return lock;
    }

    /**
     * Lock that will block for other entry threads in the critical section
     *
     * @author AgentTroll
     * @version 1.0
     */
    private final static class BlockingLock extends UnsafeLock {
        /**
         * Whether or not this lock has blocked the critical section
         */
        private final AtomicBoolean locked   = new AtomicBoolean(false);
        /**
         * The thread that is currently holding the monitor
         */
        private final List<Thread>  currents = Collections.
                synchronizedList(new ArrayList<Thread>());

        /**
         * Builds lock for current thread
         */
        public BlockingLock() {
            currents.add(Thread.currentThread());
        }

        /**
         * Gets the next thread in line in the currents collection
         *
         * @return Thread at index 0, or null if there aren't any left
         */
        private Thread next() {
            try {
                return currents.get(0);
            } catch (ArrayIndexOutOfBoundsException x) {
                return null;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void lock() {
            if (locked.get()) return;
            locked.set(true);

            Thread t = Thread.currentThread();
            Thread block;

            while ((block = next()) != null &&
                    block != t)
                LockSupport.park(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unlock() {
            if (!locked.get()) return;

            LockSupport.unpark(next());
            currents.remove(next());

            if (next() == null)
                locked.set(false);
        }
    }

    /**
     * A lock that works by acquiring the monitor for the object instead of blocking thread access
     *
     * @author AgentTroll
     * @version 1.0
     */
    private static final class MonitorLock extends UnsafeLock {
        /**
         * Whether or not this lock has blocked the critical section
         */
        private final AtomicBoolean locked = new AtomicBoolean(false);
        /**
         * The object to acquire the monitor for
         */
        private final Object lock;

        /**
         * Builds a new lock using monitor based semantics
         *
         * @param lock the object to acquire the lock for in a critical section
         */
        public MonitorLock(Object lock) {
            this.lock = lock;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void lock() {
            if (locked.get()) return;
            locked.set(true);

            UnsafeProvider.monitor(true, lock);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void unlock() {
            if (!locked.get()) return;
            locked.set(false);

            UnsafeProvider.monitor(false, lock);
        }
    }

    /**
     * Acquires the monitor for the current lock
     */
    public abstract void lock();

    /**
     * Releases the lock or unblocks the critical section
     */
    public abstract void unlock();
}

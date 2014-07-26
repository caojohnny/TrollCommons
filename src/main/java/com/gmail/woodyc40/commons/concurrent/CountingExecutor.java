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

package com.gmail.woodyc40.commons.concurrent;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.*;

/**
 * An executor that counts the tasks being executed and able to resize with the workload, along with block tasks
 * that are out of the bound of the execution range.
 *
 * @author AgentTroll
 * @version 1.0
 * @see java.util.concurrent.ThreadPoolExecutor
 * @see java.util.concurrent.ExecutorService
 */
@ThreadSafe
public class CountingExecutor extends ThreadPoolExecutor {
    /** The enforced amount of tasks to be executed */
    @GuardedBy("this") private          Semaphore semaphore;
    /** The maximum tasks that can be handled */
    private volatile                    int       max;

    /**
     * Constructs a new ExecutorService that keeps track of the tasks to be executed
     *
     * <p>
     * This is useful for load balancing and statistics
     *
     * @param corePoolSize the amount of threads to create for each task
     * @param maximumPoolSize the max amount of threads to create
     * @param keepAliveTime time for idle threads to be disposed
     * @param unit the unit {@code keepAliveTime} is expressed in
     * @param workQueue the queue of tasks to execute
     */
    private CountingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                             BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

        this.max = maximumPoolSize;
        synchronized (this) {
            this.semaphore = new Semaphore(maximumPoolSize);
        }
    }

    /**
     * Constructs a new ExecutorService that keeps track of the tasks to be executed
     *
     * <p>
     * This is useful for load balancing and statistics
     *
     * <p>
     * This constructs a thread pool with the size if the available processors multiplied by {@code 1.5}, and casted
     * to an {@code int}
     */
    public static CountingExecutor newCountingExecutor() {
        int threads = (int) ((double) Runtime.getRuntime().availableProcessors() * 1.5);
        return new CountingExecutor(threads, threads, 0L, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());
    }

    /**
     * The amount of space left before the thread pool runs out of space
     *
     * <p>
     * The fallback executor should be used when the {@code int} returned is larger than {@code 95}
     *
     * @return the total available task space
     */
    public int available() {
        synchronized (this) {
            return this.semaphore.availablePermits();
        }
    }

    /**
     * The maximum limit of submitted tasks
     *
     * @return the tasks that are able to be submitted without being blocked
     */
    public int max() {
        return this.max;
    }

    /**
     * The amount of the tasks executing over the maximum amount of task slots available
     *
     * @return the percentage of the thread pool consumed by tasks
     */
    public double percentUsed() {
        return (double) ((this.max - this.available()) / this.max);
    }

    /**
     * Recalculates and resizes the thread pool plus the amount available slots for tasks
     *
     * <p>
     * Should be called every 50,000 milliseconds
     */
    public void recalc() {
        int newSize = (int) ((double) Runtime.getRuntime().availableProcessors() * 1.5);

        this.max = newSize;
        super.setCorePoolSize(newSize);
        super.setMaximumPoolSize(newSize);
        synchronized (this) {
            this.semaphore = new Semaphore(newSize);
        }
    }

    @Override protected void beforeExecute(Thread t, Runnable r) {
        try {
            synchronized (this) {
                this.semaphore.acquire();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }

        super.beforeExecute(t, r);
    }

    @Override protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);

        synchronized (this) {
            this.semaphore.release();
        }
    }
}
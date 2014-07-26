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

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class ThreadPoolManager {
    /** The beat count. Resets when the worker threads are recalcualted */
    private static final AtomicInteger beats = new AtomicInteger(0);

    /** The first consumer executor */
    private static final CountingExecutor LOAD     =
            CountingExecutor.newCountingExecutor();
    /** The second consumer executor */
    private static final CountingExecutor LOAD0    =
            CountingExecutor.newCountingExecutor();

    /** Balancing executor */
    private static final ExecutorService BALANCER = Executors.newCachedThreadPool();
    /** The fallback (last ditch) consumer executor */
    private static final CountingExecutor FALLBACK = CountingExecutor.newCountingExecutor();

    // Start the heartbeat regulator
    static {
        try {
            new JavaFork().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks the beat and recalculates on the 1000th beat from {@link com.gmail.woodyc40.commons.concurrent.JavaFork}
     */
    static void beat() {
        if (ThreadPoolManager.beats.get() == 1000) {
            ThreadPoolManager.LOAD.recalc();
            ThreadPoolManager.LOAD0.recalc();
            ThreadPoolManager.beats.set(0);
        }
    }

    /**
     * Submits a task for balancing on one of the thread pools
     *
     * <p>
     * The task MUST be designed asynchronously - there are no guarantees for thread safety, so synchronize,
     * synchronize, synchronize!
     *
     * @param task the task to execute concurrently
     * @param <V> the return type of the task
     */
    public <V> void submit(Callable<V> task) {
        Distributor<V> dist = new Distributor<>(task)
                .addExecutor(ThreadPoolManager.LOAD)
                .addExecutor(ThreadPoolManager.LOAD0)
                .setBalancer(ThreadPoolManager.BALANCER)
                .setFallback(ThreadPoolManager.FALLBACK);

        ThreadPoolManager.BALANCER.submit(dist);
    }

    /**
     * Ends the thread manager
     */
    public void shutdown() {
        ThreadPoolManager.LOAD.shutdownNow();
        ThreadPoolManager.LOAD0.shutdownNow();
        ThreadPoolManager.BALANCER.shutdownNow();
        ThreadPoolManager.FALLBACK.shutdownNow();
    }
}

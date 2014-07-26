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
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.*;

/**
 * Distributes a task amongst a list of {@link com.gmail.woodyc40.commons.concurrent.CountingExecutor}s, using a
 * balancer and fallback ThreadPoolExecutors to assist with load estimates.
 *
 * @param <V> the return value of the Future representing the task submitted
 *
 * @author AgentTroll
 * @version 1.0
 */
@ThreadSafe
public class Distributor<V> implements Callable<Future<V>> {
    /** The worker services */
    @GuardedBy("services") private final Collection<CountingExecutor> services = new HashSet<>();
    /** The task to submit */
    private final Callable<V> task;
    /** The fallback ExecutorService as the 2nd to last ditch executor for new tasks */
    @GuardedBy("this") private CountingExecutor fallback;
    /** The balancing ExecutorService used to handle the distribution */
    @GuardedBy("this") private ExecutorService balancer;

    /**
     * Builds a new distribution for the task provided
     *
     * @param task the task to distribute
     */
    public Distributor(Callable<V> task) {
        this.task = task;
    }

    /**
     * Sets the fallback executor to use if all workers are full
     *
     * @param fallback the fallback executor
     * @return the instance the fallback was added to
     */
    public Distributor<V> setFallback(CountingExecutor fallback) {
        synchronized (this) {
            this.fallback = fallback;
        }

        return this;
    }

    /**
     * Sets the balancing executor to submit the balancing task to
     *
     * @param balancer the balancing executor
     * @return the instance the balancer was added to
     */
    public Distributor<V> setBalancer(ExecutorService balancer) {
        synchronized (this) {
            this.balancer = balancer;
        }

        return this;
    }

    /**
     * Adds an executor to the consumer executor list
     *
     * @param service the worker executor
     * @return the instance the worker was added to
     */
    public Distributor<V> addExecutor(CountingExecutor service) {
        synchronized (this.services) {
            this.services.add(service);
        }

        return this;
    }

    /**
     * Selects the worker to use
     *
     * <p>
     * The first step is to find the worker with the lowest work load
     *
     * <p>
     * The second step checks the work load of the lowest worker, if it is within 5% of 100, the fallback is selected
     *
     * <p>
     * However, if the fallback happens to be at 100% workload, then the task will be moved over to the balancer
     *
     * @return
     * @throws Exception
     */
    @Override public Future<V> call() throws Exception {
        double usage = 100.0;
        CountingExecutor executor = null;
        synchronized (this.services) {
            for (CountingExecutor service : this.services) {
                if (service.percentUsed() < usage) {
                    usage = service.percentUsed();
                    executor = service;
                }
            }
        }

        synchronized (this) {
            if (usage <= 100.0 && usage >= 95.0 && this.fallback.percentUsed() < 100.0) {
                return this.fallback.submit(this.task);
            } else if (this.fallback.percentUsed() == 100.0)
                return this.balancer.submit(this.task);
        }

        return executor != null ? executor.submit(this.task) : null;
    }
}

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

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedTransferQueue;

import javax.annotation.concurrent.ThreadSafe;

/**
 * Thread list to allow task execution in a shared thread scaled with removal
 *
 * <p>Allows assignment of a worker to the user</p>
 *
 * @param <T> the assignment type, if used
 * @author AgentTroll
 */
@ThreadSafe
public class TaskExec<T> {
    // http://git.io/PQtiog is version of this class, copied version handed to TridentSDK BSD license
    private static final Map.Entry<?, ? extends Number> DEF_ENTRY = new AbstractMap.SimpleEntry<>(null, Long.MAX_VALUE);

    private final Map<TaskExec.InnerThread, Integer> scale       = new HashMap<>();
    private final Map<T, TaskExec.InnerThread>       assignments = new HashMap<>();

    /**
     * Create a new executor using the number of threads to scale
     *
     * @param scale the threads to use
     */
    public TaskExec(int scale) {
        for (int i = 0; i < scale; i++) this.scale.put(new TaskExec.InnerThread(), 0);
    }

    private static <T> Map.Entry<T, ? extends Number> minMap(Map<T, ? extends Number> map) {
        Map.Entry<T, ? extends Number> ent = (Map.Entry<T, ? extends Number>) TaskExec.DEF_ENTRY;

        for (Map.Entry<T, ? extends Number> entry : map.entrySet())
            if (entry.getValue().longValue() < ent.getValue().longValue())
                ent = entry;

        return ent;
    }

    /**
     * Gets a thread that has the least amount of assignment uses. You must assign the user before this can scale.
     *
     * @return the thread with the lowest assignments
     */
    public TaskExec.TaskExecutor getScaledThread() {
        Map.Entry<TaskExec.InnerThread, ? extends Number> handler = TaskExec.minMap(this.scale);
        return handler.getKey();
    }

    /**
     * Assigns the scaled thread to the assignment
     *
     * <p>If already assigned, the executor is returned for the fast-path</p>
     *
     * @param executor   the executor associated with the assignment
     * @param assignment the assignment that uses the executor
     * @return the executor assigned
     */
    public TaskExec.TaskExecutor assign(TaskExec.TaskExecutor executor, T assignment) {
        if (!this.assignments.containsKey(assignment)) {
            Map.Entry<TaskExec.InnerThread, ? extends Number> handler = TaskExec.minMap(this.scale);
            TaskExec.InnerThread thread = handler.getKey();

            this.assignments.put(assignment, thread);
            this.scale.put(handler.getKey(), Integer.valueOf(handler.getValue().intValue() + 1));

            return thread;
        }

        return executor;
    }

    /**
     * Removes the assigned thread and reduces by one the scale factor for the thread
     *
     * @param assignment the assignment that uses the executor to be removed
     */
    public void removeAssignment(T assignment) {
        TaskExec.InnerThread thread = this.assignments.remove(assignment);
        if (thread != null) this.scale.put(thread, this.scale.get(thread) + 1);
    }

    public void shutdown() {
        for (TaskExec.InnerThread thread : this.scale.keySet())
            thread.interrupt();
        this.scale.clear();
        this.assignments.clear();
    }

    /**
     * Execution abstraction
     *
     * @author AgentTroll
     */
    public interface TaskExecutor {
        /**
         * Adds the task to the queue
         *
         * @param task the task to add
         */
        void addTask(Runnable task);

        /**
         * Closes the thread and stops execution of new/remaining tasks
         */
        void interrupt();

        /**
         * Thread form
         *
         * @return the thread that is running
         */
        Thread asThread();
    }

    private static class InnerThread implements TaskExec.TaskExecutor {
        private final BlockingQueue<Runnable>             tasks  = new LinkedTransferQueue<>();
        private final TaskExec.InnerThread.DelegateThread thread = new TaskExec.InnerThread.DelegateThread();
        private boolean stopped;
        // Does not need to be volatile because only this thread can change it

        public static TaskExec.InnerThread createThread() {
            TaskExec.InnerThread thread = new TaskExec.InnerThread();
            thread.getThread().start();
            return thread;
        }

        @Override
        public void addTask(Runnable task) {
            try {
                this.tasks.put(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void interrupt() {
            this.getThread().interrupt();
            this.addTask(new Runnable() {
                @Override
                public void run() {
                    TaskExec.InnerThread.this.stopped = true;
                }
            });
        }

        @Override
        public Thread asThread() {
            return this.getThread();
        }

        public TaskExec.InnerThread.DelegateThread getThread() {
            return this.thread;
        }

        private class DelegateThread extends Thread {
            @Override
            public void run() {
                while (!TaskExec.InnerThread.this.stopped) {
                    try {
                        Runnable task = TaskExec.InnerThread.this.tasks.take();
                        task.run();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }
}


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

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.io.File;
import java.io.IOException;

/**
 * Forks the JVM into a separate {@link java.lang.Process}, which runs the heartbeat for
 * {@link com.gmail.woodyc40.commons.concurrent.ThreadPoolManager}
 *
 * @author AgentTroll
 * @version 1.0
 */
@Immutable @ThreadSafe
public class JavaFork {
    /**
     * Beats at minecraft tick rate (20/sec). The while loop sleeps for 2 milliseconds and wakes to count the current
     * time minus the starting time. If the elapsed time is larger than 50, or the time from the last beat exceeds 50,
     * the ThreadPoolManager beats.
     *
     * @param args the command line arguments (none)
     */
    public static void main(String... args) {
        long msec = 0L;
        long current = System.currentTimeMillis();

        while (true) {
            try {
                Thread.sleep(2);
                long elapsed = System.currentTimeMillis() - current;
                if (elapsed >= 50L || msec >= 50L) {
                    ThreadPoolManager.beat();
                    msec = 0L;
                } else msec = elapsed;
            } catch (Exception x) {
                continue;
            }
        }
    }

    /**
     * Forks the JVM and starts a new task on this class
     *
     * @throws IOException when the process cannot be started (for some reason, possibly security restrictions)
     */
    public void start() throws IOException {
        new ProcessBuilder("java com.gmail.woodyc40.commons.concurrent.JavaFork")
                .directory(new File(JavaFork.class.getProtectionDomain().getCodeSource().getLocation().getFile()))
                .inheritIO()
                .start();
    }
}

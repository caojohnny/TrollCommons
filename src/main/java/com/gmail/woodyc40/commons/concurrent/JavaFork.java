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

import com.gmail.woodyc40.commons.io.Files;
import com.gmail.woodyc40.commons.misc.SerializableRunnable;
import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * Forks the JVM into a separate {@link java.lang.Process}, which runs the heartbeat for {@link
 * com.gmail.woodyc40.commons.concurrent.ThreadPoolManager}
 * <p/>
 * <p/>
 * NOT THREAD SAFE.
 *
 * @author AgentTroll
 * @version 1.0
 */
@NotThreadSafe
public class JavaFork {
    /** The remote caller */
    @Getter(AccessLevel.PACKAGE) private static final Remotes FROM_FORK = new Remotes("Fork2TPM");
    /** The process started by the fork of the JVM */
    private static         Process         process;
    /** The instrument */
    @Getter private static Instrumentation instrument;
    /** The remote receiver */
    @Getter(AccessLevel.PACKAGE) private static Remotes TO_FORK;

    public JavaFork(Remotes toFork) {
        JavaFork.TO_FORK = toFork;
    }

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
                Thread.sleep(2L);
                long elapsed = System.currentTimeMillis() - current;
                if (elapsed >= 50L || msec >= 50L) {
                    JavaFork.FROM_FORK.call(new SerializableRunnable<Void>() {
                        private static final long serialVersionUID = 3355592265139036018L;

                        @Override public Void run() {
                            ThreadPoolManager.beat();
                            return null;
                        }
                    });
                    msec = 0L;
                } else msec = elapsed;
            } catch (Exception x) {
                continue;
            }
        }
    }

    /**
     * Acquires the instance of {@link java.lang.instrument.Instrumentation}
     *
     * @param args       the arguments passed (none)
     * @param instrument the instance of the instrument
     * @throws Exception well... Let's hope this doesn't happen
     */
    public static void premain(String args, Instrumentation instrument) throws Exception {
        JavaFork.instrument = instrument;
    }

    /**
     * Forks the JVM and starts a new task on this class
     *
     * @throws IOException when the process cannot be started (for some reason, possibly security restrictions)
     */
    public static void start() throws IOException {
        System.out.println("Starting fork");
        JavaFork.process = new ProcessBuilder("java", "-jar", Files.jarFile().getName())
                .directory(Files.pluginDir())
                .inheritIO()
                .start();
    }

    /**
     * Stops the forked JVM upon exit of the main JVM (ie the CraftBukkit server)
     */
    public static void shutdown() {
        if (JavaFork.process != null)
            JavaFork.process.destroy();
    }
}

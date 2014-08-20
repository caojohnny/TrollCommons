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

import com.gmail.woodyc40.commons.Commons;
import com.gmail.woodyc40.commons.io.*;
import com.gmail.woodyc40.commons.misc.SerializableRunnable;
import com.google.common.io.ByteStreams;
import lombok.AccessLevel;
import lombok.Getter;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Forks the JVM into a separate {@link java.lang.Process}, which runs the heartbeat for {@link
 * com.gmail.woodyc40.commons.concurrent.ThreadPoolManager}
 * <p>
 * <p>NOT THREAD SAFE.</p>
 *
 * @author AgentTroll
 * @version 1.0
 * @since 1.0
 */
@NotThreadSafe
public final class JavaFork {
    /** The remote caller */
    @Getter(AccessLevel.PACKAGE) private static Remotes         FROM_FORK;
    /** The process started by the fork of the JVM */
    private static                              Process         process;
    /** The instrument */
    @Getter private static                      Instrumentation instrument;
    /** The remote receiver */
    @Getter(AccessLevel.PACKAGE) private static Remotes         TO_FORK;

    /** The security file */
    private static File security;

    private JavaFork() {} // Suppress instantiation

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
        }
    }

    /**
     * Acquires the instance of {@link java.lang.instrument.Instrumentation}
     *
     * @param args       the arguments passed (none)
     * @param instrument the instance of the instrument
     */
    public static void premain(String args, Instrumentation instrument) {
        JavaFork.instrument = instrument;
    }

    /**
     * Forks the JVM and starts a new task on this class
     *
     * @throws IOException when the process cannot be started (for some reason, possibly security restrictions)
     */
    public static void start() throws IOException {
        JavaFork.genSecurityFiles();

        JavaFork.process = new ProcessBuilder(Files.executable().getAbsolutePath(),
                                              "-Djava.rmi.server.codebase=file:/" +
                                              Files.jarFile().getAbsolutePath().replaceFirst("/", ""),
                                              "-Djava.security.policy=" + JavaFork.security.getAbsolutePath(),
                                              "-jar",
                                              Files.jarFile().getName())
                .directory(Files.pluginDir())
                .inheritIO()
                .start();

        JavaFork.FROM_FORK = new Remotes("Fork2TPM");
        JavaFork.TO_FORK = new Remotes(JavaFork.FROM_FORK);
    }

    /**
     * Generate the security files in the parent folder
     */
    private static void genSecurityFiles() throws IOException {
        File file = FileWrapper.handleFolder(Commons.getPlugin().getDataFolder());
        final File src = new File(file, "src");
        if (!src.exists()) {
            src.mkdirs();
            File jar = Files.jarFile();
            JarFile jarFile = new JarFile(jar);
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".java"))
                    JavaFork.copy(jarFile, entry);
            }
        }

        JavaFork.security = FileWrapper.handle(new File(file, "server.policy"));
        final StringFileWriter writer = new StringFileWriter(JavaFork.security);

        writer.writeLine("grant codeBase \"file:" + src.getAbsolutePath() + "\" {",
                         "    permission java.security.AllPermission;",
                         "};");

        System.setProperty("java.security.policy", JavaFork.security.getAbsolutePath());
        System.setProperty("java.rmi.server.codebase",
                           "file:/" + src.getAbsolutePath().replaceFirst("/", ""));
    }

    /**
     * Copy source file to data folder
     *
     * @param file  the originating jarfile the entry came from
     * @param entry the entry to copy into the data folder
     */
    public static void copy(final JarFile file, final JarEntry entry) throws IOException {
        final File newFile = new File(Commons.getPlugin().getDataFolder(), entry.getName());
        com.google.common.io.Files.write(ByteStreams.toByteArray(file.getInputStream(entry)), newFile);
    }

    /**
     * Stops the forked JVM upon exit of the main JVM (ie the CraftBukkit server)
     */
    public static void shutdown() {
        if (JavaFork.process != null)
            JavaFork.process.destroy();
    }
}

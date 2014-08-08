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

package com.gmail.woodyc40.commons.io;

import java.io.*;

/**
 * Allows for the functionality to write content to a file
 * <p/>
 * Must be closed when finished!
 *
 * @author AgentTroll
 * @version 1.0
 */
public class StringFileWriter implements Closeable {
    /** The file to write to */
    private final File           file;
    /** The file writer */
    private final BufferedWriter writer;

    /**
     * Builds a new file writer capable of writing Strings to the file
     *
     * @param file the file to write to
     * @throws IOException if the BufferedWriter cannot be created
     */
    public StringFileWriter(File file) throws IOException {
        this.file = file;
        this.writer = new BufferedWriter(new FileWriter(file));
    }

    /**
     * Writes a line to the file
     *
     * @param line the String to write
     */
    public void writeLine(String line) {
        try {
            this.writer.write(line + '\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes multiple lines to the file
     *
     * @param lines the lines to write
     */
    public void writeLine(String... lines) {
        for (String line : lines) {
            this.writeLine(line);
        }
    }

    /**
     * Adds text to the file. This does not append a new line.
     *
     * @param string the string to write
     */
    public void write(String string) {
        try {
            this.writer.write(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds multiple pieces of text to the file. This does not append a new line for each string.
     *
     * @param strings the strings to write
     */
    public void write(String... strings) {
        for (String string : strings) {
            this.write(string);
        }
    }

    @Override public void close() throws IOException {
        this.writer.close();
    }
}

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

import java.io.File;
import java.io.IOException;

/**
 * Handler for creation of files
 *
 * @author AgentTroll
 * @version 1.0
 */
public final class FileWrapper {
    private FileWrapper() {}

    /**
     * Creates the file if non-existant
     *
     * @param file the file to check
     */
    public static File handle(File file) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        return file;
    }

    /**
     * Creates the folder if non-existant
     *
     * @param folder the folder to check
     */
    public static File handleFolder(File folder) {
        if (!folder.exists()) folder.mkdirs();

        return folder;
    }
}

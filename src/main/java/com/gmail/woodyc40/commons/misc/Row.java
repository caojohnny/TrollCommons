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

package com.gmail.woodyc40.commons.misc;

/**
 * The {@code interface} that represents a horizontal row on the ASCII table
 *
 * @author AgentTroll
 * @version 1.0
 */
public interface Row {
    /**
     * Adds the data onto the row
     *
     * @param column the column, starting from 0, to put the data in
     * @param entry  the data
     * @return the row the data was added to
     */
    Row setColumn(int column, String entry);

    /**
     * The entries of data this row holds
     *
     * @return the entries of the row
     */
    String[] getEntries();
}

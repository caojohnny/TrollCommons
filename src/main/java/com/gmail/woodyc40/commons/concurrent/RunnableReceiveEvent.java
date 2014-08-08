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

import com.gmail.woodyc40.commons.event.*;
import com.gmail.woodyc40.commons.misc.SerializableRunnable;
import lombok.*;

/**
 * Called when a runnable is received from {@link com.gmail.woodyc40.commons.concurrent.Remotes}
 *
 * @author AgentTroll
 * @version 1.0
 */
@Getter @RequiredArgsConstructor @Handler(EventType.RUNNABLE_RECEIVE)
public class RunnableReceiveEvent implements CustomEvent {
    /** The runnable called */
    private final SerializableRunnable<?> runnable;
    /** Whether or not this is cancelled */
    @Setter private       boolean                 cancelled;

    @Override public boolean getCancelled() {
        return this.cancelled;
    }
}

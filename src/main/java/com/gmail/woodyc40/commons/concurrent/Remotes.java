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

import com.gmail.woodyc40.commons.event.Events;
import com.gmail.woodyc40.commons.misc.SerializableRunnable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Provides the RMI interface to send the method call from the forked JVM to the parent JVM
 *
 * @author AgentTroll
 * @version 1.0
 */
public class Remotes {
    /** The name of the remote */
    private final String name;

    private final Remotes.Receiver receiver;
    /** The caller */
    private final Remotes.Caller   caller;

    /**
     * Builds a new remote starting the server. The server receives items.
     *
     * @param name the name to set for the remote. Should be unique.
     */
    public Remotes(String name) {
        this.name = name;
        this.receiver = new Remotes.Receiver();
        this.caller = new Remotes.Caller();

        this.getReceiver().start(this.getReceiver());
    }

    /**
     * Builds a new remote starting the client. The client sends the methods.
     *
     * @param server the remote server
     */
    public Remotes(Remotes server) {
        this.name = server.getName();
        this.receiver = new Remotes.Receiver();
        this.caller = new Remotes.Caller();

        this.getReceiver().start(server.getReceiver());
    }

    /**
     * Calls a runnable
     *
     * @param runnable the task to execute
     * @param <T>      the return type of the runnable
     */
    public <T> void call(SerializableRunnable<T> runnable) {
        this.caller.call(runnable);
    }

    /** Remote identifier */
    public String getName() {
        return this.name;
    }

    /** The receiver */
    public Remotes.Receiver getReceiver() {
        return this.receiver;
    }

    /**
     * Receives calls
     *
     * @author AgentTroll
     * @version 1.0
     */
    private class Receiver implements Remote {
        /**
         * Starts listening for callers
         *
         * @param receiver the receiver to use in listening
         */
        public void start(Remote receiver) {
            if (System.getSecurityManager() == null) System.setSecurityManager(new SecurityManager());

            try {
                Remote stub = UnicastRemoteObject.exportObject(receiver, 0);
                Registry registry = LocateRegistry.getRegistry();
                registry.rebind(Remotes.this.getName(), stub);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        /**
         * Method to execute the runnables
         *
         * @param runnable the runnable to run
         * @param <T>      the return type of the runnable
         * @throws RemoteException when an exception occurs while passing the remote
         */
        private <T> void execute(SerializableRunnable<T> runnable) throws RemoteException {
            runnable.run();
            Events.call(new RunnableReceiveEvent(runnable));
        }
    }

    /**
     * The class used to execute methods in the remote JVM
     *
     * @author AgentTroll
     * @version 1.0
     */
    private class Caller {
        /**
         * Calls a runnable to the remote JVM
         *
         * @param task the task to execute
         * @param <T>  the return type of the task
         */
        public <T> void call(SerializableRunnable<T> task) {
            if (System.getSecurityManager() == null) System.setSecurityManager(new SecurityManager());

            try {
                Registry registry = LocateRegistry.getRegistry();
                Remotes.Receiver call = (Remotes.Receiver) registry.lookup(Remotes.this.getName());
                call.execute(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

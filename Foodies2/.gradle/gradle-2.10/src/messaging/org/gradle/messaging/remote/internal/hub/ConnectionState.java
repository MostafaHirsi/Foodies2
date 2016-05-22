/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.messaging.remote.internal.hub;

import org.gradle.messaging.remote.internal.Connection;
import org.gradle.messaging.remote.internal.hub.protocol.InterHubMessage;
import org.gradle.messaging.remote.internal.hub.queue.EndPointQueue;

class ConnectionState {
    private boolean receiveFinished;
    private boolean dispatchFinished;
    private final Connection<InterHubMessage> connection;
    private final ConnectionSet owner;
    private final EndPointQueue dispatchQueue;

    ConnectionState(ConnectionSet owner, Connection<InterHubMessage> connection, EndPointQueue dispatchQueue) {
        this.owner = owner;
        this.connection = connection;
        this.dispatchQueue = dispatchQueue;
    }

    public Connection<InterHubMessage> getConnection() {
        return connection;
    }

    public EndPointQueue getDispatchQueue() {
        return dispatchQueue;
    }

    public void receiveFinished() {
        receiveFinished = true;
        if (!dispatchFinished) {
            dispatchQueue.stop();
        }
        maybeDisconnected();
    }

    public void dispatchFinished() {
        dispatchFinished = true;
        maybeDisconnected();
    }

    private void maybeDisconnected() {
        if (dispatchFinished && receiveFinished) {
            owner.finished(this);
        }
    }
}

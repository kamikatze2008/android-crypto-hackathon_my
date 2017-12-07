package com.bux.crypto.internal.websocket.events;

import com.bux.crypto.internal.core.websocket.WebSocketEvent;
import com.bux.crypto.domain.SocketError;

public class SocketConnectionFailureEvent implements WebSocketEvent {
    private SocketError error;

    public SocketConnectionFailureEvent(SocketError error) {
        this.error = error;
    }

    public SocketError getError() {
        return error;
    }
}

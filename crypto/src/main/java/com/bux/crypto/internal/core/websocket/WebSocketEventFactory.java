package com.bux.crypto.internal.core.websocket;

/**
 * Factory that will produce {@linkplain WebSocketEvent events} based on connection updates in {@link WebSocketConnectionClient}
 */
public interface WebSocketEventFactory {
    /**
     * Returns {@link WebSocketEvent} that is triggered when connection is opened
     */
    WebSocketEvent createOpenConnectionEvent();

    /**
     * Returns {@link WebSocketEvent} that is triggered when connection is closed gracefully
     */
    WebSocketEvent createClosedConnectionEvent(int code, String reason);

    /**
     * Returns {@link WebSocketEvent} that is triggered when {@code #message} is received as {@link String}
     */
    WebSocketEvent createMessageReceivedEvent(String message);

    /**
     * Returns {@link WebSocketEvent} that is triggered when {@code #message} is received as byte array
     */
    WebSocketEvent createMessageReceivedEvent(byte[] message);
}

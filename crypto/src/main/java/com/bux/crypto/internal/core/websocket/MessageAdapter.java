package com.bux.crypto.internal.core.websocket;

/**
 * Adapter that converts {@link Message} into raw message byte array that will be sent via WebSocket connection
 */
public interface MessageAdapter {
    byte[] getRawMessage(Message message);
}

package com.bux.crypto.internal.core.websocket;

/**
 * {@link Exception} that may be thrown in {@link WebSocketConnectionClient} when some errors occur while communicating with server wia web socket
 * connection
 */
public class WebSocketException extends Exception {

    public WebSocketException() {
        super();
    }

    public WebSocketException(String message) {
        super(message);
    }

    public WebSocketException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebSocketException(Throwable cause) {
        super(cause);
    }
}

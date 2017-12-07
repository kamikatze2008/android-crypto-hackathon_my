package com.bux.crypto.internal.core.websocket;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * WebSocket connection client interface that provides methods for getting {@link Observable}'s for current connection, sending messages, etc.
 */
public interface WebSocketConnectionClient {
    int DEFAULT_RECONNECT_DELAY = 1000;
    int DEFAULT_MAX_RECONNECT_ATTEMPTS = 3;

    /**
     * Returns {@link Observable} that emits {@linkplain WebSocketEvent events} produced by the connection. Connection should be opened when at least
     * one {@link Observable} is subscribed to this connection and should be closed once the last observer unsubscribes.
     */
    Observable<WebSocketEvent> getConnectionObservable();

    /**
     * Send {@link Message} to the server via WebSocket connection
     *
     * @param message to be sent
     * @return true if message can be sent, false - otherwise
     */
    boolean sendMessage(Message message);

    /**
     * Force close connection regardless of how many subscriptions this connection currently has
     * @return true if it can be closed. false - otherwise
     */
    boolean closeConnection();

    /**
     * Returns true is connection is Open. false - in all other cases.
     */
    boolean isConnectionOpen();


    /**
     * Builder class that may be used to build {@link WebSocketConnectionClient} with all required configuration set up.
     */
    abstract class Builder {
        protected Map<String, String> headers = new HashMap<>();
        protected String url;
        protected WebSocketEventFactory eventFactory;
        protected MessageAdapter messageAdapter;
        protected int reconnectDelay = DEFAULT_RECONNECT_DELAY;
        protected int maxReconnectAttempts = DEFAULT_MAX_RECONNECT_ATTEMPTS;

        /**
         * Constructs new {@link Builder}
         *
         * @param url base URL that will be used for creating web socket connections
         * @param eventFactory {@linkplain WebSocketEventFactory factory} that produces {@link WebSocketEvent events} based on connection updates
         * @param messageAdapter {@linkplain MessageAdapter adapter} that converts client's {@linkplain Message messages} into raw message
         */
        public Builder(@NonNull String url, @NonNull WebSocketEventFactory eventFactory, @NonNull MessageAdapter messageAdapter) {
            this.url = url;
            this.eventFactory = eventFactory;
            this.messageAdapter = messageAdapter;
        }

        /**
         * Set base URL for the web socket connection
         */
        public Builder setUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        /**
         * Set headers for the web socket connection
         */
        public Builder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        /**
         * Set a single header for the web socket connection
         */
        public Builder setHeader(@NonNull String name, String value) {
            headers.put(name, value);
            return this;
        }

        /**
         * Set re-connect attempts parameters.
         */
        public Builder setReconnectAttempts(int reconnectDelay, int maxReconnectAttempts) {
            this.reconnectDelay = reconnectDelay;
            this.maxReconnectAttempts = maxReconnectAttempts;
            return this;
        }

        /**
         * Set {@link WebSocketEventFactory} that will produce events based on connection updates
         */
        public Builder setEventFactory(@NonNull WebSocketEventFactory eventFactory) {
            this.eventFactory = eventFactory;
            return this;
        }

        /**
         * Build {@link WebSocketConnectionClient}
         */
        public abstract WebSocketConnectionClient build();
    }
}

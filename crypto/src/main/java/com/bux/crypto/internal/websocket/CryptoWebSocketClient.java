package com.bux.crypto.internal.websocket;

import android.os.Handler;

import com.bux.crypto.internal.core.websocket.Message;
import com.bux.crypto.internal.core.websocket.MessageAdapter;
import com.bux.crypto.internal.core.websocket.WebSocketConnectionClient;
import com.bux.crypto.internal.core.websocket.WebSocketEvent;
import com.bux.crypto.internal.core.websocket.WebSocketEventFactory;
import com.bux.crypto.internal.core.websocket.okhttp.OkHttpWebSocketConnectionClient;
import com.bux.crypto.internal.websocket.events.BuxSocketClosedEvent;
import com.bux.crypto.internal.websocket.events.BuxSocketConnectedEvent;
import com.bux.crypto.internal.websocket.events.BuxSocketOpenEvent;
import com.bux.crypto.internal.websocket.events.CryptoQuoteUpdateEvent;
import com.bux.crypto.internal.websocket.events.SocketConnectionFailureEvent;
import com.bux.crypto.domain.CryptoQuote;
import com.bux.crypto.domain.MessageType;
import com.bux.crypto.domain.SocketError;
import com.bux.crypto.domain.SocketMessage;
import com.bux.crypto.domain.SubscribeMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * This class implements {@link WebSocketConnectionClient} for the Bux server.
 * Currently it works based on OkHttp implementation of web sockets, but this can be changed by passing another {@link WebSocketConnectionClient}
 * implementation to the constructor.
 * <p/>
 * Connection life time is optimized in order to keep connection opened only when client is actively using it. When client is connected but
 * doesn't listen to any of channels, connection will be automatically closed in {@link #INACTIVE_CONNECION_TIMEOUT} time.
 */
public class CryptoWebSocketClient implements WebSocketConnectionClient {
    private static final long INACTIVE_CONNECION_TIMEOUT = TimeUnit.MINUTES.toMillis(5);
    private static final String APP_LANGUAGE = "nl-NL,en;q=0.8";

    private static final String WEBSOCKET_URL = "https://api.dev.getbux.com/crypto/1/subscriptions/me ";
    private WebSocketConnectionClient webSocketConnectionClient;

    private HashSet<String> subscriptions = new HashSet<>();
    private Handler autoCloseConnectionHandler = new Handler();

    private CryptoWebSocketClient(WebSocketConnectionClient webSocketConnectionClient) {
        this.webSocketConnectionClient = webSocketConnectionClient;
    }

    public static CryptoWebSocketClient newInstance(String authorizationToken) {
        Gson gsonParser = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();

        WebSocketEventFactory webSocketEventFactory = new BuxSocketEventFactory(gsonParser);
        MessageAdapter messageAdapter = new BuxMessageAdapter(gsonParser);
        WebSocketConnectionClient okHttpWebSocketConnectionClient =
                new OkHttpWebSocketConnectionClient.Builder(WEBSOCKET_URL, webSocketEventFactory, messageAdapter)
                        .setHeader("Accept-Language", APP_LANGUAGE)
                        .setHeader("Authorization", "Bearer " + authorizationToken)
                        .build();
        return new CryptoWebSocketClient(okHttpWebSocketConnectionClient);
    }

    public Observable<WebSocketEvent> getConnectionObservable() {
        return webSocketConnectionClient.getConnectionObservable();
    }

    @Override
    public boolean sendMessage(Message message) {
        if (message instanceof SubscribeMessage) {
            synchronized (this) {
                subscriptions.addAll(Arrays.asList(((SubscribeMessage) message).getSubscribeTo()));
                subscriptions.removeAll(Arrays.asList(((SubscribeMessage) message).getUnsubscribeFrom()));
                if (subscriptions.isEmpty()) {
                    autoCloseConnectionHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (webSocketConnectionClient.isConnectionOpen()) {
                                webSocketConnectionClient.closeConnection();
                            }
                        }
                    }, INACTIVE_CONNECION_TIMEOUT);
                } else {
                    autoCloseConnectionHandler.removeCallbacksAndMessages(null);
                }
            }
        }
        return webSocketConnectionClient.sendMessage(message);
    }

    @Override
    public boolean closeConnection() {
        return webSocketConnectionClient.closeConnection();
    }

    @Override
    public boolean isConnectionOpen() {
        return webSocketConnectionClient.isConnectionOpen();
    }

    private static class BuxMessageAdapter implements MessageAdapter {
        private Gson gson;

        private BuxMessageAdapter(Gson gson) {
            this.gson = gson;
        }

        @Override
        public byte[] getRawMessage(Message message) {
            String rawMessageString = gson.toJson(message, message.getClass());
            return rawMessageString.getBytes();
        }
    }

    private static class BuxSocketEventFactory implements WebSocketEventFactory {
        private Gson gson;

        private BuxSocketEventFactory(Gson gson) {
            this.gson = gson;
        }

        @Override
        public WebSocketEvent createOpenConnectionEvent() {
            return new BuxSocketOpenEvent();
        }

        @Override
        public WebSocketEvent createClosedConnectionEvent(int code, String reason) {
            return new BuxSocketClosedEvent();
        }

        @Override
        public WebSocketEvent createMessageReceivedEvent(String message) {
            SocketMessage socketMessage = gson.fromJson(message, SocketMessage.class);
            MessageType messageType = socketMessage.getType();
            if (messageType != null) {
                switch (messageType) {
                    case CONNECTED_EVENT:
                        return new BuxSocketConnectedEvent();
                    case CRYPTO_QUOTE:
                        CryptoQuote cryptoQuote = gson.fromJson(socketMessage.getBody(), CryptoQuote.class);
                        return new CryptoQuoteUpdateEvent(cryptoQuote);
                    case ERROR:
                        SocketError error = gson.fromJson(socketMessage.getBody(), SocketError.class);
                        return new SocketConnectionFailureEvent(error);
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public WebSocketEvent createMessageReceivedEvent(byte[] message) {
            return null;
        }
    }
}

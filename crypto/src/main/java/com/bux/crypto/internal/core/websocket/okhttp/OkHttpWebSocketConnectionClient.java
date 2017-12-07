package com.bux.crypto.internal.core.websocket.okhttp;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bux.crypto.BuildConfig;
import com.bux.crypto.internal.core.websocket.Message;
import com.bux.crypto.internal.core.websocket.MessageAdapter;
import com.bux.crypto.internal.core.websocket.WebSocketConnectionClient;
import com.bux.crypto.internal.core.websocket.WebSocketEvent;
import com.bux.crypto.internal.core.websocket.WebSocketEventFactory;
import com.bux.crypto.internal.core.websocket.WebSocketException;

import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import rx.AsyncEmitter;
import rx.Observable;
import rx.functions.Action1;

/**
 * WebSocket client that works with {@link OkHttpClient}. It supports only single web socket connection with multiple observers. Connection will
 * be opened when the first observer subscribes to {@link Observable} returned in {@code getConnectionObservable}, and gracefully closes connection
 * when all subscribers are unsubscribed.
 * <p/>
 * In case of connection failure {@link #maxReconnectAttempts} will be made with the certain {@link #reconnectDelay}. If all
 * reconnection attempts fail, observers will receive an error {@link WebSocketException}
 */

public class OkHttpWebSocketConnectionClient implements WebSocketConnectionClient {
    private static final String TAG = OkHttpWebSocketConnectionClient.class.getName();

    private static final int NORMAL_WEB_SOCKET_CLOSURE = 1000;

    private String url;
    private Headers headers;
    private MessageAdapter messageAdapter;
    private WebSocketEventFactory eventFactory;

    private int reconnectDelay;
    private int maxReconnectAttempts;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private WebSocket webSocket;
    private volatile boolean isOpen = false;
    private volatile boolean isClosing = false;
    private Observable<WebSocketEvent> connectionSourceObservable;
    private AtomicInteger reconnectCounter = new AtomicInteger(0);


    private Handler reconnectionAttemptHandler = new Handler();


    private OkHttpWebSocketConnectionClient(@NonNull String url, Headers headers, @NonNull WebSocketEventFactory eventFactory,
                                            @NonNull MessageAdapter messageAdapter) {
        this.eventFactory = eventFactory;
        this.headers = headers;
        this.url = url;
        this.messageAdapter = messageAdapter;
    }

    /**
     * Returns {@link Observable} that emits {@linkplain WebSocketEvent events} produced on connection updates. Connection will be opened only when
     * the first {@link rx.Observer} subscribes on this {@link Observable} and closed when all subscribers unsubscribe from it.
     */
    public Observable<WebSocketEvent> getConnectionObservable() {
        if (connectionSourceObservable == null) {
            connectionSourceObservable = createNewSourceObservable();
        }
        return connectionSourceObservable;
    }

    /**
     * Send {@link Message} to the server via WebSocket connection.
     *
     * @param message to be sent
     * @return true if message can be sent, false - otherwise
     */
    @Override
    public boolean sendMessage(Message message) {
        if (webSocket != null) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Send message " + new String(messageAdapter.getRawMessage(message)));
            }
            return webSocket.send(ByteString.of(messageAdapter.getRawMessage(message)));
        }
        return false;
    }

    @Override
    public boolean closeConnection() {
        return closeConnectionInt();
    }

    /**
     * Returns true if connection is Open, false - in all other cases.
     */
    @Override
    public boolean isConnectionOpen() {
        return isOpen;
    }

    private Observable<WebSocketEvent> createNewSourceObservable() {
        return Observable
                .fromEmitter(new Action1<AsyncEmitter<WebSocketEvent>>() {
                    @Override
                    public void call(final AsyncEmitter<WebSocketEvent> webSocketEventAsyncEmitter) {
                        openConnection(webSocketEventAsyncEmitter);
                        webSocketEventAsyncEmitter.setCancellation(new AsyncEmitter.Cancellable() {
                            @Override
                            public void cancel() throws Exception {
                                closeConnectionInt();
                            }
                        });
                    }
                }, AsyncEmitter.BackpressureMode.LATEST)
                .share();
    }

    private void openConnection(final AsyncEmitter<WebSocketEvent> eventAsyncEmitter) {
        Request request = new Request.Builder().url(url).headers(headers).build();
        final WebSocketListener listener = new WebSocketListener() {

            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "WebSocket connected");
                }
                isOpen = true;
                eventAsyncEmitter.onNext(eventFactory.createOpenConnectionEvent());
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "WebSocket message received: " + text);
                }
                eventAsyncEmitter.onNext(eventFactory.createMessageReceivedEvent(text));
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                eventAsyncEmitter.onNext(eventFactory.createMessageReceivedEvent(bytes.toByteArray()));
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                if (true) { //TODO
                    Log.d(TAG, "WebSocket connection closed: " + reason);
                }
                isOpen = false;
                isClosing = false;
                eventAsyncEmitter.onNext(eventFactory.createClosedConnectionEvent(code, reason));
                eventAsyncEmitter.onCompleted();
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                Log.e(TAG, "WebSocket connection failed: " + t.getMessage(), t);

                if (!isClosing && reconnectCounter.get() <= maxReconnectAttempts) {
                    reconnectionAttemptHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            reconnectCounter.incrementAndGet();
                            openConnection(eventAsyncEmitter);
                        }
                    }, reconnectDelay);
                } else {
                    reconnectCounter.set(0);
                    eventAsyncEmitter.onError(new WebSocketException("WebSocket connection error", t));
                }
                isOpen = false;
                isClosing = false;
            }
        };
        webSocket = okHttpClient.newWebSocket(request, listener);
    }


    private boolean closeConnectionInt() {
        isClosing = webSocket.close(NORMAL_WEB_SOCKET_CLOSURE, null);
        cancelScheduledReconnect();
        return isClosing;
    }

    private void cancelScheduledReconnect() {
        reconnectionAttemptHandler.removeCallbacksAndMessages(null);
    }

    public static class Builder extends WebSocketConnectionClient.Builder {

        public Builder(@NonNull String url, @NonNull WebSocketEventFactory eventFactory, @NonNull MessageAdapter messageAdapter) {
            super(url, eventFactory, messageAdapter);
        }

        public OkHttpWebSocketConnectionClient build() {
            Headers okHttpHeaders = Headers.of(headers);
            OkHttpWebSocketConnectionClient client = new OkHttpWebSocketConnectionClient(url, okHttpHeaders, eventFactory, messageAdapter);
            client.maxReconnectAttempts = maxReconnectAttempts;
            client.reconnectDelay = reconnectDelay;
            return client;
        }
    }
}

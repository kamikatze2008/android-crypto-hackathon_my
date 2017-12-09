package com.bux.crypto;

import com.bux.crypto.domain.Market;
import com.bux.crypto.domain.SubscribeMessage;
import com.bux.crypto.internal.core.websocket.WebSocketConnectionClient;
import com.bux.crypto.internal.core.websocket.WebSocketEvent;
import com.bux.crypto.internal.websocket.CryptoWebSocketClient;

import java.util.LinkedList;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class CryptoWebSocketConnectionManager {

    private WebSocketConnectionClient webSocketConnectionClient;
    private LinkedList<WebSocketListener> listeners = new LinkedList<>();

    private Subscription webSocketSubscription = null;

    CryptoWebSocketConnectionManager(String authorizationToken) {
        webSocketConnectionClient = CryptoWebSocketClient.newInstance(authorizationToken);
    }

    public void disconnect() {
        webSocketConnectionClient.closeConnection();
    }

    public void subscribeForMarkets(Market... markets) {
        SubscribeMessage.Builder builder = new SubscribeMessage.Builder();
        for (Market market : markets) {
            builder.addSubscription(market);
        }
        webSocketConnectionClient.sendMessage(builder.build());
    }

    public void unsubscribeFromMarkets(Market... markets) {
        SubscribeMessage.Builder builder = new SubscribeMessage.Builder();
        for (Market market : markets) {
            builder.removeSubscription(market);
        }
        webSocketConnectionClient.sendMessage(builder.build());
    }

    public void registerEventsListener(final WebSocketListener listener) {
        listeners.add(listener);
        if (webSocketSubscription == null || webSocketSubscription.isUnsubscribed()) {
            webSocketSubscription = webSocketConnectionClient.getConnectionObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new Action1<WebSocketEvent>() {
                                @Override
                                public void call(WebSocketEvent event) {
                                    notifyListeners(event);
                                }
                            },
                            new Action1<Throwable>() {
                                @Override
                                public void call(Throwable throwable) {
                                    listener.onError(throwable);
                                }
                            });
        }
    }

    public void unregisterEventsListener(WebSocketListener listener) {
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            webSocketSubscription.unsubscribe();
        }
    }

    private void notifyListeners(WebSocketEvent event) {
        for (WebSocketListener listener : listeners) {
            listener.onEventReceived(event);
        }
    }

    public interface WebSocketListener {
        void onEventReceived(WebSocketEvent event);

        void onError(Throwable error);
    }
}

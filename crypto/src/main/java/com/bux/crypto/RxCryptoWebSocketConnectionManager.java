package com.bux.crypto;

import com.bux.crypto.domain.Market;
import com.bux.crypto.domain.SubscribeMessage;
import com.bux.crypto.internal.core.websocket.WebSocketConnectionClient;
import com.bux.crypto.internal.core.websocket.WebSocketEvent;
import com.bux.crypto.internal.websocket.CryptoWebSocketClient;

import rx.Observable;

public class RxCryptoWebSocketConnectionManager {

    private WebSocketConnectionClient webSocketConnectionClient;

    RxCryptoWebSocketConnectionManager(String authorizationToken) {
        webSocketConnectionClient = CryptoWebSocketClient.newInstance(authorizationToken);
    }

    public Observable<WebSocketEvent> getConnectionObservable() {
        return webSocketConnectionClient.getConnectionObservable();
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
}

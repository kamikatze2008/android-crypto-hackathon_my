package com.bux.crypto;

public class RxCrypto {

    private static RxCrypto instance = null;

    private RxCryptoDataManager dataManager;

    private RxCryptoWebSocketConnectionManager webSocketConnectionManager;

    public static RxCrypto getInstance() {
        return instance;
    }

    public RxCryptoDataManager getDataManager() {
        return dataManager;
    }

    public RxCryptoWebSocketConnectionManager getWebSocketConnectionManager() {
        return webSocketConnectionManager;
    }

    public static void init(String authorizationToken) {
        instance = new RxCrypto();
        instance.dataManager = new RxCryptoDataManager(authorizationToken);
        instance.webSocketConnectionManager = new RxCryptoWebSocketConnectionManager(authorizationToken);
    }
}

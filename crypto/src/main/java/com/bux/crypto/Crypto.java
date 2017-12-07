package com.bux.crypto;


public class Crypto {

    private static Crypto instance = null;

    private CryptoDataManager dataManager;

    private CryptoWebSocketConnectionManager webSocketConnectionManager;

    public static Crypto getInstance() {
        return instance;
    }

    public CryptoDataManager getDataManager() {
        return dataManager;
    }

    public CryptoWebSocketConnectionManager getWebSocketConnectionManager() {
        return webSocketConnectionManager;
    }

    public static void init(String authorizationToken) {
        instance = new Crypto();
        instance.dataManager = new CryptoDataManager(authorizationToken);
        instance.webSocketConnectionManager = new CryptoWebSocketConnectionManager(authorizationToken);
    }
}

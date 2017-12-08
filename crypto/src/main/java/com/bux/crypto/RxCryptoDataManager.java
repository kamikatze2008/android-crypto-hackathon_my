package com.bux.crypto;

import com.bux.crypto.domain.AccountDetails;
import com.bux.crypto.domain.Market;
import com.bux.crypto.domain.Order;
import com.bux.crypto.domain.Trade;
import com.bux.crypto.internal.rest.CryptoRetrofitClient;

import rx.Observable;


public class RxCryptoDataManager {
    private CryptoRetrofitClient restServiceClient;

    RxCryptoDataManager(String authorizationToken) {
        restServiceClient = CryptoRetrofitClient.newInstance(authorizationToken);
    }

    public Observable<Market[]> getMarketsObservable() {
        return restServiceClient.getRetrofitInterface().getMarkets();
    }

    public Observable<AccountDetails> getAccountDetailsObservable() {
        return restServiceClient.getRetrofitInterface().getAccountDetails();
    }

    public Observable<Order> buyCryptoObservable(String cryptoAsset, Trade trade) {
        return restServiceClient.getRetrofitInterface().buyCrypto(cryptoAsset, trade);
    }

    public Observable<Order> sellCryptoObservable(String cryptoAsset, Trade trade) {
        return restServiceClient.getRetrofitInterface().sellCrypto(cryptoAsset, trade);
    }

    public Observable<Order> cancelOrderObservable(String cryptoAsset, String orderId) {
        return restServiceClient.getRetrofitInterface().cancelOrder(cryptoAsset, orderId);
    }

}

package com.bux.crypto;


import android.support.annotation.NonNull;

import com.bux.crypto.internal.rest.CryptoRetrofitClient;
import com.bux.crypto.domain.AccountDetails;
import com.bux.crypto.domain.Market;
import com.bux.crypto.domain.Order;
import com.bux.crypto.domain.Trade;

import rx.Observable;
import rx.functions.Action1;

public class CryptoDataManager {

    private CryptoRetrofitClient restServiceClient;

    CryptoDataManager(String authorizationToken) {
        restServiceClient = CryptoRetrofitClient.newInstance(authorizationToken);
    }

    public Observable<Market[]> getMarketsObservable() {
        return restServiceClient.getRetrofitInterface().getMarkets();
    }

    public void getMarkets(@NonNull final DataRequestListener<Market[]> listener) {
        restServiceClient.getRetrofitInterface().getMarkets().subscribe(
                new Action1<Market[]>() {
                    @Override
                    public void call(Market[] markets) {
                        listener.dataReceived(markets);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.requestFailed(throwable.getMessage(), throwable);
                    }
                });
    }

    public Observable<AccountDetails> getAccountDetailsObservable() {
        return restServiceClient.getRetrofitInterface().getAccountDetails();
    }

    public void getAccountDetails(@NonNull final DataRequestListener<AccountDetails> listener) {
        restServiceClient.getRetrofitInterface().getAccountDetails().subscribe(
                new Action1<AccountDetails>() {
                    @Override
                    public void call(AccountDetails accountDetails) {
                        listener.dataReceived(accountDetails);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.requestFailed(throwable.getMessage(), throwable);
                    }
                });
    }

    public Observable<Order> buyCryptoObservable(String cryptoAsset, Trade trade) {
        return restServiceClient.getRetrofitInterface().buyCrypto(cryptoAsset, trade);
    }

    public void buyCrypto(String cryptoAsset, Trade trade, @NonNull final DataRequestListener<Order> listener) {
        restServiceClient.getRetrofitInterface().buyCrypto(cryptoAsset, trade).subscribe(
                new Action1<Order>() {
                    @Override
                    public void call(Order order) {
                        listener.dataReceived(order);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.requestFailed(throwable.getMessage(), throwable);
                    }
                });
    }


    public Observable<Order> sellCryptoObservable(String cryptoAsset, Trade trade) {
        return restServiceClient.getRetrofitInterface().sellCrypto(cryptoAsset, trade);
    }

    public void sellCrypto(String cryptoAsset, Trade trade, @NonNull final DataRequestListener<Order> listener) {
        restServiceClient.getRetrofitInterface().sellCrypto(cryptoAsset, trade).subscribe(
                new Action1<Order>() {
                    @Override
                    public void call(Order order) {
                        listener.dataReceived(order);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.requestFailed(throwable.getMessage(), throwable);
                    }
                });
    }

    public Observable<Order> cancelOrderObservable(String cryptoAsset, String orderId) {
        return restServiceClient.getRetrofitInterface().cancelOrder(cryptoAsset, orderId);
    }

    public void cancelOrderObservable(String cryptoAsset, String orderId, @NonNull final DataRequestListener<Order> listener) {
        restServiceClient.getRetrofitInterface().cancelOrder(cryptoAsset, orderId).subscribe(
                new Action1<Order>() {
                    @Override
                    public void call(Order order) {
                        listener.dataReceived(order);
                    }
                },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        listener.requestFailed(throwable.getMessage(), throwable);
                    }
                });
    }


    public interface DataRequestListener<T> {
        void dataReceived(T data);

        void requestFailed(String errorMessage, Throwable cause);
    }

}

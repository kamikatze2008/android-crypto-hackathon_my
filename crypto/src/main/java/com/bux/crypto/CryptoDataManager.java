package com.bux.crypto;


import android.support.annotation.NonNull;

import com.bux.crypto.domain.AccountDetails;
import com.bux.crypto.domain.Market;
import com.bux.crypto.domain.Order;
import com.bux.crypto.domain.Trade;
import com.bux.crypto.internal.rest.CryptoRetrofitClient;

import rx.functions.Action1;

public class CryptoDataManager {

    private CryptoRetrofitClient restServiceClient;

    CryptoDataManager(String authorizationToken) {
        restServiceClient = CryptoRetrofitClient.newInstance(authorizationToken);
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

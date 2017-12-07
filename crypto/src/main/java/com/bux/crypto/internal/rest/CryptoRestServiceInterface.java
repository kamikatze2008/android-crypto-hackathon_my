package com.bux.crypto.internal.rest;

import com.bux.crypto.domain.AccountDetails;
import com.bux.crypto.domain.Market;
import com.bux.crypto.domain.Order;
import com.bux.crypto.domain.Trade;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

public interface CryptoRestServiceInterface {

    @GET("crypto/1/accounts/me")
    Observable<AccountDetails> getAccountDetails();

    @GET("crypto/1/markets")
    Observable<Market[]> getMarkets();

    @POST("crypto/1/accounts/me/portfolio/{cryptoAsset}/buy")
    Observable<Order> buyCrypto(@Path("cryptoAsset") String cryptoAsset, @Body Trade trade);

    @POST("crypto/1/accounts/me/portfolio/{cryptoAsset}/sell")
    Observable<Order> sellCrypto(@Path("cryptoAsset") String cryptoAsset, @Body Trade trade);

    @DELETE("crypto/1/accounts/me/portfolio/{cryptoAsset}/orders/{orderId}")
    Observable<Order> cancelOrder(@Path("cryptoAsset") String cryptoAsset, @Path("orderId") String orderId);
}

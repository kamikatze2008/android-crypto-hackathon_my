package com.bux.crypto.internal.rest;

import android.support.test.espresso.IdlingResource;

import com.bux.crypto.BuildConfig;
import com.bux.crypto.internal.core.rest.retrofit.BaseRetrofitClient;
import com.bux.crypto.internal.core.rest.retrofit.NetworkExceptionFactory;
import com.bux.crypto.internal.core.rest.retrofit.RxCallAdapterFactory;
import com.bux.crypto.domain.Error;
import com.bux.crypto.internal.tls.OkHttpTls12Patcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jakewharton.espresso.OkHttp3IdlingResource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * {@link com.bux.crypto.internal.core.websocket.WebSocketConnectionClient} configured for Crypto API service
 */
public class CryptoRetrofitClient extends BaseRetrofitClient<CryptoRestServiceInterface> {
    private static final int READ_TIMEOUT_SEC = 30;
    private static final int CONNECT_TIMEOUT_SEC = 30;
    private static final String RESPONSE_TYPE = "application/json";
    private static final String BASE_URL = "https://api.dev.getbux.com/";

    private IdlingResource idlingResource;
    private String authorizationToken;

    public static CryptoRetrofitClient newInstance(String authorizationToken) {
        return new CryptoRetrofitClient(authorizationToken);
    }

    private CryptoRetrofitClient(String token) {
        this.authorizationToken = token;
    }

    @Override
    protected CryptoRestServiceInterface createRetrofitInterface() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT_SEC, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .header("Accept", RESPONSE_TYPE)
                                .header("Authorization", "Bearer " + authorizationToken)
                                .build();
                        return chain.proceed(request);
                    }
                });


        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }

        OkHttpTls12Patcher.patch(okHttpClientBuilder);

        OkHttpClient httpClient = okHttpClientBuilder.build();

        idlingResource = OkHttp3IdlingResource.create("OkHttp", httpClient);

        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        }).create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(new RxCallAdapterFactory(Schedulers.io(), new BuxNetworkExceptionFactory()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(CryptoRestServiceInterface.class);
    }

    public IdlingResource getIdlingResource() {
        return idlingResource;
    }

    private class BuxNetworkExceptionFactory implements NetworkExceptionFactory {
        @Override
        public Throwable httpError(retrofit2.Response response, Retrofit retrofit) {
            if (response != null && response.errorBody() != null) {
                Converter<ResponseBody, Error> converter = retrofit.responseBodyConverter(Error.class, new Annotation[0]);
                try {
                    return new CryptoNetworkException(converter.convert(response.errorBody()));
                } catch (IOException e) {
                    // empty
                }
            }
            return new CryptoNetworkException();

        }

        @Override
        public Throwable networkError(IOException exception) {
            return new CryptoNetworkException(exception);
        }

        @Override
        public Throwable unexpectedError(Throwable exception) {
            return new CryptoNetworkException(exception);
        }
    }
}

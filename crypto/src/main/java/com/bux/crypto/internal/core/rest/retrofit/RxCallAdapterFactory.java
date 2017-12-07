package com.bux.crypto.internal.core.rest.retrofit;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Func1;

/**
 * A {@link retrofit2.CallAdapter.Factory} call adapter which uses RxJava for creating observables and {@link NetworkExceptionFactory} to produce
 * customized exceptions in case of errors.
 */
public class RxCallAdapterFactory extends CallAdapter.Factory {
    private final RxJavaCallAdapterFactory original;
    private final NetworkExceptionFactory exceptionFactory;

    /**
     * Constructs instance of {@link RxCallAdapterFactory} which creates synchronous observables that {@linkplain Observable#subscribeOn(Scheduler)
     * subscribe on} {@code scheduler} by default and produces customized exceptions with the help of {@code exceptionFactory}
     */
    public RxCallAdapterFactory(Scheduler scheduler, NetworkExceptionFactory exceptionFactory) {
        original = RxJavaCallAdapterFactory.createWithScheduler(scheduler);
        this.exceptionFactory = exceptionFactory;
    }

    /**
     * Returns a call adapter for interface methods that return {@code returnType}, or null if it cannot be handled by this factory.
     */
    @Override
    public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        return new RxCallAdapter<>(retrofit, original.get(returnType, annotations, retrofit));
    }

    private class RxCallAdapter<R> implements CallAdapter<R, Observable<R>> {
        private final Retrofit retrofit;
        private final CallAdapter<R, ?> callAdapter;

        private RxCallAdapter(Retrofit retrofit, CallAdapter<R, ?> callAdapter) {
            this.retrofit = retrofit;
            this.callAdapter = callAdapter;
        }

        @Override
        public Type responseType() {
            return callAdapter.responseType();
        }


        @SuppressWarnings("unchecked")
        @Override
        public Observable<R> adapt(@NonNull Call<R> call) {
            return ((Observable) callAdapter.adapt(call)).onErrorResumeNext(new Func1<Throwable, Observable>() {
                @Override
                public Observable call(Throwable throwable) {
                    Throwable requestException;
                    if (throwable instanceof HttpException) {
                        HttpException httpException = (HttpException) throwable;
                        requestException = exceptionFactory.httpError(httpException.response(), retrofit);
                    } else if (throwable instanceof IOException) {
                        requestException = exceptionFactory.networkError((IOException) throwable);
                    } else {
                        requestException = exceptionFactory.unexpectedError(throwable);
                    }
                    return Observable.error(requestException);
                }
            });
        }

    }
}

package com.bux.crypto.internal.core.rest.retrofit;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Factory that is intended to produce customized exceptions depending on the cause of errors occurred while executing http requests
 */
public interface NetworkExceptionFactory {
    /**
     * Create {@link Throwable} for http errors.
     * @param response {@link Response} that was received
     * @return {@link Throwable} new exception
     */
    Throwable httpError(Response response, Retrofit retrofit);

    /**
     * Create {@link Throwable} for network errors not related to common http errors like timeout or bad connection errors, etc.
     * @param exception original {@link IOException} that caused this error
     * @return {@link Throwable} new exception
     */
    Throwable networkError(IOException exception);

    /**
     * Create {@link Throwable} for all unexpected errors.
     * @param exception that causes this error.
     * @return {@link Throwable} exception
     */
    Throwable unexpectedError(Throwable exception);
}

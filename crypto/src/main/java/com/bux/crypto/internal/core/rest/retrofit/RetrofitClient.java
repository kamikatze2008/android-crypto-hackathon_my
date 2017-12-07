package com.bux.crypto.internal.core.rest.retrofit;

import android.support.test.espresso.IdlingResource;

/**
 * Class that provides instance of retrofit interface.
 * Implementations may contain configuration setup of Retrofit.
 * @param <T> class of Retrofit interface
 */
public interface RetrofitClient<T> {
    /**
     * Returns Retrofit interface instance
     */
    T getRetrofitInterface();

    /**
     * Returns Idling resource (for tests)
     */
    IdlingResource getIdlingResource();
}

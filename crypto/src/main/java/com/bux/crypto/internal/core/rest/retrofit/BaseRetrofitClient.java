package com.bux.crypto.internal.core.rest.retrofit;

/**
 * Base class for {@link RetrofitClient} that creates Retrofit interface and returns it upon request.
 * @param <T> class of Retrofit interface
 */
public abstract class BaseRetrofitClient<T> implements RetrofitClient<T> {
    private T apiInterface;

    @Override
    public T getRetrofitInterface() {
        if (apiInterface == null) {
            apiInterface = createRetrofitInterface();
        }
        return apiInterface;
    }

    /**
     * Implement this method with all required configuration set up for Retrofit.
     * @return Retrofit interface instance
     */
    protected abstract T createRetrofitInterface();
}

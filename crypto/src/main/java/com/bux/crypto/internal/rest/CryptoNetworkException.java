package com.bux.crypto.internal.rest;

import com.bux.crypto.domain.Error;

public class CryptoNetworkException extends Throwable {
    private Error errorBody;


    public CryptoNetworkException() {
    }

    public CryptoNetworkException(String message) {
        super(message);
    }

    public CryptoNetworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public CryptoNetworkException(Throwable cause) {
        super(cause);
    }

    public CryptoNetworkException(Error errorBody) {
        this.errorBody = errorBody;
    }

    @Override
    public String getMessage() {
        if (errorBody != null) {
            return errorBody.getMessage();
        }
        return super.getMessage();
    }

    public Error getErrorBody() {
        return errorBody;
    }
}

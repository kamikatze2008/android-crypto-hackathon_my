package com.bux.crypto.domain;

public class Error {
    private String message;
    private String developerMessage;
    private String errorCode;

    public String getMessage() {
        return message;
    }

    public String getDeveloperMessage() {
        return developerMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

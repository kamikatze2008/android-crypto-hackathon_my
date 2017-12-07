package com.bux.crypto.domain;

public enum OrderStatus {
    PENDING_CREATE,
    CREATED,
    PARTIALLY_FILLED,
    FILLED,
    PENDING_CANCEL,
    CANCELLED
}

package com.bux.crypto.domain;

import com.google.gson.annotations.SerializedName;

public enum MessageType {
    @SerializedName("crypto.quote")
    CRYPTO_QUOTE,
    @SerializedName("connect.connected")
    CONNECTED_EVENT,
    @SerializedName("connect.failed")
    ERROR
}

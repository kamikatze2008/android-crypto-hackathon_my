package com.bux.crypto.domain;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class SocketMessage {
    @SerializedName("t")
    private MessageType type;

    private JsonObject body;

    public MessageType getType() {
        return type;
    }

    public JsonObject getBody() {
        return body;
    }

}

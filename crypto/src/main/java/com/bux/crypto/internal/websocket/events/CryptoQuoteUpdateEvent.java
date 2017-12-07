package com.bux.crypto.internal.websocket.events;

import com.bux.crypto.internal.core.websocket.WebSocketEvent;
import com.bux.crypto.domain.CryptoQuote;

public class CryptoQuoteUpdateEvent implements WebSocketEvent {
    private CryptoQuote cryptoQuote;

    public CryptoQuoteUpdateEvent(CryptoQuote cryptoQuote) {
        this.cryptoQuote = cryptoQuote;
    }

    public CryptoQuote getCryptoQuote() {
        return cryptoQuote;
    }
}

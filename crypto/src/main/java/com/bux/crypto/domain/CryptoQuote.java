package com.bux.crypto.domain;

import java.math.BigDecimal;
import java.util.Date;

public class CryptoQuote {
    private String marketName;
    private String baseCurrency;
    private String quoteCurrency;
    private BigDecimal bid;
    private BigDecimal ask;
    private Date timestamp;

    public String getMarketName() {
        return marketName;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public BigDecimal getAsk() {
        return ask;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}

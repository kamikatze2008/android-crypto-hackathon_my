package com.bux.crypto.domain;


import android.os.Parcel;
import android.os.Parcelable;

import com.bux.crypto.internal.websocket.Subscribable;

import java.math.BigDecimal;
import java.util.Date;

public class Market implements Parcelable, Subscribable {
    private String name;
    private String baseCurrency;
    private String quoteCurrency;
    private BigDecimal bestBid;
    private BigDecimal bestAsk;
    private Date lastUpdated;

    public String getName() {
        return name;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public BigDecimal getBestBid() {
        return bestBid;
    }

    public BigDecimal getBestAsk() {
        return bestAsk;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.baseCurrency);
        dest.writeString(this.quoteCurrency);
        dest.writeSerializable(this.bestBid);
        dest.writeSerializable(this.bestAsk);
        dest.writeLong(this.lastUpdated != null ? this.lastUpdated.getTime() : -1);
    }


    protected Market(Parcel in) {
        this.name = in.readString();
        this.baseCurrency = in.readString();
        this.quoteCurrency = in.readString();
        this.bestBid = (BigDecimal) in.readSerializable();
        this.bestAsk = (BigDecimal) in.readSerializable();
        long tmpLastUpdated = in.readLong();
        this.lastUpdated = tmpLastUpdated == -1 ? null : new Date(tmpLastUpdated);
    }

    public static final Parcelable.Creator<Market> CREATOR = new Parcelable.Creator<Market>() {
        @Override
        public Market createFromParcel(Parcel source) {
            return new Market(source);
        }

        @Override
        public Market[] newArray(int size) {
            return new Market[size];
        }
    };

    @Override
    public String getSubscriptionId() {
        return "crypto.quote." + name;
    }
}

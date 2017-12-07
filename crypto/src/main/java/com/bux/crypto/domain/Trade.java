package com.bux.crypto.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class Trade implements Parcelable {

    private MoneyAmount tradeSize;

    private BigDecimal limitPrice;

    public Trade(MoneyAmount tradeSize, BigDecimal limitPrice) {
        this.tradeSize = tradeSize;
        this.limitPrice = limitPrice;
    }

    public MoneyAmount getTradeSize() {
        return tradeSize;
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setTradeSize(MoneyAmount tradeSize) {
        this.tradeSize = tradeSize;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.tradeSize, flags);
        dest.writeSerializable(this.limitPrice);
    }

    protected Trade(Parcel in) {
        this.tradeSize = in.readParcelable(MoneyAmount.class.getClassLoader());
        this.limitPrice = (BigDecimal) in.readSerializable();
    }

    public static final Parcelable.Creator<Trade> CREATOR = new Parcelable.Creator<Trade>() {
        @Override
        public Trade createFromParcel(Parcel source) {
            return new Trade(source);
        }

        @Override
        public Trade[] newArray(int size) {
            return new Trade[size];
        }
    };
}

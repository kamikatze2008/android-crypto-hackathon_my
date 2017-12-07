package com.bux.crypto.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

public class MoneyAmount implements Parcelable {

    private String currency;
    private int decimals;
    private BigDecimal amount;

    public MoneyAmount(BigDecimal amount, String currency, int decimals) {
        this.currency = currency;
        this.decimals = decimals;
        this.amount = amount;
    }

    protected MoneyAmount(Parcel in) {
        currency = in.readString();
        decimals = in.readInt();
        amount = new BigDecimal(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency);
        dest.writeInt(decimals);
        dest.writeString(amount.toPlainString());
    }

    public String getCurrency() {
        return currency;
    }

    public int getDecimals() {
        return decimals;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }


    public static final Creator<MoneyAmount> CREATOR = new Creator<MoneyAmount>() {
        @Override
        public MoneyAmount createFromParcel(Parcel in) {
            return new MoneyAmount(in);
        }

        @Override
        public MoneyAmount[] newArray(int size) {
            return new MoneyAmount[size];
        }
    };
}

package com.bux.crypto.domain;


import android.os.Parcel;
import android.os.Parcelable;

public class AccountBalance implements Parcelable {
    private String quoteCurrency;
    private String baseCurrency;
    private MoneyAmount quantity;
    private MoneyAmount reservedAmount;
    private Transactions[] transactions;
    private Order[] orders;
    private Quote lastQuote;
    private MoneyAmount currentValue;

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public MoneyAmount getQuantity() {
        return quantity;
    }

    public MoneyAmount getReservedAmount() {
        return reservedAmount;
    }

    public Transactions[] getTransactions() {
        return transactions;
    }

    public Quote getLastQuote() {
        return lastQuote;
    }

    public MoneyAmount getCurrentValue() {
        return currentValue;
    }

    public Order[] getOrders() {
        return orders;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quoteCurrency);
        dest.writeString(this.baseCurrency);
        dest.writeParcelable(this.quantity, flags);
        dest.writeParcelable(this.reservedAmount, flags);
        dest.writeTypedArray(this.transactions, flags);
        dest.writeTypedArray(this.orders, flags);
        dest.writeParcelable(this.lastQuote, flags);
        dest.writeParcelable(this.currentValue, flags);
    }


    protected AccountBalance(Parcel in) {
        this.quoteCurrency = in.readString();
        this.baseCurrency = in.readString();
        this.quantity = in.readParcelable(MoneyAmount.class.getClassLoader());
        this.reservedAmount = in.readParcelable(MoneyAmount.class.getClassLoader());
        this.transactions = in.createTypedArray(Transactions.CREATOR);
        this.orders = in.createTypedArray(Order.CREATOR);
        this.lastQuote = in.readParcelable(Quote.class.getClassLoader());
        this.currentValue = in.readParcelable(MoneyAmount.class.getClassLoader());
    }

    public static final Creator<AccountBalance> CREATOR = new Creator<AccountBalance>() {
        @Override
        public AccountBalance createFromParcel(Parcel source) {
            return new AccountBalance(source);
        }

        @Override
        public AccountBalance[] newArray(int size) {
            return new AccountBalance[size];
        }
    };
}

package com.bux.crypto.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountDetails implements Parcelable {
    private String id;
    private AccountBalance baseBalance;
    private AccountBalance[] portfolio;

    public String getId() {
        return id;
    }

    public AccountBalance getBaseBalance() {
        return baseBalance;
    }

    public AccountBalance[] getPortfolio() {
        return portfolio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeParcelable(this.baseBalance, flags);
        dest.writeTypedArray(this.portfolio, flags);
    }

    protected AccountDetails(Parcel in) {
        this.id = in.readString();
        this.baseBalance = in.readParcelable(AccountBalance.class.getClassLoader());
        this.portfolio = in.createTypedArray(AccountBalance.CREATOR);
    }

    public static final Creator<AccountDetails> CREATOR = new Creator<AccountDetails>() {
        @Override
        public AccountDetails createFromParcel(Parcel source) {
            return new AccountDetails(source);
        }

        @Override
        public AccountDetails[] newArray(int size) {
            return new AccountDetails[size];
        }
    };
}

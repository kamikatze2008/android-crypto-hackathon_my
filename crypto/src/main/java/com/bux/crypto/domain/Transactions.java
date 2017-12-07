package com.bux.crypto.domain;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Transactions implements Parcelable {
    private String id;
    private TransactionType type;
    private MoneyAmount amount;
    private String description;
    private Date dateCreated;

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public MoneyAmount getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.amount, flags);
        dest.writeString(this.description);
        dest.writeLong(this.dateCreated != null ? this.dateCreated.getTime() : -1);
    }

    protected Transactions(Parcel in) {
        this.id = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : TransactionType.values()[tmpType];
        this.amount = in.readParcelable(MoneyAmount.class.getClassLoader());
        this.description = in.readString();
        long tmpDateCreated = in.readLong();
        this.dateCreated = tmpDateCreated == -1 ? null : new Date(tmpDateCreated);
    }

    public static final Parcelable.Creator<Transactions> CREATOR = new Parcelable.Creator<Transactions>() {
        @Override
        public Transactions createFromParcel(Parcel source) {
            return new Transactions(source);
        }

        @Override
        public Transactions[] newArray(int size) {
            return new Transactions[size];
        }
    };
}

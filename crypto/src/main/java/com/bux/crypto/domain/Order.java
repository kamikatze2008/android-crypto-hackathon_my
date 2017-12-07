package com.bux.crypto.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private String id;
    private OrderType type;
    private MoneyAmount limit;
    private MoneyAmount quantity;
    private OrderStatus status;

    public String getId() {
        return id;
    }

    public OrderType getType() {
        return type;
    }

    public MoneyAmount getLimit() {
        return limit;
    }

    public MoneyAmount getQuantity() {
        return quantity;
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeParcelable(this.limit, flags);
        dest.writeParcelable(this.quantity, flags);
        dest.writeInt(this.status == null ? -1 : this.status.ordinal());
    }

    protected Order(Parcel in) {
        this.id = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : OrderType.values()[tmpType];
        this.limit = in.readParcelable(MoneyAmount.class.getClassLoader());
        this.quantity = in.readParcelable(MoneyAmount.class.getClassLoader());
        int tmpStatus = in.readInt();
        this.status = tmpStatus == -1 ? null : OrderStatus.values()[tmpStatus];
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel source) {
            return new Order(source);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
}

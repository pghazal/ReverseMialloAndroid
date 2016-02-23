package com.pghazal.reversemiallo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.pghazal.reversemiallo.database.table.FriendTable;

public class Friend implements Parcelable {

    @SerializedName(value = FriendTable.FriendColumn.FRIEND_ID)
    private String id;

    @SerializedName(value = FriendTable.FriendColumn.FRIEND_EMAIL)
    private String email;

    @SerializedName(value = FriendTable.FriendColumn.FRIEND_USERNAME)
    private String username;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public Friend() {
    }

    public Friend(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Friend(String id, String email, String username, String createdAt, String updatedAt) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return getId() + " " + getUsername() + " " + getEmail();
    }

    public Friend(Parcel in) {
        super();
        this.id = in.readString();
        this.username = in.readString();
        this.email = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
        @Override
        public Friend createFromParcel(Parcel source) {
            return new Friend(source);
        }

        @Override
        public Friend[] newArray(int size) {
            return new Friend[size];
        }
    };
}

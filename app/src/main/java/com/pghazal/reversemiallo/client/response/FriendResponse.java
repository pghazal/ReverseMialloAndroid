package com.pghazal.reversemiallo.client.response;

import com.android.volley.Response;
import com.google.gson.annotations.SerializedName;
import com.pghazal.reversemiallo.entity.Friend;

import java.util.List;

/**
 * Created by Pierre Ghazal on 16/02/2016.
 */
public class FriendResponse {

    @SerializedName("friends")
    private List<Friend> friendList;

    public FriendResponse() {
    }

    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }
}

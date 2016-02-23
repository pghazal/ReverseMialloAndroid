package com.pghazal.reversemiallo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pierre Ghazal on 15/02/2016.
 */
public class FriendRequest {

    public interface STATE {
        int SENT = 0;
        int ACCEPTED = 1;
        int REFUSED = 2;
    }

    @SerializedName("id")
    private String id;

    @SerializedName("id_asker")
    private String idAsker;

    @SerializedName("id_new_friend")
    private String idNewFriend;

    @SerializedName("state")
    private int state;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public FriendRequest(String id, String idAsker, String idNewFriend, int state) {
        this.id = id;
        this.idAsker = idAsker;
        this.idNewFriend = idNewFriend;
        this.state = state;
    }

    public FriendRequest(String id, String idAsker, String idNewFriend, String createdAt, String updatedAt, int state) {
        this.id = id;
        this.idAsker = idAsker;
        this.idNewFriend = idNewFriend;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdAsker() {
        return idAsker;
    }

    public void setIdAsker(String idAsker) {
        this.idAsker = idAsker;
    }

    public String getIdNewFriend() {
        return idNewFriend;
    }

    public void setIdNewFriend(String idNewFriend) {
        this.idNewFriend = idNewFriend;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
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
}

package com.pghazal.reversemiallo.entity;

import com.google.gson.annotations.SerializedName;

public class Friend {

    @SerializedName(value = "id")
    private String id;

    @SerializedName(value = "email")
    private String email;

    @SerializedName(value = "username")
    private String username;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

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
}

package com.pghazal.reversemiallo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pierre Ghazal on 15/02/2016.
 */
public class User {

    @SerializedName(value = "id")
    private String id;

    @SerializedName(value = "email")
    private String email;

    @SerializedName(value = "password")
    private String password;

    @SerializedName(value = "username")
    private String username;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public User(String id, String email, String password, String username) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User(String id, String email, String password, String username, String createdAt, String updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

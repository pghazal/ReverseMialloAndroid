package com.pghazal.reversemiallo.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Pierre Ghazal on 15/02/2016.
 */
public class Message {

    @SerializedName("id")
    private String id;

    @SerializedName("id_sender")
    private String idSender;

    @SerializedName("id_receiver")
    private String idReceiver;

    @SerializedName("text")
    private String text;

    @SerializedName("picture_path")
    private String picturePath;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    public Message(String id, String idSender, String idReceiver, String text, String picturePath) {
        this.id = id;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.text = text;
        this.picturePath = picturePath;
    }

    public Message(String id, String idSender, String idReceiver, String text, String picturePath, String createdAt, String updatedAt) {
        this.id = id;
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.text = text;
        this.picturePath = picturePath;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
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

package com.pghazal.reversemiallo.client.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pierre Ghazal on 16/02/2016.
 */
public class ServerResponse<T> {

    @SerializedName("datas")
    private List<T> datas;

}

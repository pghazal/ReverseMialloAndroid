package com.pghazal.reversemiallo.client.request;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Pierre Ghazal on 16/02/2016.
 */
public class RequestManager {

    private static RequestManager ourInstance;

    private Context mContext;
    private RequestQueue mRequestQueue;

    private RequestManager(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized RequestManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new RequestManager(context);
        }

        return ourInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

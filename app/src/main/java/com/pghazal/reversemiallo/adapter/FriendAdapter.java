package com.pghazal.reversemiallo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.client.response.FriendResponse;
import com.pghazal.reversemiallo.entity.Friend;
import com.pghazal.reversemiallo.client.request.GsonRequest;
import com.pghazal.reversemiallo.utility.NetworkUtility;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends ArrayAdapter<Friend> {

    private static final String URL_GET_FRIENDS = "http://www.google.fr";

    private Context mContext;
    private List<Friend> mFriendList;
    private int mResource;

    public FriendAdapter(Context context, int resource) {
        super(context, resource);

        mContext = context;
        mResource = resource;
        mFriendList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mFriendList.size();
    }

    @Override
    public Friend getItem(int position) {
        return mFriendList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());

            convertView = inflater.inflate(mResource, parent, false);

            if (convertView != null) {
                holder.usernameText = (TextView) convertView.findViewById(R.id.usernameText);
                holder.emailText = (TextView) convertView.findViewById(R.id.emailText);

                convertView.setTag(holder);
            }
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Friend friend = getItem(position);

        holder.usernameText.setText(friend.getUsername());
        holder.emailText.setText(friend.getEmail());

        return convertView;
    }

    public void updateAdapterData(List<Friend> friends) {
        if (mFriendList != null) {
            mFriendList.clear();

            if (friends != null) {
                mFriendList.addAll(friends);
                notifyDataSetChanged();
            }
        }
    }

    private static class ViewHolder {
        public TextView usernameText;
        public TextView emailText;
    }

    public void loadFriends() {
        NetworkUtility.getInstance(mContext).addToRequestQueue(
                new GsonRequest<FriendResponse>(Request.Method.GET, URL_GET_FRIENDS,
                        FriendResponse.class, null, new Response.Listener<FriendResponse>() {
            @Override
            public void onResponse(FriendResponse response) {
                updateAdapterData(response.getFriendList());
                notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }));
    }
}

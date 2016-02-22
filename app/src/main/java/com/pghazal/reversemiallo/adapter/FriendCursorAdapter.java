package com.pghazal.reversemiallo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.entity.Friend;
import com.pghazal.reversemiallo.provider.FriendContentProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pierreghazal on 21/02/16.
 */
public class FriendCursorAdapter extends SimpleCursorAdapter {

    public interface OnItemCheckChangeListener {
        public void onItemCheckChangeListener(int position);
        public void setSelectedFriends(List<Friend> friends);
    }

    private static final String TAG = "FriendCursorAdapter";

    private HashMap<String, Integer> mItemCheckedMap = new HashMap<>();
    private OnItemCheckChangeListener mListener;

    public FriendCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to,
                               int flags, HashMap<String, Integer> map) {
        super(context, layout, c, from, to, flags);

        this.mContext = context;
        this.mListener = null;

        if (map != null) {
            mItemCheckedMap.clear();
            mItemCheckedMap.putAll(map);
        }
    }

    public void setOnItemCheckChangeListener(OnItemCheckChangeListener listener) {
        mListener = listener;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolder holder = (ViewHolder) view.getTag();
        final int position = cursor.getPosition();

        final String id = cursor.getString(
                cursor.getColumnIndexOrThrow(FriendTable.FriendColumn._ID));
        String username = cursor.getString(
                cursor.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_USERNAME));
        String email = cursor.getString(
                cursor.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_EMAIL));

        holder.usernameText.setText(username);
        holder.emailText.setText(email);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.checkbox.isChecked()) {
                    mItemCheckedMap.put(id, position);
                    System.out.println("cursor.getPosition(true): " + position);
                } else if (!holder.checkbox.isChecked()) {
                    mItemCheckedMap.remove(id);
                    System.out.println("cursor.getPosition(false): " + position);
                }

                if (mListener != null)
                    mListener.onItemCheckChangeListener(position);
            }
        });

        if (mItemCheckedMap.size() > 0 && mItemCheckedMap.containsKey(id))
            holder.checkbox.setChecked(true);
        else
            holder.checkbox.setChecked(false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.adapter_friend, null);

        ViewHolder holder = new ViewHolder();

        holder.usernameText = (TextView) v.findViewById(R.id.usernameText);
        holder.emailText = (TextView) v.findViewById(R.id.emailText);
        holder.checkbox = (CheckBox) v.findViewById(R.id.checkbox);

        v.setTag(holder);

        return v;
    }

    @Override
    public Friend getItem(int position) {
        Cursor cursor = super.getCursor();

        if (cursor != null) {
            cursor.moveToPosition(position);

            String id = cursor.getString(
                    cursor.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_ID));
            String username = cursor.getString(
                    cursor.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_USERNAME));
            String email = cursor.getString(
                    cursor.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_EMAIL));

            Friend friend = new Friend();
            friend.setId(id);
            friend.setUsername(username);
            friend.setEmail(email);

            return friend;
        }

        return null;
    }

    public HashMap<String, Integer> getMapSelectedFriends() {
        return mItemCheckedMap;
    }

    private Cursor getSelectedFriends() {
        Cursor c = null;

        if (mItemCheckedMap.size() > 0) {
            String[] projection = {
                    FriendTable.FriendColumn._ID,
                    FriendTable.FriendColumn.FRIEND_ID,
                    FriendTable.FriendColumn.FRIEND_USERNAME,
                    FriendTable.FriendColumn.FRIEND_EMAIL
            };

            int size = mItemCheckedMap.size();

            String selection = FriendContentProvider.makePlaceholders(size);

            String[] keys = new String[size];
            Integer[] values = new Integer[size];
            int index = 0;
            for (HashMap.Entry<String, Integer> mapEntry : mItemCheckedMap.entrySet()) {
                keys[index] = mapEntry.getKey();
                values[index] = mapEntry.getValue();
                index++;
            }

            c = mContext.getContentResolver().query(
                    FriendContentProvider.CONTENT_URI, projection,
                    FriendTable.FriendColumn._ID + " IN (" + selection + ")", keys, null);
        }

        return c;
    }

    public int getItemSelectedCount() {
        if (mItemCheckedMap != null) {
            return mItemCheckedMap.size();
        }

        return 0;
    }

    private static class ViewHolder {
        public TextView usernameText;
        public TextView emailText;
        public CheckBox checkbox;
    }

    public class GetSelectedFriendsAsyncTask extends AsyncTask<Void, Void, List<Friend>> {
        public GetSelectedFriendsAsyncTask() {

        }

        @Override
        protected List<Friend> doInBackground(Void... params) {
            List<Friend> friends = new ArrayList<>();

            Cursor c = getSelectedFriends();

            if (c != null) {
                while (c.moveToNext()) {
                    Friend friend = new Friend();
                    friend.setId(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_ID)));
                    friend.setUsername(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_USERNAME)));
                    friend.setEmail(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_EMAIL)));

                    friends.add(friend);
                }

                c.close();
            }

            return friends;
        }

        @Override
        protected void onPostExecute(List<Friend> friends) {
            super.onPostExecute(friends);

            mListener.setSelectedFriends(friends);
        }
    }
}

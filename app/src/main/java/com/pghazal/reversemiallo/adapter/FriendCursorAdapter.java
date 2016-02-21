package com.pghazal.reversemiallo.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.fragment.FriendsFragment;
import com.pghazal.reversemiallo.provider.FriendContentProvider;

import java.util.HashMap;

/**
 * Created by pierreghazal on 21/02/16.
 */
public class FriendCursorAdapter extends SimpleCursorAdapter {

    public interface OnItemCheckChangeListener {
        public void onItemCheckChangeListener();
    }

    private static final String TAG = "FriendCursorAdapter";

    private HashMap<String, Integer> itemCheckedMap = new HashMap<>();
    private OnItemCheckChangeListener mListener;

    public FriendCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to,
                               int flags, HashMap<String, Integer> map) {
        super(context, layout, c, from, to, flags);

        this.mContext = context;
        this.mListener = null;

        if(map != null) {
            itemCheckedMap.clear();
            itemCheckedMap.putAll(map);
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

        holder.checkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (holder.checkbox.isChecked()) {
                    itemCheckedMap.put(id, position);
                    System.out.println("cursor.getPosition(true): " + position);
                } else if (!holder.checkbox.isChecked()) {
                    itemCheckedMap.remove(id);
                    System.out.println("cursor.getPosition(false): " + position);
                }

                if (mListener != null)
                    mListener.onItemCheckChangeListener();
            }
        });

        if (itemCheckedMap.size() > 0 && itemCheckedMap.containsKey(id))
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

    public HashMap<String, Integer> getMapSelectedFriends() {
        return itemCheckedMap;
    }

    public Cursor getSelectedFriends() {
        Cursor c = null;

        if (itemCheckedMap.size() > 0) {
            String[] projection = {
                    FriendTable.FriendColumn._ID,
                    FriendTable.FriendColumn.FRIEND_ID,
                    FriendTable.FriendColumn.FRIEND_USERNAME,
                    FriendTable.FriendColumn.FRIEND_EMAIL
            };

            int size = itemCheckedMap.size();

            String selection = FriendContentProvider.makePlaceholders(size);

            String[] keys = new String[size];
            Integer[] values = new Integer[size];
            int index = 0;
            for (HashMap.Entry<String, Integer> mapEntry : itemCheckedMap.entrySet()) {
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

    private static class ViewHolder {
        public TextView usernameText;
        public TextView emailText;
        public CheckBox checkbox;
    }
}

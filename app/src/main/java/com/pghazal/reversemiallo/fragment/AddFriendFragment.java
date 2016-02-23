package com.pghazal.reversemiallo.fragment;

import android.content.ContentValues;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.activity.LoginActivity;
import com.pghazal.reversemiallo.adapter.FriendCursorAdapter;
import com.pghazal.reversemiallo.database.table.FriendRequestTable;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.entity.Friend;
import com.pghazal.reversemiallo.entity.FriendRequest;
import com.pghazal.reversemiallo.provider.FriendRequestContentProvider;
import com.pghazal.reversemiallo.utility.SettingsUtility;
import com.pghazal.reversemiallo.utility.UIUtility;
import com.pghazal.reversemiallo.widget.ClearableEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pierre Ghazal on 23/02/2016.
 */
public class AddFriendFragment extends ListFragment implements
        FriendCursorAdapter.OnItemCheckChangeListener {

    private static final String TAG = "AddFriendFragment";

    private ListView mListView;
    private ClearableEditText mSearchField;

    private FriendCursorAdapter mAdapter;

    public AddFriendFragment() {
    }

    public static AddFriendFragment newInstance() {
        AddFriendFragment fragment = new AddFriendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "# onCreate");
        setRetainInstance(true);

        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friends, container, false);

        mListView = (ListView) view.findViewById(android.R.id.list);
        mSearchField = (ClearableEditText) view.findViewById(R.id.searchField);
        mSearchField.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAdapter();
        setListeners();
    }

    private void initAdapter() {
        String[] from = new String[]{
                FriendTable.Columns.FRIEND_USERNAME,
                FriendTable.Columns.FRIEND_EMAIL
        };

        int[] to = new int[]{R.id.usernameText, R.id.emailText};
        mAdapter = new FriendCursorAdapter(getActivity(), R.layout.adapter_friend, null,
                from, to, 0, null);
        mAdapter.setOnItemCheckChangeListener(this);
        mListView.setAdapter(mAdapter);
    }

    private void setListeners() {
        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mSearchField.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mSearchField.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                mSearchField.afterTextChanged(s);
                String username = mSearchField.getText().toString().trim();

                // At least 3 characters to retrieve users
                if (username.length() > 3) {
                    RetrieveFriendsAsyncTask task = new RetrieveFriendsAsyncTask();
                    task.execute(username);
                }
            }
        });

        mListView.setItemsCanFocus(false);
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UIUtility.hideKeyboard(getContext(), v);
                return false;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.setChecked(!checkBox.isChecked());
            }
        });
    }

    @Override
    public void onItemCheckChangeListener(int position) {
        Friend f = mAdapter.getItem(position);

        View view = mListView.getChildAt(position);
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);

        if (checkBox.isChecked()) {
            //TODO: Add to Friend Request Provider and then sync
            addFriendRequest(f);
        } else {
            //TODO: ARemove Friend Request Provider and then sync
            removeFriendRequest(f);
        }
    }


    @Override
    public void setSelectedFriends(List<Friend> friends) {
        // do nothing
    }

    public class RetrieveFriendsAsyncTask extends AsyncTask<String, Void, MatrixCursor> {


        public RetrieveFriendsAsyncTask() {
        }

        @Override
        protected MatrixCursor doInBackground(String... params) {
            // TODO: get users from username

            String usernameToFind = params[0];

            List<Friend> friends = new ArrayList<>();

            friends.add(new Friend("299", "Miallo1", "miallo@test.fr"));
            friends.add(new Friend("301", "Miallo2", "miallo@test.fr"));
            friends.add(new Friend("302", "Miallo3", "miallo@test.fr"));
            friends.add(new Friend("303", "Miallo4", "miallo@test.fr"));
            friends.add(new Friend("304", "Miallo5", "miallo@test.fr"));

            String[] columns = new String[]{
                    FriendTable.Columns._ID,
                    FriendTable.Columns.FRIEND_ID,
                    FriendTable.Columns.FRIEND_USERNAME,
                    FriendTable.Columns.FRIEND_EMAIL
            };

            MatrixCursor matrixCursor = new MatrixCursor(columns);

            for (Friend f : friends) {
                matrixCursor.addRow(new Object[]{null, f.getId(), f.getUsername(), f.getEmail()});
            }

            return matrixCursor;
        }

        @Override
        protected void onPostExecute(MatrixCursor matrixCursor) {
            super.onPostExecute(matrixCursor);
            mAdapter.swapCursor(matrixCursor);
        }
    }

    private void addFriendRequest(Friend friend) {
        ContentValues mNewValues = new ContentValues();

        String currentUserId = SettingsUtility.get(getContext(), LoginActivity.KEY_USER_ID, null);

        if (currentUserId != null) {
            mNewValues.put(FriendRequestTable.Columns.FRIEND_REQUEST_ID_ASKER, currentUserId);
            mNewValues.put(FriendRequestTable.Columns.FRIEND_REQUEST_ID_NEW_FRIEND, friend.getId());
            mNewValues.put(FriendRequestTable.Columns.FRIEND_REQUEST_STATE, FriendRequest.STATE.SENT);

            getActivity().getContentResolver().insert(
                    FriendRequestContentProvider.CONTENT_URI,
                    mNewValues
            );
        }
    }

    private void removeFriendRequest(Friend f) {
        String currentUserId = SettingsUtility.get(getContext(), LoginActivity.KEY_USER_ID, null);

        getActivity().getContentResolver().delete(
                FriendRequestContentProvider.CONTENT_URI,
                FriendRequestTable.Columns.FRIEND_REQUEST_ID_ASKER + " LIKE ? AND " +
                        FriendRequestTable.Columns.FRIEND_REQUEST_ID_NEW_FRIEND + " LIKE ? AND " +
                        FriendRequestTable.Columns.FRIEND_REQUEST_STATE + " LIKE ?",
                new String[]{
                        currentUserId,
                        f.getId(),
                        Integer.toString(FriendRequest.STATE.SENT)
                }
        );
    }

}

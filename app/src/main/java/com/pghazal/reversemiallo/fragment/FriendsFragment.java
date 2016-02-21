package com.pghazal.reversemiallo.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.adapter.FriendCursorAdapter;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.entity.Friend;
import com.pghazal.reversemiallo.provider.FriendContentProvider;

import java.util.HashMap;

/**
 * A simple {@link ListFragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener,
        FriendCursorAdapter.OnItemCheckChangeListener {

    private static final String TAG = "FriendsFragment";

    private ListView mListView;
    private FriendCursorAdapter mAdapter;
    private SwipeRefreshLayout mSwipeContainer;

    public FriendsFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FriendsFragment.
     */
    public static FriendsFragment newInstance() {
        FriendsFragment fragment = new FriendsFragment();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        mListView = (ListView) view.findViewById(android.R.id.list);

        mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        mSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeContainer.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "# onActivityCreated");

        mListView.setItemsCanFocus(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.setChecked(!checkBox.isChecked());
            }

        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                checkBox.setChecked(!checkBox.isChecked());
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "TODO: Delete item", Toast.LENGTH_SHORT).show();
                
//                getActivity().getContentResolver().delete(
//                        FriendContentProvider.CONTENT_URI,
//                        FriendTable.FriendColumn._ID, new String[]{String.valueOf(id)}
//                );

                return false;
            }
        });

        HashMap<String, Integer> mapSelectedFriends = null;

        if (savedInstanceState != null)
            mapSelectedFriends =
                    (HashMap<String, Integer>) savedInstanceState.getSerializable("mapSelectedFriends");

        loadDatas(mapSelectedFriends);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "# onSaveInstanceState");

        HashMap<String, Integer> mapSelectedFriends = mAdapter.getMapSelectedFriends();

        if (mapSelectedFriends != null) {
            outState.putSerializable("mapSelectedFriends", mapSelectedFriends);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "# onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "# onDetach");
    }

    private void addTest() {
        Uri mNewUri;

        ContentValues mNewValues = new ContentValues();

        mNewValues.put(FriendTable.FriendColumn.FRIEND_ID, "1");
        mNewValues.put(FriendTable.FriendColumn.FRIEND_EMAIL, "test@test.fr");
        mNewValues.put(FriendTable.FriendColumn.FRIEND_USERNAME, "jesuisletest");

        mNewUri = getContext().getContentResolver().insert(
                FriendContentProvider.CONTENT_URI,
                mNewValues
        );
    }

    private void deleteAll() {
        Log.d(TAG, "# deleteAll");
        getActivity().getContentResolver().delete(
                FriendContentProvider.CONTENT_URI,
                "1", null
        );
    }

    private void loadDatas(HashMap<String, Integer> mapSelectedFriends) {
        Log.d(TAG, "# loadDatas");
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{
                FriendTable.FriendColumn.FRIEND_USERNAME,
                FriendTable.FriendColumn.FRIEND_EMAIL
        };

        // Fields on the UI to which we map
        int[] to = new int[]{R.id.usernameText, R.id.emailText};

        mAdapter = new FriendCursorAdapter(getContext(), R.layout.adapter_friend, null,
                from, to, 0, mapSelectedFriends);
        mAdapter.setOnItemCheckChangeListener(this);
        mListView.setAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "# onCreateLoader");

        String[] projection = {
                FriendTable.FriendColumn._ID,
                FriendTable.FriendColumn.FRIEND_ID,
                FriendTable.FriendColumn.FRIEND_USERNAME,
                FriendTable.FriendColumn.FRIEND_EMAIL
        };

        CursorLoader cursorLoader = new CursorLoader(getContext(),
                FriendContentProvider.CONTENT_URI, projection, null, null, null);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "# onLoadFinished");
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO : Add network get request with Volley
                addTest();

                String[] projection = {
                        FriendTable.FriendColumn._ID,
                        FriendTable.FriendColumn.FRIEND_ID,
                        FriendTable.FriendColumn.FRIEND_USERNAME,
                        FriendTable.FriendColumn.FRIEND_EMAIL
                };

                Cursor cursor = getContext().getContentResolver().query(
                        FriendContentProvider.CONTENT_URI,
                        projection, null, null, null
                );

                mAdapter.swapCursor(cursor);

                mSwipeContainer.setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onItemCheckChangeListener() {
        Cursor c = mAdapter.getSelectedFriends();

        if (c != null) {
            while (c.moveToNext()) {

                Friend friend = new Friend();
                friend.setId(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_ID)));
                friend.setUsername(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_USERNAME)));
                friend.setEmail(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_EMAIL)));

                Log.d(TAG, "" + friend.toString());
            }

            c.close();
        }
    }
}

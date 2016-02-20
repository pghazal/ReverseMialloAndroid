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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.provider.FriendContentProvider;

/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FriendsFragment";
    private OnFragmentInteractionListener mListener;

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;
    private SwipeRefreshLayout mSwipeContainer;

    // TODO: delete when not needed anymore
//    private void fillFriendListTest() {
//        List<Friend> friendList = new ArrayList<>();
//        friendList.add(new Friend("1", "Miallo", "miallo@test.com"));
//        friendList.add(new Friend("2", "Azerty", "azerty@test.com"));
//        friendList.add(new Friend("3", "Foo", "foo@test.com"));
//
//        mAdapter.updateAdapterData(friendList);
//    }

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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);

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

        mListView = getListView();

        loadDatas();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "# onAttach");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "# onDetach");
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_fragment_friends, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_friend:
                if (mListener != null)
                    mListener.onAddFriendInteraction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface OnFragmentInteractionListener {
        void onAddFriendInteraction();
    }

    private void addTest() {
        Uri mNewUri;

        ContentValues mNewValues = new ContentValues();

        mNewValues.put(FriendTable.FriendColumn.FRIEND_ID, "1");
        mNewValues.put(FriendTable.FriendColumn.FRIEND_EMAIL, "test@test.fr");
        mNewValues.put(FriendTable.FriendColumn.FRIEND_USERNAME, "jesuisletest");

        mNewUri = getActivity().getContentResolver().insert(
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

    private void loadDatas() {
        Log.d(TAG, "# loadDatas");
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{
                FriendTable.FriendColumn.FRIEND_USERNAME,
                FriendTable.FriendColumn.FRIEND_EMAIL
        };

        // Fields on the UI to which we map
        int[] to = new int[]{R.id.usernameText, R.id.emailText};

        mAdapter = new SimpleCursorAdapter(getActivity(), R.layout.adapter_friend, null,
                from, to, 0);
        setListAdapter(mAdapter);

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

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
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
        // data is not available anymore, delete reference
        mAdapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        // Your code to refresh the list here.
        // Make sure you call mSwipeContainer.setRefreshing(false)
        // once the network request has completed successfully.

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

                Cursor cursor = getActivity().getContentResolver().query(
                        FriendContentProvider.CONTENT_URI,
                        projection, null, null, null
                );

                mAdapter.swapCursor(cursor);

                mSwipeContainer.setRefreshing(false);
            }
        }, 2000);
    }
}

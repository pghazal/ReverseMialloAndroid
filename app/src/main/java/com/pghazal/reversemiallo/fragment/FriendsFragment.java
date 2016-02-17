package com.pghazal.reversemiallo.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.adapter.FriendAdapter;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.entity.Friend;
import com.pghazal.reversemiallo.provider.FriendContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "FriendsFragment";
    private OnFragmentInteractionListener mListener;

    private ListView mListView;
    private SimpleCursorAdapter mAdapter;

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

        if (getArguments() != null) {
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "# onActivityCreated");

        mListView = getListView();
        //mAdapter = new FriendAdapter(getActivity(), R.layout.adapter_friend);
        //mListView.setAdapter(mAdapter);

        //fillFriendListTest();
        addTest();
        fillData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

        mNewUri = getContext().getContentResolver().insert(
                FriendContentProvider.CONTENT_URI,
                mNewValues
        );
    }

    private void fillData() {
        Log.d(TAG, "# fillData");
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{
                FriendTable.FriendColumn.FRIEND_USERNAME,
                FriendTable.FriendColumn.FRIEND_EMAIL
        };

        // Fields on the UI to which we map
        int[] to = new int[]{R.id.usernameText, R.id.emailText};



        getLoaderManager().initLoader(0, null, this);
        mAdapter = new SimpleCursorAdapter(getContext(), R.layout.adapter_friend, null,
                from, to, 0);

        setListAdapter(mAdapter);
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
        // data is not available anymore, delete reference
        mAdapter.swapCursor(null);
    }
}

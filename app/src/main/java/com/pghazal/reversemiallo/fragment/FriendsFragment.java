package com.pghazal.reversemiallo.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.activity.MainActivity;
import com.pghazal.reversemiallo.adapter.FriendCursorAdapter;
import com.pghazal.reversemiallo.animation.ResizeAnimation;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.entity.Friend;
import com.pghazal.reversemiallo.provider.FriendContentProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendsFragment extends ListFragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        SwipeRefreshLayout.OnRefreshListener,
        FriendCursorAdapter.OnItemCheckChangeListener,
        View.OnClickListener {

    public interface OnActionButtonClickListener {
        public void onActionButtonClick(List<Friend> friends);
    }

    private static final String TAG = "FriendsFragment";

    private ListView mListView;
    private FriendCursorAdapter mAdapter;
    private SwipeRefreshLayout mSwipeContainer;
    private LinearLayout actionButton;

    private OnActionButtonClickListener mListener;
    private List<Friend> selectedFriends;

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

//        addTest("1");
//        addTest("2");
//        addTest("3");
//        addTest("4");
//        addTest("5");
//        addTest("6");
//        addTest("7");
//        addTest("8");
//        addTest("9");
//        addTest("10");
//        addTest("11");
//        addTest("12");

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
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                }

                Friend f = (Friend) mListView.getItemAtPosition(position);

                getActivity().getContentResolver().delete(
                        FriendContentProvider.CONTENT_URI,
                        FriendTable.FriendColumn.FRIEND_ID + " LIKE ?", new String[]{f.getId()}
                );

                return true;
            }
        });

        actionButton = (LinearLayout) getActivity().findViewById(R.id.actionButton);
        actionButton.setOnClickListener(this);
        mListener = (MainActivity) getActivity();

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

    private void addTest(String id) {
        Uri mNewUri;

        ContentValues mNewValues = new ContentValues();

        mNewValues.put(FriendTable.FriendColumn.FRIEND_ID, id);
        mNewValues.put(FriendTable.FriendColumn.FRIEND_EMAIL, id + "@test.fr");
        mNewValues.put(FriendTable.FriendColumn.FRIEND_USERNAME, "jesuisletest # " + id);

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

        // Cache la vue si jamais il n'y a plus d'item
        if (data != null && data.getCount() < 1) {
            hideActionButton();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void showActionButton() {
        ResizeAnimation ra = new ResizeAnimation(
                mSwipeContainer,
                mSwipeContainer.getWidth(),
                mSwipeContainer.getHeight(),
                mSwipeContainer.getWidth(),
                mSwipeContainer.getHeight() - actionButton.getHeight());
        mSwipeContainer.startAnimation(ra);

        actionButton.setAlpha(0.0f);
        actionButton.animate()
                .setDuration(500)
                .alpha(1.0f)
                .translationY(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        actionButton.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void hideActionButton() {
        ResizeAnimation ra = new ResizeAnimation(
                mSwipeContainer,
                mSwipeContainer.getWidth(),
                mSwipeContainer.getHeight(),
                mSwipeContainer.getWidth(),
                mSwipeContainer.getHeight() + actionButton.getHeight());
        mSwipeContainer.startAnimation(ra);

        actionButton.setAlpha(1.0f);
        actionButton.animate()
                .setDuration(500)
                .alpha(0.0f)
                .translationY(actionButton.getHeight())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        actionButton.setVisibility(View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO : Add network get request with Volley
                //addTest();

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
    public void onItemCheckChangeListener(int position) {
        int countSelected = mAdapter.getItemSelectedCount();

        if (countSelected > 0 && actionButton.getVisibility() == View.INVISIBLE) {
            showActionButton();
        } else if (countSelected <= 0 && actionButton.getVisibility() == View.VISIBLE) {
            hideActionButton();
        }
    }

    @Override
    public void setSelectedFriends(List<Friend> friends) {
        if (selectedFriends == null) {
            selectedFriends = new ArrayList<>();
        }

        selectedFriends.clear();
        selectedFriends.addAll(friends);

        mListener.onActionButtonClick(selectedFriends);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionButton:
                FriendCursorAdapter.GetSelectedFriendsAsyncTask task =
                        mAdapter.new GetSelectedFriendsAsyncTask();
                task.execute();
                break;
            default:
                break;
        }
    }
}

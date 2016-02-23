package com.pghazal.reversemiallo.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.fragment.AddFriendFragment;

public class AddFriendActivity extends AppCompatActivity {

    private static final String TAG_ADD_FRIENDS_FRAGMENT = "TAG_ADD_FRIENDS_FRAGMENT";

    private AddFriendFragment mAddFriendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentManager fm = getSupportFragmentManager();

        mAddFriendFragment = (AddFriendFragment)
                fm.findFragmentByTag(TAG_ADD_FRIENDS_FRAGMENT);

        if (mAddFriendFragment == null) {
            mAddFriendFragment = AddFriendFragment.newInstance();
            fm.beginTransaction().
                    add(R.id.fragment_container, mAddFriendFragment, TAG_ADD_FRIENDS_FRAGMENT).
                    commit();
        }
    }

}

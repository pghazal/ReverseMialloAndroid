package com.pghazal.reversemiallo.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.adapter.SimpleFragmentPagerAdapter;
import com.pghazal.reversemiallo.utility.SessionManager;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private static final String TAB_POSITION = "TAB_POSITION";

    private static final int REQUEST_CODE_LOGIN = 1;
    private static final int REQUEST_CODE_ADD_FRIEND = 2;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "# onCreate");

        if (!SessionManager.isLoggedIn(this)) {
            showLoginActivity();
        } else {
            // We're already logged in, stay here and setup views
            setupActionBar();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "# onSaveInstanceState");

        if (tabLayout != null)
            outState.putInt(TAB_POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "# onRestoreInstanceState");

        if (viewPager != null)
            viewPager.setCurrentItem(savedInstanceState.getInt(TAB_POSITION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "# onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "# onPause");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "# onActivityResult");

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LOGIN:
                    setupActionBar();
                    break;

                case REQUEST_CODE_ADD_FRIEND:
                    break;
            }
        }
    }

    private void setupActionBar() {
        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.ab_custom_main, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);
        actionBar.setCustomView(v);

        v.findViewById(R.id.settingsButton).setOnClickListener(this);
        v.findViewById(R.id.addFriendButton).setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void showLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    private void showAddFriendActivity() {
        Intent intent = new Intent(this, AddFriendActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_FRIEND);
    }

    private void showSettingsctivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settingsButton:
                showSettingsctivity();
                break;
            case R.id.addFriendButton:
                showAddFriendActivity();
                break;
            default:
                break;
        }
    }
}

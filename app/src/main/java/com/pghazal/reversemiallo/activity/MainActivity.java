package com.pghazal.reversemiallo.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.adapter.SimpleFragmentPagerAdapter;
import com.pghazal.reversemiallo.entity.Friend;
import com.pghazal.reversemiallo.fragment.FriendsFragment;
import com.pghazal.reversemiallo.fragment.SettingsFragment;
import com.pghazal.reversemiallo.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        FriendsFragment.OnFragmentInteractionListener,
        SettingsFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";
    private static final String TAB_POSITION = "TAB_POSITION";
    private static final int REQUEST_CODE_LOGIN = 1;

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
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_logout) {
            logout();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new SimpleFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void logout() {
        if (SessionManager.isLoggedIn(this)) {
            SessionManager.setLoggedIn(this, false);

            showLoginActivity();
        }
    }

    private void showLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN);
    }

    @Override
    public void onFragmentInteraction() {
        Log.d(TAG, "# onFragmentInteraction");
    }
}

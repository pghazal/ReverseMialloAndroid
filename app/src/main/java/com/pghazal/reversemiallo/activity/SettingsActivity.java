package com.pghazal.reversemiallo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.utility.SessionManager;
import com.pghazal.reversemiallo.utility.SettingsUtility;

public class SettingsActivity extends AppCompatActivity  implements View.OnClickListener{

    private Animation mShakeAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.logoutButton).setOnClickListener(this);
        findViewById(R.id.accountButton).setOnClickListener(this);

        mShakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake);
    }

    private void logout() {
        if (SessionManager.isLoggedIn(this)) {
            SessionManager.setLoggedIn(this, false);
            SettingsUtility.set(this, LoginActivity.KEY_USER_ID, null);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logoutButton:
                logout();
                break;

            case R.id.accountButton:
                v.startAnimation(mShakeAnim);
                break;

            default:
                break;
        }
    }
}

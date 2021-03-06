package com.pghazal.reversemiallo.activity;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;

import com.pghazal.reversemiallo.utility.PermissionChecker;
import com.pghazal.reversemiallo.utility.SettingsUtility;

/**
 * Created by Pierre Ghazal on 15/02/2016.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PermissionChecker.REQUEST_PERMISSION_INTERNET: {
                if (isGranted(grantResults))
                    PermissionChecker.setPermissionGranted(getApplicationContext(), PermissionChecker.REQUEST_PERMISSION_INTERNET, true);
                else
                    PermissionChecker.setPermissionGranted(getApplicationContext(), PermissionChecker.REQUEST_PERMISSION_INTERNET, false);
                return;
            }
        }
    }

    private boolean isGranted(int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }
}

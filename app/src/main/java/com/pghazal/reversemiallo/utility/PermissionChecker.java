package com.pghazal.reversemiallo.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by Pierre Ghazal on 15/02/2016.
 */
public class PermissionChecker {

    private static final String KEY_PERMISSION_INTERNET = "KEY_PERMISSION_INTERNET";
    public static final int REQUEST_PERMISSION_INTERNET = 1;

    public static boolean useRuntimePermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static void checkAndAskInternetPermission(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.INTERNET);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.INTERNET},
                    REQUEST_PERMISSION_INTERNET);
        } else {

        }
    }

    public static void setPermissionGranted(Context context, int requestCode, boolean isGranted) {
        switch (requestCode) {
            case REQUEST_PERMISSION_INTERNET:
                SettingsUtility.set(context, KEY_PERMISSION_INTERNET, isGranted);
                return;

            default:
                return;
        }
    }

}

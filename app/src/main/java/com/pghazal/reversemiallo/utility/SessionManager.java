package com.pghazal.reversemiallo.utility;

import android.content.Context;

public class SessionManager {

    private static final String TAG = "SessionManager";

    private static final String KEY_IS_LOGGED_IN = "KEY_IS_LOGGED_IN";

    public static boolean isLoggedIn(Context context) {
        boolean isLoggedIn = false;

        if(SettingsUtility.contains(context, KEY_IS_LOGGED_IN)) {
            if(SettingsUtility.get(context, KEY_IS_LOGGED_IN, false)) {
                isLoggedIn = true;
            }
        }

        return isLoggedIn;
    }

    public static void setLoggedIn(Context context, boolean loggedIn) {
        SettingsUtility.set(context, KEY_IS_LOGGED_IN, loggedIn);
    }
}

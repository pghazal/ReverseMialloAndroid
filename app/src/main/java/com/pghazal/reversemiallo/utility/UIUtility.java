package com.pghazal.reversemiallo.utility;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Pierre Ghazal on 23/02/2016.
 */
public class UIUtility {

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManger = (InputMethodManager) context.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        inputMethodManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

package com.pghazal.reversemiallo.utility;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Pierre Ghazal on 23/02/2016.
 */
public class CameraUtility {

    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    public static int getOrientation() {
        //check if emulator is running
        if (Build.BRAND.toLowerCase().contains("generic")) {
            return 0;
        } else {
            return 90;
        }
    }
}

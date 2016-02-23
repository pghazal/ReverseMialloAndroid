package com.pghazal.reversemiallo.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pghazal.reversemiallo.R;
import com.pghazal.reversemiallo.entity.Friend;
import com.pghazal.reversemiallo.media.CameraPreviewOld;
import com.pghazal.reversemiallo.utility.CameraUtility;

import java.util.ArrayList;

public class CameraActivity extends Activity {

    private static final String TAG = "CameraActivity";

    private Camera mCamera;
    private CameraPreviewOld mPreview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        if (CameraUtility.checkCameraHardware(this)) {
            mCamera = CameraUtility.getCameraInstance();

            if (mCamera != null) {
                // Create our Preview view and set it as the content of our activity.
                mPreview = new CameraPreviewOld(this, mCamera);
                FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
                preview.addView(mPreview);
            }
        } else {
            Toast.makeText(this, "Camera not supported", Toast.LENGTH_SHORT).show();
        }

        ArrayList<Friend> friends = getIntent().getExtras().getParcelableArrayList("friends");

        Log.d(TAG, friends.toString());
    }
}
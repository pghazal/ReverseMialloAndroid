package com.pghazal.reversemiallo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pghazal.reversemiallo.database.table.FriendRequestTable;
import com.pghazal.reversemiallo.database.table.FriendTable;

public class MialloDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "MialloDatabaseHelper";

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "miallo.sqlite";

    public MialloDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");

        FriendTable.onCreate(db);
        FriendRequestTable.onCreate(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade");
        FriendTable.onUpgrade(db, oldVersion, newVersion);
        FriendRequestTable.onUpgrade(db, oldVersion, newVersion);
    }
}

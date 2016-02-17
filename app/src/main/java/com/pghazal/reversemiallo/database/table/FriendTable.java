package com.pghazal.reversemiallo.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public abstract class FriendTable {

    public static final String TABLE_NAME = "friends";

    public static abstract class FriendColumn implements BaseColumns {

        public static final String FRIEND_ID = "id";
        public static final String FRIEND_USERNAME = "username";
        public static final String FRIEND_EMAIL = "email";
    }

    private static abstract class FriendCommand {
        public static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        FriendColumn._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                        FriendColumn.FRIEND_ID + " TEXT" + "," +
                        FriendColumn.FRIEND_USERNAME + " TEXT" + "," +
                        FriendColumn.FRIEND_EMAIL + " TEXT" +
                        " )";

        public static final String TABLE_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(FriendCommand.TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(FriendTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion);
        database.execSQL(FriendCommand.TABLE_DELETE);
        onCreate(database);
    }
}

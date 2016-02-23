package com.pghazal.reversemiallo.database.table;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by Pierre Ghazal on 23/02/2016.
 */
public class FriendRequestTable {

    public static final String TABLE_NAME = "friend_requests";

    public static abstract class Columns implements BaseColumns {

        public static final String FRIEND_REQUEST_ID = "id";
        public static final String FRIEND_REQUEST_ID_ASKER = "id_asker";
        public static final String FRIEND_REQUEST_ID_NEW_FRIEND = "id_new_friend";
        public static final String FRIEND_REQUEST_STATE = "state";
    }

    private static abstract class FriendRequestCommand {
        public static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                        Columns.FRIEND_REQUEST_ID + " TEXT" + "," +
                        Columns.FRIEND_REQUEST_ID_ASKER + " TEXT" + "," +
                        Columns.FRIEND_REQUEST_ID_NEW_FRIEND + " TEXT" + "," +
                        Columns.FRIEND_REQUEST_STATE + " INTEGER" +
                        " )";

        public static final String TABLE_DELETE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(FriendRequestCommand.TABLE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(FriendRequestTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion);
        database.execSQL(FriendRequestCommand.TABLE_DELETE);
        onCreate(database);
    }
}

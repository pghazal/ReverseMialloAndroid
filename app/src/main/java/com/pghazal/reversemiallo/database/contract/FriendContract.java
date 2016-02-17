package com.pghazal.reversemiallo.database.contract;

import android.provider.BaseColumns;

public abstract class FriendContract {

    public FriendContract() {
    }

    public static abstract class FriendEntry implements BaseColumns {
        public static final String TABLE_NAME = "friends";
        public static final String FRIEND_ID = "id";
        public static final String FRIEND_USERNAME = "username";
        public static final String FRIEND_EMAIL = "email";
    }

    public static abstract class FriendCommand {
        public static final String TABLE_CREATE =
                "CREATE TABLE " + FriendEntry.TABLE_NAME + " (" +
                        FriendEntry._ID + " INTEGER PRIMARY KEY"+ "," +
                        FriendEntry.FRIEND_ID + " TEXT" + "," +
                        FriendEntry.FRIEND_USERNAME + " TEXT" + "," +
                        FriendEntry.FRIEND_EMAIL + " TEXT" +
                        " )";

        public static final String TABLE_DELETE =
                "DROP TABLE IF EXISTS " + FriendEntry.TABLE_NAME;

    }
}

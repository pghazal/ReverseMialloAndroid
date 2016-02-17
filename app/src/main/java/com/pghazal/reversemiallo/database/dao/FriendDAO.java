package com.pghazal.reversemiallo.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.pghazal.reversemiallo.database.MialloDatabaseHelper;
import com.pghazal.reversemiallo.database.table.FriendTable;
import com.pghazal.reversemiallo.entity.Friend;

import java.util.ArrayList;
import java.util.List;

public class FriendDAO {

    // TODO: Mettre dans des AsyncTask
    public static long addFriend(Context context, String id, String username, String email) {
        MialloDatabaseHelper mialloDatabaseHelper = new MialloDatabaseHelper(context);

        SQLiteDatabase db = mialloDatabaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FriendTable.FriendColumn.FRIEND_ID, id);
        values.put(FriendTable.FriendColumn.FRIEND_USERNAME, username);
        values.put(FriendTable.FriendColumn.FRIEND_EMAIL, email);

        long newRowId;
        newRowId = db.insert(
                FriendTable.TABLE_NAME,
                null,
                values);

        return newRowId;
    }

    public static int editFriend(Context context, String id, String username, String email) {
        MialloDatabaseHelper mialloDatabaseHelper = new MialloDatabaseHelper(context);

        SQLiteDatabase db = mialloDatabaseHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(FriendTable.FriendColumn.FRIEND_USERNAME, username);
        values.put(FriendTable.FriendColumn.FRIEND_EMAIL, email);

        String selection = FriendTable.FriendColumn.FRIEND_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        int count = db.update(
                FriendTable.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return count;
    }

    public static int deleteFriend(Context context, String id) {
        MialloDatabaseHelper mialloDatabaseHelper = new MialloDatabaseHelper(context);

        SQLiteDatabase db = mialloDatabaseHelper.getWritableDatabase();

        String selection = FriendTable.FriendColumn.FRIEND_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        return db.delete(FriendTable.TABLE_NAME, selection, selectionArgs);
    }

    private static Cursor getAll(Context context) {
        MialloDatabaseHelper mialloDatabaseHelper = new MialloDatabaseHelper(context);

        SQLiteDatabase db = mialloDatabaseHelper.getReadableDatabase();

//        String[] projection = {
//                FriendTable.FriendColumn._ID,
//                FriendTable.FriendColumn.FRIEND_ID,
//                FriendTable.FriendColumn.FRIEND_USERNAME,
//                FriendTable.FriendColumn.FRIEND_EMAIL
//        };
//
        String sortOrder =
                FriendTable.FriendColumn.FRIEND_USERNAME + " DESC";
//
//        Cursor c = db.query(
//                FriendTable.FriendColumn.TABLE_NAME,  // The table to query
//                projection,                               // The columns to return
//                null,                                // The columns for the WHERE clause
//                null,                            // The values for the WHERE clause
//                null,                                     // don't group the rows
//                null,                                     // don't filter by row groups
//                sortOrder                                 // The sort order
//        );

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FriendTable.TABLE_NAME);
        Cursor c = qb.query(db, null, null, null, null, null, sortOrder);

        c.moveToFirst();

        return c;
    }

    private static Cursor getOne(Context context, String id) {
        MialloDatabaseHelper mialloDatabaseHelper = new MialloDatabaseHelper(context);

        SQLiteDatabase db = mialloDatabaseHelper.getReadableDatabase();

        String sortOrder =
                FriendTable.FriendColumn.FRIEND_USERNAME + " DESC";

        String selection = FriendTable.FriendColumn.FRIEND_ID + " LIKE ?";
        String[] selectionArgs = {String.valueOf(id)};

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FriendTable.TABLE_NAME);
        Cursor c = qb.query(db, null, selection, selectionArgs, null, null, sortOrder);

        c.moveToFirst();

        return c;
    }

    public static List<Friend> getFriends(Context context) {

        List<Friend> friends = new ArrayList<Friend>();

        Cursor c = FriendDAO.getAll(context);

        while (c.moveToNext()) {

            Friend friend = new Friend();
            friend.setId(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_ID)));
            friend.setUsername(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_USERNAME)));
            friend.setEmail(c.getString(c.getColumnIndexOrThrow(FriendTable.FriendColumn.FRIEND_EMAIL)));

            friends.add(friend);
        }

        c.close();

        return friends;
    }
}

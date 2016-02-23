package com.pghazal.reversemiallo.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.pghazal.reversemiallo.database.MialloDatabaseHelper;
import com.pghazal.reversemiallo.database.table.FriendRequestTable;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Pierre Ghazal on 17/02/2016.
 */
public class FriendRequestContentProvider extends ContentProvider {
    private static final String TAG = "FRequestContentProvider";

    // used for the UriMacher
    private static final int FRIEND_REQUESTS = 10;
    private static final int FRIEND_REQUESTS_ID = 20;

    private static final String AUTHORITY = "com.pghazal.reversemiallo.provider.friend_requests";
    private static final String BASE_PATH = FriendRequestTable.TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/friend_requests";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/friend_request";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, FRIEND_REQUESTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FRIEND_REQUESTS_ID);
    }

    private MialloDatabaseHelper database;

    public FriendRequestContentProvider() {
        super();
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "# onCreate");
        database = new MialloDatabaseHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        qb.setTables(FriendRequestTable.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case FRIEND_REQUESTS:
                break;
            case FRIEND_REQUESTS_ID:
                // adding the ID to the original query
                qb.appendWhere(FriendRequestTable.Columns._ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);

        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "# insert");

        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = database.getWritableDatabase();

        long id = 0;

        switch (uriType) {
            case FRIEND_REQUESTS:
                id = sqlDB.insert(FriendRequestTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        Uri newUri = Uri.parse(BASE_PATH + "/" + id);

        Log.d(TAG, "# " + newUri);

        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = database.getWritableDatabase();

        int rowsDeleted = 0;

        switch (uriType) {
            case FRIEND_REQUESTS:
                rowsDeleted = sqlDB.delete(FriendRequestTable.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case FRIEND_REQUESTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(FriendRequestTable.TABLE_NAME,
                            FriendRequestTable.Columns._ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(FriendRequestTable.TABLE_NAME,
                            FriendRequestTable.Columns._ID + "=" + id
                                    + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        Log.d(TAG, "# Number of rows deleted : " + rowsDeleted);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = database.getWritableDatabase();

        int rowsUpdated = 0;

        switch (uriType) {
            case FRIEND_REQUESTS:
                rowsUpdated = sqlDB.update(FriendRequestTable.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case FRIEND_REQUESTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(FriendRequestTable.TABLE_NAME,
                            values, FriendRequestTable.Columns._ID + "=" + id, null);
                } else {
                    rowsUpdated = sqlDB.update(FriendRequestTable.TABLE_NAME,
                            values, FriendRequestTable.Columns._ID + "=" + id
                                    + " and "
                                    + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        Log.d(TAG, "# Number of rows updated : " + rowsUpdated);

        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = {
                FriendRequestTable.Columns._ID,
                FriendRequestTable.Columns.FRIEND_REQUEST_ID,
                FriendRequestTable.Columns.FRIEND_REQUEST_ID_ASKER,
                FriendRequestTable.Columns.FRIEND_REQUEST_ID_NEW_FRIEND,
                FriendRequestTable.Columns.FRIEND_REQUEST_STATE,
        };

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }

    public static String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }
}

package com.example.CursorAdapterTest;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-26.
 */
public class MyContentProvider extends ContentProvider {

    private static final String TAG = "PickerContentProvider";

    public static final UriMatcher sUriMatcher;
    public static final int BOOK = 1;
    private static final Object DBLock = new Object();

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(MyDB.AUTHORITY, "book", BOOK);
    }

    private String getMatchTable(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case BOOK:
                return BookTable.TABLE_NAME;
            default:
                throw new IllegalArgumentException("Illegal URI: "+uri);
        }
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case BOOK:
                return BookTable.CONTENT_DIR_TYPE;
            default:
                throw new IllegalArgumentException("Illegal URI : "+uri);
        }
    }





    private static MyDBHelper mDBHelper;

    public SQLiteOpenHelper getDBHelper() {
        if (mDBHelper == null)
            mDBHelper = new MyDBHelper(getContext(), MyDB.DATABASE_NAME, null, MyDB.DATABASE_VERSION);
        return mDBHelper;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = getMatchTable(uri);
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(table);

        // TODO : add the other args.

        SQLiteDatabase db = getDBHelper().getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, null);
        // 这里是关键
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (DBLock) {
            String table = getMatchTable(uri);

            SQLiteDatabase db = getDBHelper().getWritableDatabase();

            long rowID = -1;
            db.beginTransaction();
            try {
                rowID = db.insert(table, null, values);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            if (rowID > 0)
                return ContentUris.withAppendedId(BookTable.CONTENT_URI, rowID);
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            String table = getMatchTable(uri);
            SQLiteDatabase db = getDBHelper().getWritableDatabase();

            int count = 0;
            db.beginTransaction();
            try {
                count = db.delete(table, selection, selectionArgs);
                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        synchronized (DBLock) {
            SQLiteDatabase db = getDBHelper().getWritableDatabase();
            int count;
            String table = getMatchTable(uri);
            db.beginTransaction();
            try {
                count = db.update(table, values, selection, selectionArgs);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            getContext().getContentResolver().notifyChange(uri, null);

            return count;
        }
    }
}

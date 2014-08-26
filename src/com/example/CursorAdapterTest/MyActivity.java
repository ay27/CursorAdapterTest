package com.example.CursorAdapterTest;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class MyActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    SimpleCursorAdapter adapter;
    public static final Uri userUri = UserTable.CONTENT_URI;
    public static final Uri bookUri = BookTable.CONTENT_URI;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 避免在UI线程中加载数据而带来的性能问题，具体表现为：对数据库进行载入、操作时UI会有稍微的卡顿，特别是载入时
        // 使用LoaderManager可以解决
        getLoaderManager().initLoader(0, null, this);

        // Cursor项保持为空，在LoadFinish() 中会把cursor设置正确
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null,
                new String[]{UserTable.KEY_USER_EMAIL}, new int[]{android.R.id.text1}, 0);
        getListView().setAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        getContentResolver().delete(userUri, UserTable.KEY_USER_EMAIL+"=?", new String[]{"me@ay27.pw"});
        getLoaderManager().restartLoader(0, null, this);
    }

    // 为了测试在不同线程中，能否对数据库进行更改
    private class MyThread extends Thread {

        public MyThread() {
        }

        @Override
        public void run() {
            ContentValues newValues = new ContentValues();
            newValues.put(UserTable._ID, 123445);
            newValues.put(UserTable.KEY_USER_ID, 123456);
            newValues.put(UserTable.KEY_USERNAME, "ay27");
            newValues.put(UserTable.KEY_USER_PASSWORD, "____");
            newValues.put(UserTable.KEY_USER_LOGO, "logo logo");
            newValues.put(UserTable.KEY_USER_EMAIL, "me@ay27.pw");
            getContentResolver().insert(UserTable.CONTENT_URI, newValues);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 这两种方法都可以实现，但是效率不知道如何
                    adapter.swapCursor(getContentResolver().query(UserTable.CONTENT_URI, null, null, null, null));
//                    getLoaderManager().restartLoader(0, null, MyActivity.this);
                }
            });

            newValues.clear();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, UserTable.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.changeCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            new MyThread().start();

            ContentValues values = new ContentValues();
            values.put(BookTable.KEY_BOOK_ID, 12344);
            values.put(BookTable.KEY_BOOK_ISBN, "1234567890123");
            values.put(BookTable.KEY_BOOK_NAME, "book name");
            Uri result = getContentResolver().insert(bookUri, values);
            Log.i("MainActivity", result.toString());

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

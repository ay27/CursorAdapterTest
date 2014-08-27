package com.example.CursorAdapterTest;

import android.app.Activity;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;

public class MyActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {
    CursorAdapter adapter;
    public static final Uri bookUri = BookTable.CONTENT_URI;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView = (ListView)findViewById(R.id.listView);

        // 避免在UI线程中加载数据而带来的性能问题，具体表现为：对数据库进行载入、操作时UI会有稍微的卡顿，特别是载入时
        // 使用LoaderManager可以解决
        getLoaderManager().initLoader(0, null, this);

        // Cursor项保持为空，在LoadFinish() 中会把cursor设置正确
        adapter = new MyCursorAdapter(this, null, false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        getContentResolver().delete(bookUri, BookTable.KEY_BOOK_NAME+"=?", new String[]{"book_name"});
        ViewHolder holder = (ViewHolder) view.getTag();
        getContentResolver().delete(bookUri, BookTable._ID+"=?", new String[]{holder.id});
//        getLoaderManager().restartLoader(0, null, this);
    }

    // 为了测试在不同线程中，能否对数据库进行更改
    private class MyThread extends Thread {

        public MyThread() {
        }

        @Override
        public void run() {
            ContentValues newValues = new ContentValues();
            newValues.put(BookTable.KEY_BOOK_ISBN, 12345678);
            newValues.put(BookTable.KEY_BOOK_NAME, "book_name");
            getContentResolver().insert(bookUri, newValues);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    // 这两种方法都可以实现，但是效率不知道如何
//                    adapter.swapCursor(getContentResolver().query(bookUri, null, null, null, null));
////                    getLoaderManager().restartLoader(0, null, MyActivity.this);
//                }
//            });

            newValues.clear();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, bookUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
//        adapter.notifyDataSetChanged();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

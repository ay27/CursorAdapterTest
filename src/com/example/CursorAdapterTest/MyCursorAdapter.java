package com.example.CursorAdapterTest;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-26.
 */
public class MyCursorAdapter extends CursorAdapter {
    public MyCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public MyCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View root = LayoutInflater.from(context).inflate(R.layout.list_item, null);
        ViewHolder holder = new ViewHolder();
        holder.bookID = (TextView) root.findViewById(R.id.book_id);
        holder.bookISBN = (TextView) root.findViewById(R.id.book_isbn);
        holder.bookName = (TextView) root.findViewById(R.id.book_name);

        root.setTag(holder);

        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.bookName.setText(cursor.getString(cursor.getColumnIndex(BookTable.KEY_BOOK_NAME)));
        holder.bookISBN.setText("isbn="+cursor.getString(cursor.getColumnIndex(BookTable.KEY_BOOK_ISBN)));
        holder.bookID.setText("id="+cursor.getString(cursor.getColumnIndex(BookTable._ID)));
        holder.id = cursor.getString(cursor.getColumnIndex(BookTable._ID));
    }
}

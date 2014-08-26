package com.example.CursorAdapterTest;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-26.
 */
public class BookTable implements BaseColumns {

    public static final String TABLE_NAME = "book";
    public static final String PATH = "/book";
    public static final Uri CONTENT_URI = Uri.parse(MyDB.SCHEME+ MyDB.AUTHORITY+PATH);
    public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.ay27.picker";

    public static final String KEY_BOOK_ISBN = "isbn";
    public static final String KEY_BOOK_NAME = "name";

    public static final String SQL_CREATE_TABLE = "create table "+TABLE_NAME+" ("+_ID+" integer primary key, "
            +KEY_BOOK_ISBN+" VARCHAR(13), "
            +KEY_BOOK_NAME+" VARCHAR(100));";

}

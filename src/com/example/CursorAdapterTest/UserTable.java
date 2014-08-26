package com.example.CursorAdapterTest;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Proudly to use Intellij IDEA.
 * Created by ay27 on 14-8-26.
 */
public class UserTable implements BaseColumns {

    public static final String TABLE_NAME = "user";
    public static final String PATH = "/user";
    public static final Uri CONTENT_URI = Uri.parse(PickerDB.SCHEME+PickerDB.AUTHORITY+PATH);
    public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.ay27.picker";

    public static final String KEY_USER_ID = "id";
    public static final String KEY_USERNAME = "name";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_LOGO = "logo";


    public static final String SQL_CREATE_TABLE = "create table "+ TABLE_NAME+" ("+_ID+" integer primary key, "
            + KEY_USER_ID+" integer not null, "
            + KEY_USERNAME+" varchar(50), "
            + KEY_USER_PASSWORD+" varchar(20), "
            +KEY_USER_LOGO+" varchar(100), "
            +KEY_USER_EMAIL+" varchar(100));";

}

package com.example.fragment_cper;

import android.net.Uri;

/**
 * Created by Administrator on 2017/7/24.
 */

public class Word_Dao {
    public static final String AUTHORITY = "com.example.fragment";//URI授权者

    public static final String TABLE_NAME = "words";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CHINESE = "chinese";
    public static final String COLUMN_EXAMPLE = "example";

    //MIME类型
    public static final String MIME_DIR_PREFIX = "vnd.com.example.fragment.android.cursor.dir";
    public static final String MIME_ITEM_PREFIX = "vnd.com.example.fragment.android.cursor.item";
    public static final String MINE_ITEM = "vnd.com.example.fragment.android.word";
    public static final String MINE_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MINE_ITEM;
    public static final String MINE_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MINE_ITEM;
    public static final String PATH_SINGLE = "word/#";//单条数据的路径
    public static final String PATH_MULTIPLE = "word";//多条数据的路径


    public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);

}

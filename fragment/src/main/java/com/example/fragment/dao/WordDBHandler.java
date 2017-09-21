package com.example.fragment.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class WordDBHandler extends SQLiteOpenHelper {
    private final static String DB_NAME = "wordsdb";
    private final static int DB_VERSION = 1;

    private final static String CREATE_TABEL = "create table "+DBdao.TABLE_NAME+"(" +
            DBdao.COLUMN_ID+"  text  PRIMARY KEY," +
            DBdao.COLUMN_CHINESE+" text,"+
            DBdao.COLUMN_EXAMPLE+" text)";
    private final String DROP_TABEL = "drop tabel if exists"+DBdao.TABLE_NAME;
    public WordDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABEL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABEL);
        onCreate(db);
    }
}

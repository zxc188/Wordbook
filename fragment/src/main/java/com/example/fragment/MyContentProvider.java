package com.example.fragment;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import com.example.fragment.dao.DBdao;
import com.example.fragment.dao.WordDBHandler;

public class MyContentProvider extends ContentProvider {
    private static final int MULTIPLE_WORDS = 1;//
    private static final int SINGLE_WORD = 2;

    private SQLiteDatabase db;
    private WordDBHandler wordDBHandler;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public MyContentProvider() {
        uriMatcher.addURI(DBdao.AUTHORITY, DBdao.PATH_SINGLE, SINGLE_WORD);
        uriMatcher.addURI(DBdao.AUTHORITY, DBdao.PATH_MULTIPLE, MULTIPLE_WORDS);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                count = db.delete(DBdao.TABLE_NAME, selection, selectionArgs);
                break;
            case SINGLE_WORD:
                count = db.delete(DBdao.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
        //通知ContentResolver,数据已经发生改变
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                return DBdao.MINE_TYPE_MULTIPLE;
            case SINGLE_WORD://单条数据记录
                return DBdao.MINE_TYPE_SINGLE;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.insert(DBdao.TABLE_NAME, null, values);
        if (id > 0) {
            //在已有的Uri后面添加id
            Uri newUri = ContentUris.withAppendedId(DBdao.CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        wordDBHandler = new WordDBHandler(getContext());
        db=wordDBHandler.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(DBdao.TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                return db.query(DBdao.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

            case SINGLE_WORD:
                qb.appendWhere(DBdao._ID + "=" + uri.getPathSegments().get(1));
                return qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case MULTIPLE_WORDS:
                count = db.update(DBdao.TABLE_NAME, values, selection, selectionArgs);
                break;
            case SINGLE_WORD:
                String segment = uri.getPathSegments().get(1);
                count = db.update(DBdao.TABLE_NAME, values, DBdao._ID + "=" + segment, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unkonwn Uri:" + uri);
        }

        //通知ContentResolver,数据已经发生改变
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}

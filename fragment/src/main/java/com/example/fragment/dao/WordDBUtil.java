package com.example.fragment.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.fragment.word.words;

import java.util.LinkedList;

/**
 * Created by Administrator on 2017/7/23.
 */

public class WordDBUtil {
    private WordDBHandler db;
    private Context context;
    public WordDBUtil(Context context) {
        this.context = context;
        db = new WordDBHandler(context);
    }

    public void des() {
        db.close();
        db = null;
    }
    /*
    * 向数据库中插入数据，type=1是为sql语句插入，type=2时为api插入
    * */

    public void insert(String id, String chinese, String example, int type) {
        if (search(id).getId() != "") {
            Toast.makeText(context, "这个单词已经有了", Toast.LENGTH_LONG).show();
        } else {
            switch (type) {
                case 1:
                    String sql = "insert into " + DBdao.TABLE_NAME + " values(?,?,?)";
                    SQLiteDatabase sqldb = db.getWritableDatabase();
                    sqldb.execSQL(sql, new String[]{id, chinese, example});
                    break;
                case 2:

                    break;
                default:
                    break;
            }
        }

    }

    /*
    * 得到所有单词列表
    * */
    public LinkedList<words> getall() {
        SQLiteDatabase sqldb = db.getReadableDatabase();
        LinkedList<words> linkedList = new LinkedList<>();
        String[] projection = {
                DBdao.COLUMN_ID,
                DBdao.COLUMN_CHINESE,
                DBdao.COLUMN_EXAMPLE,
        };

        //排序
        String sortOrder =
                DBdao.COLUMN_ID + " ASC";

        Cursor c = sqldb.query(
                DBdao.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        Log.i("ajdifj", c.getColumnCount() + c.getColumnName(0) + c.getColumnName(1) + c.getColumnName(2) + c.getCount() + "");
        while (c.moveToNext()) {
            words dummyItem;
            String id, chinese, example;
            id = c.getString(c.getColumnIndex(DBdao.COLUMN_ID));

            chinese = c.getString(c.getColumnIndex(DBdao.COLUMN_CHINESE));
            example = c.getString(c.getColumnIndex(DBdao.COLUMN_EXAMPLE));
            dummyItem = new words(id, chinese, example);
            linkedList.add(dummyItem);
        }
        return linkedList;
    }

    /*
    * 修改数据库更新单词
    * */
    public void change(String old, String id, String chinese, String example) {
        delete(old);
        insert(id, chinese, example, 1);
    }

    /*
    *删除单词
    * */
    public void delete(String id) {
        String sql = "delete from " + DBdao.TABLE_NAME + " where " + DBdao.COLUMN_ID + "='" + id + "'";
        SQLiteDatabase sqldb = db.getWritableDatabase();
        sqldb.execSQL(sql);
    }

    /*
    * 查找单词
    * */
    public words search(String id) {
        words ws = new words("", "", "");
        String sql = "select * from " + DBdao.TABLE_NAME + " where " + DBdao.COLUMN_ID + "=?";
        SQLiteDatabase sqldb = db.getReadableDatabase();
        /*String[] projection = {
                DBdao.COLUMN_ID,
                DBdao.COLUMN_CHINESE,
                DBdao.COLUMN_EXAMPLE,
        };
        String selection = DBdao.COLUMN_ID+"=?";
        String[] selectionArgs = {id};
        Cursor c = sqldb.query(
                DBdao.TABLE_NAME,                         // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );*/
        Cursor c = sqldb.rawQuery(sql, new String[]{id});
        Log.i("aiosdfj", c.getCount()+"");
        while (c.moveToNext()) {
            String ids, chinese, example;
            ids = c.getString(c.getColumnIndex(DBdao.COLUMN_ID));
            chinese = c.getString(c.getColumnIndex(DBdao.COLUMN_CHINESE));
            example = c.getString(c.getColumnIndex(DBdao.COLUMN_EXAMPLE));
            ws = new words(ids, chinese, example);
        }
        return ws;
    }
}

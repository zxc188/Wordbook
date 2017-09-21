package com.example.fragment_cper;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ContentResolver contentResolver;
    private String TAG = "contentResolver_Frag";
    private Button btall;
    private Button btser;
    private Button btdel;
    private Button btdelall;
    private Button btupd;
    private Button btadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentResolver = this.getContentResolver();
        btadd = (Button) findViewById(R.id.btadd);
        btall = (Button) findViewById(R.id.btall);
        btdel = (Button) findViewById(R.id.btdel);
        btdelall = (Button) findViewById(R.id.btdelall);
        btser = (Button) findViewById(R.id.btser);
        btupd = (Button) findViewById(R.id.btupd);
    }
    public void ONButtonClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btadd:
                String idnew = "banana";
                String chinesenew = "香蕉";
                String examplenew = "This is a banana";
                ContentValues values = new ContentValues();
                values.put(Word_Dao.COLUMN_ID, idnew);
                values.put(Word_Dao.COLUMN_CHINESE, chinesenew);
                values.put(Word_Dao.COLUMN_EXAMPLE, examplenew);
                Uri uri=contentResolver.insert(Word_Dao.CONTENT_URI, values);
                break;
            case R.id.btdelall:
                contentResolver.delete(Word_Dao.CONTENT_URI, null, null);
                break;
            case R.id.btdel:
                String idds = "2";
                Uri uri1 = Uri.parse(Word_Dao.CONTENT_URI_STRING +"/"+ idds);
                String wheres = Word_Dao.COLUMN_ID + "='banana'";
                int result = contentResolver.delete(uri1, wheres, null);
                break;
            case R.id.btupd:
                ContentValues contentValues = new ContentValues();
                String idnews = "banana";
                String chinesenews = "香蕉aa";
                String examplenews = "This is a banana";
                contentValues.put(Word_Dao.COLUMN_ID, idnews);
                contentValues.put(Word_Dao.COLUMN_CHINESE, chinesenews);
                contentValues.put(Word_Dao.COLUMN_EXAMPLE, examplenews);
                String wheress = Word_Dao.COLUMN_ID + "='banana'";
                contentResolver.update(Word_Dao.CONTENT_URI, contentValues, wheress, null);
                break;
            case R.id.btser:
                String wheresss = Word_Dao.COLUMN_ID + "='banana'";
                Cursor cursors=contentResolver.query(Word_Dao.CONTENT_URI,
                        new String[]{Word_Dao.COLUMN_ID, Word_Dao.COLUMN_CHINESE, Word_Dao.COLUMN_EXAMPLE},
                        wheresss, null, null
                );
                if (cursors == null) {
                    Toast.makeText(MainActivity.this,"没有查找到数据",Toast.LENGTH_LONG).show();
                    return;
                }
                while (cursors.moveToNext()) {
                    String message = "";
                    String idget = cursors.getString(cursors.getColumnIndex(Word_Dao.COLUMN_ID));
                    String chineseget=cursors.getString(cursors.getColumnIndex(Word_Dao.COLUMN_CHINESE));
                    String exampleget = cursors.getString(cursors.getColumnIndex(Word_Dao.COLUMN_EXAMPLE));
                    message = message + "id:  " + idget + "\tchinese:  " + chineseget + "\texample:  " + exampleget;
                    Log.i("search", message);
                }
                break;
            case R.id.btall:
                Cursor cursor = contentResolver.query(Word_Dao.CONTENT_URI,
                        new String[]{Word_Dao.COLUMN_ID, Word_Dao.COLUMN_CHINESE, Word_Dao.COLUMN_EXAMPLE},
                        null, null, null);
                if (cursor == null) {
                    Toast.makeText(MainActivity.this,"没有查找到数据",Toast.LENGTH_LONG).show();
                    return;
                }
                while (cursor.moveToNext()) {
                    String message = "";
                    String idget = cursor.getString(cursor.getColumnIndex(Word_Dao.COLUMN_ID));
                    String chineseget=cursor.getString(cursor.getColumnIndex(Word_Dao.COLUMN_CHINESE));
                    String exampleget = cursor.getString(cursor.getColumnIndex(Word_Dao.COLUMN_EXAMPLE));
                    message = message + "id:  " + idget + "\tchinese:  " + chineseget + "\texample:  " + exampleget;
                    Log.i(TAG, message);
                }
                break;
            case R.id.btreadcall:
                Cursor cursorss=contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if (cursorss == null) {
                    Toast.makeText(MainActivity.this,"没有查找到数据",Toast.LENGTH_LONG).show();
                    return;
                }
                while (cursorss.moveToNext()) {
                    String message = "";
                    String idget = cursorss.getString(cursorss.getColumnIndex(ContactsContract.Contacts._ID));
                    String chineseget=cursorss.getString(cursorss.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    message = message + "id:  " + idget + "\tchinese:  " + chineseget ;
                    Log.i(TAG, message);
                }
                break;
            default:
                break;
        }
    }

}

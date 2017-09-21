package com.example.fragment.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.fragment.R;
import com.example.fragment.dao.WordDBUtil;
import com.example.fragment.fragment.BlankFragment;
import com.example.fragment.fragment.ItemFragment;
import com.example.fragment.word.words;

import java.util.LinkedList;


public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {
    private WordDBUtil wordDBUtil;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Log.i("created", "ajdsofijoijo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordDBUtil = new WordDBUtil(this);
        if (!(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {

        }
    }


    @Override
    public void onListFragmentInteraction(words item) {
        if (!(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
            Bundle arguments = new Bundle();
            arguments.putString("id", item.getId());
            arguments.putString("chinese", item.getChinese());
            arguments.putString("example", item.getExample());
            BlankFragment fragment = new BlankFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction().replace(R.id.worddetail, fragment).commit();
        } else {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, Right_Another.class);
            intent.putExtra("english", item.getId());
            intent.putExtra("chinese", item.getChinese());
            intent.putExtra("example", item.getExample());
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mi,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                final View view = getLayoutInflater().inflate(R.layout.add,null);
                AlertDialog.Builder  builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("增加")
                        .setView(view)
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String idnew = ((TextView) view.findViewById(R.id.ed1)).getText().toString();
                                String chinew = ((TextView) view.findViewById(R.id.ed2)).getText().toString();
                                String examnew = ((TextView) view.findViewById(R.id.ed3)).getText().toString();
                                ItemFragment itemFragment =(ItemFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
                                wordDBUtil.insert(idnew, chinew, examnew, 1);
                                LinkedList<words> linkedList = wordDBUtil.getall();
                                ItemFragment.reflash(linkedList);
                            }
                        }).show();
                break;
            case R.id.menu_search:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Search.class);
                startActivity(intent);
                break;
            case R.id.menu_web_search:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, Youdao_Main.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wordDBUtil.des();
    }
}
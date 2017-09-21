package com.example.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.fragment.R;
import com.example.fragment.dao.WordDBUtil;
import com.example.fragment.word.words;



public class Search extends AppCompatActivity {
    private Button bt;
    private TextView input;
    private WordDBUtil wordDBUtil;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sear);
        wordDBUtil = new WordDBUtil(this);
        bt = (Button) findViewById(R.id.button);
        input = (TextView) findViewById(R.id.input);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Search.this, Right_Another.class);
                words wowo = wordDBUtil.search(input.getText().toString());
                intent.putExtra("english", wowo.getId());
                intent.putExtra("chinese", wowo.getChinese());
                intent.putExtra("example", wowo.getExample());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wordDBUtil.des();
    }
}

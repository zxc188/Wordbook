package com.example.fragment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.fragment.R;

public class Right_Another extends AppCompatActivity {
    private TextView texteng;
    private TextView textchin;
    private TextView textexam;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_another);
        texteng = (TextView) findViewById(R.id.texteng);
        textchin = (TextView) findViewById(R.id.textchin);
        textexam = (TextView) findViewById(R.id.textexam);
        intent = getIntent();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                texteng.setText(intent.getStringExtra("english"));
                textchin.setText(intent.getStringExtra("chinese"));
                textexam.setText(intent.getStringExtra("example"));
            }
        });
        thread.start();
    }
}

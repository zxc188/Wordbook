package com.example.fragment.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fragment.R;
import com.example.fragment.dao.WordDBUtil;
import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.app.YouDaoApplication;
import com.youdao.sdk.ydonlinetranslate.TranslateErrorCode;
import com.youdao.sdk.ydonlinetranslate.TranslateListener;
import com.youdao.sdk.ydonlinetranslate.TranslateParameters;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Youdao_Main extends AppCompatActivity {
    private Button bt_search;
    private EditText ed_input;
    private TextView tx_show;
    private Translator translator;
    private ImageView img_del;
    private TextView text_more;
    private WordDBUtil wordDBUtil;
    private Handler handler;
    private String url;
    private String s;
    public static final int UNUSED_REQUEST_CODE = 255;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youdao_main);
        bt_search = (Button) findViewById(R.id.bt_web_search);
        ed_input = (EditText) findViewById(R.id.ed_Search_input);
        tx_show = (TextView) findViewById(R.id.text_show);
        img_del = (ImageView) findViewById(R.id.img_del);
        text_more = (TextView) findViewById(R.id.text_more);
        registerForContextMenu(tx_show);
        tx_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_input.setText(s);
                Youdao_Searh(s);
            }
        });
        text_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent .setData(Uri.parse(url));
                startActivity(intent);
            }
        });
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                tx_show.setText(msg.getData().getString("end"), TextView.BufferType.SPANNABLE);
                getEachWord(tx_show);
                tx_show.setMovementMethod(LinkMovementMethod.getInstance());
            }
        };
        img_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_input.setText("");
            }
        });

        YouDaoApplication.init(Youdao_Main.this, "2fc5a25869761448");
        /*
        * 判断权限
        * */
        if(!isPermissionGranted(this, WRITE_EXTERNAL_STORAGE)){
            String[] perssions = {WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, perssions, UNUSED_REQUEST_CODE);
        }

        Language langFrom = LanguageUtils.getLangByName("中文");
        Language langTo = LanguageUtils.getLangByName("英文");

        TranslateParameters tps = new TranslateParameters.Builder()
                .source("ydtranslate-demo")
                .from(langFrom).to(langTo).build();

        translator = Translator.getInstance(tps);

        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查询，返回两种情况，一种是成功，相关结果存储在result参数中，另外一种是失败，失败信息放在TranslateErrorCode 是一个枚举类，整个查询是异步的，为了简化操作，回调都是在主线程发生。
                Youdao_Searh(ed_input.getText().toString());
            }
        });


    }
    public static boolean isPermissionGranted(final Youdao_Main context,
                                              final String permission) {
        return ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED;
    }
    public void getEachWord(TextView textView){
        Spannable spans = (Spannable)textView.getText();
        Integer[] indices = getIndices(
                textView.getText().toString().trim(), ' ');
        int start = 0;
        int end = 0;
        // to cater last/only word loop will run equal to the length of indices.length
        for (int i = 0; i <= indices.length; i++) {
            ClickableSpan clickSpan = getClickableSpan();
            // to cater last/only word
            end = (i < indices.length ? indices[i] : spans.length());
            spans.setSpan(clickSpan, start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1;
        }
        //改变选中文本的高亮颜色
        textView.setHighlightColor(Color.BLUE);
    }
    private ClickableSpan getClickableSpan(){
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                s = tv
                        .getText()
                        .subSequence(tv.getSelectionStart(),
                                tv.getSelectionEnd()).toString();

                Log.d("tapped on:", s);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
    }

    public static Integer[] getIndices(String s, char c) {
        int pos = s.indexOf(c, 0);
        List<Integer> indices = new ArrayList<Integer>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c, pos + 1);
        }
        return (Integer[]) indices.toArray(new Integer[0]);
    }

    public void Youdao_Searh(final String fr) {
        final String from = fr;
        translator.lookup(from, new TranslateListener() {
            @Override
            public void onResult(Translate result, String input) {//查询成功
                url = result.getDeeplink();
                String to = result.getTranslations().get(0).toString();
                String end = "输入: " + from + "\n\n结果: " + to;
                String Explains = "";
                if (result.getExplains() != null) {
                    for (int i = 0; i < result.getExplains().size(); i++) {
                        Explains = Explains + result.getExplains().get(i).toString() + "\n";
                    }
                    end = end + "\n\n翻译结果:\n" + Explains + "\n\n";
                }
                final String finalEnd = end;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("end", finalEnd);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                });
                thread.start();
            }

            @Override
            public void onError(TranslateErrorCode error) {//查询失败
                Log.v("anudshfoijizxvr", error.toString());
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.poup, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pupo) {
            Youdao_Searh(s);
        }
        return true;
    }
}

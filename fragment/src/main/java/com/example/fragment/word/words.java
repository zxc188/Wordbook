package com.example.fragment.word;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/23.
 */

public class words implements Serializable{
    private String id;
    private String chinese;
    private String  example;

    public words(String id, String chinese, String example) {
        this.id = id;
        this.chinese = chinese;
        this.example = example;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}

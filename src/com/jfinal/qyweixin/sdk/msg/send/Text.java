package com.jfinal.qyweixin.sdk.msg.send;

public class Text {

    public Text() {

    }

    private String content;


    public Text(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

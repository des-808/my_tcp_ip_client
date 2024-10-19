package com.example.des808.my_tcp_ip_client;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class MessageChat {
    private String chatName;
    private String message;
    private String time;
    String pattern = "HH:mm:ss";
    @SuppressLint("SimpleDateFormat")
    private java.text.DateFormat dateFormat = new SimpleDateFormat(pattern);

    public MessageChat( String chatName,String msg) {
        this.chatName = chatName;
        this.message = msg;
        this.time = dateFormat.format(new java.util.Date());
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTime() {
        return time;
    }
}

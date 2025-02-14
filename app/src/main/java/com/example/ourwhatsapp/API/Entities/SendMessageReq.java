package com.example.ourwhatsapp.API.Entities;

public class SendMessageReq {
    private final String msg;

    public SendMessageReq(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}

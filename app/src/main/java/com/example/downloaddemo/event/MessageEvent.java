package com.example.downloaddemo.event;

/**
 * 2021.12.13 lhn
 * 消息事件
 */
public class MessageEvent {

    public final String message;
    public int num;

    public static MessageEvent getInstance(String message, int num) {
        return new MessageEvent(message,num);
    }

    private MessageEvent(String message,int num) {
        this.message = message;
        this.num = num;
    }
}
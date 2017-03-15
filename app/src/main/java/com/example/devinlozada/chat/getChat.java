package com.example.devinlozada.chat;

/**
 * Created by devinlozada on 1/03/17.
 */

public class getChat {
    private String message;
    private String id;

    public getChat() {
    }

    public getChat(String message, String id) {
        this.message = message;
        this.id = id;
    }

    public static String getMessage() {
        return null;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

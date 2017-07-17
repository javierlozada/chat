package com.example.devinlozada.chat;


public class Show_Chat_Conversation_Data_Items {
    private String message;
    private String sender;
    private String name;


    public Show_Chat_Conversation_Data_Items(){

    }

    public Show_Chat_Conversation_Data_Items(String message, String sender,String name) {
        this.message = message;
        this.sender = sender;
        this.name   = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getName(){return name;}

    public void setName(String name){this.name = name;}
}

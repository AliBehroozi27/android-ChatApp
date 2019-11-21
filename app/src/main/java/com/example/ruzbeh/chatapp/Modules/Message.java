package com.example.ruzbeh.chatapp.Modules;

public class Message {
    public String message, sender_id, chat_id;

    public Message(String message , String sender_id  , String chat_id) {
        this.message = message;
        this.sender_id = sender_id;
        this.chat_id = chat_id;
    }
}

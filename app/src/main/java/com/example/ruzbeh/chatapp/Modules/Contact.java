package com.example.ruzbeh.chatapp.Modules;

public class Contact {
    public String username, user_id, password, pic_url , email ,bio;
    public Boolean online;

    public Contact(String username , String user_id  , String email , String pic_url,String bio , Boolean online) {
        this.email = email;
        this.online = online;
        this.password = password;
        this.user_id = user_id;
        this.username = username;
        this.pic_url = pic_url;
        this.bio = bio;
    }
}

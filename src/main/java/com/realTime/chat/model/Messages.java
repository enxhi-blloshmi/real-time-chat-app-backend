package com.realTime.chat.model;

import org.springframework.data.annotation.Id;

public class Messages {

    @Id
    private String id;
    private String userName;
    private String userMessage;
    private String type;
    private byte[] image;

    public Messages(){}

    public Messages(String id, String userName, String userMessage, String type, byte[] image){
        this.id = id;
        this.userName = userName;
        this.userMessage = userMessage;
        this.type = type;
        this.image = image;
    }

    public String getId(){
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName(){
        return this.userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserMessage(){
        return userMessage;
    }

    public void setUserMessage(String userMessage){
        this.userMessage = userMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}

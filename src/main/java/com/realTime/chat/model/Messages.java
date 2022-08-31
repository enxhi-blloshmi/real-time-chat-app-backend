package com.realTime.chat.model;

public class Messages {

    private String userName;
    private String userMessage;

    public Messages(){}

    public Messages(String userName, String userMessage){
        this.userName = userName;
        this.userMessage = userMessage;
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
}

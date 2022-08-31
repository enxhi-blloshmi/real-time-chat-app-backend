package com.realTime.chat.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "rooms")
public class Room {

    @Id
    private String roomId;
    private String roomName;
    private List<Messages> messages;
    private List<Users> users;

    public Room() {
    }

    public Room(String roomId, String roomName, List<Messages> messages, List<Users> users) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.messages = messages;
        this. users = users;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Messages> getMessages() {
        return messages;
    }

    public void setMessages(List<Messages> messages) {
        this.messages = messages;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
}

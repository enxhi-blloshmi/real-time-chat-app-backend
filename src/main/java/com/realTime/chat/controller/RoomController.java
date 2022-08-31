package com.realTime.chat.controller;

import com.realTime.chat.model.Messages;
import com.realTime.chat.model.Room;
import com.realTime.chat.model.Users;
import com.realTime.chat.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private WebSocketController webSocketController;

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getRooms(){
        try{
            List<Room> rooms = roomRepository.findAll();
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/rooms")
    public ResponseEntity<Room> postRoom(@RequestBody Room room){
        try{
            Room newRoom=roomRepository.save(room);

            return new ResponseEntity<>(newRoom, HttpStatus.CREATED);
        }
        catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<Room> getOneRoom(@PathVariable String id){
        try{
            Optional<Room> room = roomRepository.findById(id);
            if(room.isPresent())
                return new ResponseEntity<>(room.get(), HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/rooms/update-messages/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable String id, @RequestBody Messages message) {
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isPresent()) {
                Room _room = room.get();
                List<Messages> messages = new ArrayList<>();
                if(_room.getMessages()!=null){
                   messages = _room.getMessages();
                }
                messages.add(message);
                _room.setMessages(messages);
                roomRepository.save(_room);
                webSocketController.chat(message.getUserMessage());
                return new ResponseEntity<>(_room, HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/rooms/update-users/{id}")
    public ResponseEntity<Room> updateRoomUsers(@PathVariable String id, @RequestBody Users user){
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isPresent()) {
                Room _room = room.get();
                List<Users> users = _room.getUsers();
                users.add(user);
                _room.setUsers(users);
                return new ResponseEntity<>(roomRepository.save(_room), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Room> deleteRoom(@PathVariable String id){
        try{
            Optional<Room> room = roomRepository.findById(id);
            if(room.isPresent()) {
                roomRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping()
    public ResponseEntity<Room> deleteAll(){
        try{
            roomRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

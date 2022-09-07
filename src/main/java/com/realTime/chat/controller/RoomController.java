package com.realTime.chat.controller;

import com.realTime.chat.model.Messages;
import com.realTime.chat.model.Room;
import com.realTime.chat.model.Users;
import com.realTime.chat.repository.RoomRepository;
import com.realTime.chat.service.RoomService;
import com.realTime.chat.util.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin()
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private WebSocketController webSocketController;

    @Autowired
    private RoomService roomService;

    /**
     * Merr te gjitha room-et nga databaza
     * @return Http Response ne varesi nese u realizua veprimi
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getRooms(){
        try{
            List<Room> rooms = roomRepository.findAll();
            for(Room r: rooms){
                r = roomService.getFileMessages(r);
            }
            return new ResponseEntity<>(rooms, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Shton nje room ne databaze
     * @param room room-i qe merret nga trupi i kerkeses HTTP POST dhe do shtohet ne databaze
     * @return Http Response ne varesi nese u realizua veprimi
     */
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

    /**
     * Merr nga databaza nje room me id qe merr si parameter
     * @param id id e room qe do kerkohet tyemerret nga databaza. Vjen si pjese e path-it te kerkeses
     * @return Http Response ne varesi nese u realizua veprimi
     */
    @GetMapping("/rooms/{id}")
    public ResponseEntity<Room> getOneRoom(@PathVariable String id){
        try{
            Optional<Room> room = roomRepository.findById(id);
            if(room.isPresent())
                return new ResponseEntity<>(roomService.getFileMessages(room.get()), HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Perditeson mesazhet ne databaze kur vjen nje mesazh i ri tekst
     * @param id id e room qe do behen update mesazhet. Vjen si pjese e path-it te kerkeses
     * @param message mesazhi qe do ruhet ne databaze
     * @return Http Response ne varesi nese u realizua veprimi
     */
    @PutMapping("/rooms/update-messages/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable String id, @RequestBody Messages message) {
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isPresent()) {
                Room updatedRoom = roomService.setMessages(room.get(), null, message.getUserName(), message.getUserMessage());
                roomRepository.save(updatedRoom);
                webSocketController.chat(message.getUserMessage());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Perditeson mesazhet ne databaze kur dergohet nje mesazh file nga front-i
     * @param id id e room-it qe do perditesohet
     * @param file imazhi si nje multipartfile
     * @param userName userName i user-it qe dergoi mesazhin
     * @return Http Response ne varesi nese u realizua veprimi
     */
    @PutMapping("/rooms/update-messages-files/{id}")
    public ResponseEntity<Room> fileMessages(@PathVariable String id, @RequestParam("image") MultipartFile file, @RequestParam("userName") String userName) {
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isPresent()) {
                Room updatedRoom = roomService.setMessages(room.get(), ImageUtility.compressImage(file.getBytes()), userName, file.getOriginalFilename());
                roomRepository.save(updatedRoom);
                webSocketController.chat("image");
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        catch (Exception e) {

        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Editon nje mesazh aktual e nje roomi-i
     * @param roomId id e room-it ku ndodhet  mesazhi
     * @param messageId id e mesazhit qe do editohet
     * @param message mesazhi qe zevendesoje mesazhin aktual
     * @return Http Response ne varesi nese u realizua veprimi
     */
    @PutMapping("/rooms/edit-message/{roomId}/{messageId}")
    public ResponseEntity<Room> editMessage(@PathVariable("roomId") String roomId, @PathVariable("messageId") String messageId, @RequestBody Messages message){
        try{
            Optional<Room> room = roomRepository.findById(roomId);
            if (room.isPresent()) {
                Room editedRoom = roomService.editMessages(room.get(), messageId, message);
                roomRepository.save(editedRoom);
                webSocketController.chat(message.getUserMessage());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Perditeson user-at e nje roi=nje room-i ne databaze
     * @param id id e room-it ne te cilen do shtohet user-i
     * @param user user-i qe do shtohet
     * @return Http Response ne varesi nese u realizua veprimi
     */
    @PutMapping("/rooms/update-users/{id}")
    public ResponseEntity<Room> updateRoomUsers(@PathVariable String id, @RequestBody Users user){
        try {
            Optional<Room> room = roomRepository.findById(id);
            if (room.isPresent()) {
                Room updatedRoom = roomService.updateUsers(room.get(), user);
                return new ResponseEntity<>(roomRepository.save(updatedRoom), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /**
     * Ben delete nje room
     * @param id id e room-it qe do fshihet
     * @return Http Response ne varesi nese u realizua veprimi
     */
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

    /**
     * Fshin te gjitha room-et
     * @return Http Response ne varesi nese u realizua veprimi
     */
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

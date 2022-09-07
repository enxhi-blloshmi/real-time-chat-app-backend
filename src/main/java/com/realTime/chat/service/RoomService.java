package com.realTime.chat.service;

import com.realTime.chat.model.Messages;
import com.realTime.chat.model.Room;
import com.realTime.chat.model.Users;
import com.realTime.chat.util.ImageUtility;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    /**
     * Perditeson mesazhet e nje room-i ne varesi nese mesazhi permban imazh apo tekst
     *
     * @param room room-i ku do te perditesohen mesazhet
     * @param image imazhi qe do ruhet ne databaze. Eshte null kur mesazhi permban tekst
     * @param userMessage mesazhi i derguar nga user-i. Mban emrin e imazhit ne kur mesazhi eshte imazh
     * @param userName emri i user-it qe dergoi mesazhin
     * @return room-in e perditesuar
     */
    public Room setMessages(Room room, byte[] image, String userName, String userMessage){
        List<Messages> messages = new ArrayList<>();
        if(room.getMessages() != null)
            messages = room.getMessages();

        Messages newMessage = new Messages();
        newMessage.setId(String.valueOf(System.currentTimeMillis()));
        newMessage.setUserName(userName);
        newMessage.setUserMessage(userMessage);
        if(image != null)
            newMessage.setType("file");

         else
            newMessage.setType("text");
         newMessage.setImage(image);

        messages.add(newMessage);
        room.setMessages(messages);
        return room;
    }

    /**
     * Ben decompress mesazhet e roomit qe jane imazhe
     * @param room room-i nga i cili do te merren mesazhet
     * @return room-in me mesazhe imazh te dekrompesuar
     */
    public Room getFileMessages(Room room) {
        List<Messages> messages = new ArrayList<>();
        if(room.getMessages() != null) {
            messages = room.getMessages();
            for(Messages m: messages){
                if(m.getType().equals("file"))
                    m.setImage(ImageUtility.decompressImage(m.getImage()));
            }
            room.setMessages(messages);
        }

        return room;
    }

    /**
     * Ben editimin e nje mesazhi ne nje room
     * @param room room-i ne te cilin ndodhet mesazhi qe do behet edit
     * @param messageId id e mesazhit qe do editohet
     * @param message mesazhi i edituar qe do ruhet ne databaze
     * @return room-in qe do ruhet ne dtabaze me mesazhin e edituar
     */
    public Room editMessages(Room room, String messageId, Messages message){
        List<Messages> messages = new ArrayList<>();
        if(room.getMessages() != null){
            messages = room.getMessages();

            for(Messages m: messages){
                if(m.getId().equals(messageId))
                    m.setUserMessage(message.getUserMessage());
             }
            room.setMessages(messages);
        }

        return room;
    }

    /**
     * Perditeson user-at e nje room-i
     * @param room te cilit do i perditesohen user-at
     * @param user user-i qe do te shtohet
     * @return room-in e perditesuar
     */
    public Room updateUsers(Room room, Users user){
        List<Users> users = new ArrayList<>();
        if(room.getUsers() != null)
            users =room.getUsers();
        users.add(user);
        room.setUsers(users);

        return room;

    }
}

package com.realTime.chat.controller;

import com.realTime.chat.model.Messages;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    /**
     * Dergon mesazh ne socket-in e frontit
     * @param msg mesazhi qe dergon user-i
     * @return
     */
    @MessageMapping("/chat")
    @SendTo("/start/chat")
    public String chat(String msg) {
       System.out.println(msg);
       return msg;

    }
}

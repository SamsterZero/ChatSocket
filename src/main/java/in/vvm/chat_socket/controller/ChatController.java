package in.vvm.chat_socket.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

public class ChatController{

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload String message, SimpMessageHeaderAccessor headerAccessor) {
        // Handle the received message (e.g., broadcast it to other clients)
    }
    
}

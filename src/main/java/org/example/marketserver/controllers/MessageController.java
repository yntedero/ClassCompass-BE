package org.example.marketserver.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.marketserver.dtos.UserDTO;
import org.example.marketserver.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.marketserver.repositories.MessageRepository;
import org.example.marketserver.models.Message;
import org.example.marketserver.dtos.MessageDTO;
import java.util.List;
import java.util.Optional;

import org.example.marketserver.models.User;
@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageRepository messageRepository;
    private final UserService userService;

    @MessageMapping("/chat")
    public void chat(@Payload MessageDTO message) {
        log.info("Message received: {}", message);
        Message messageEntity = new Message();
        messageEntity.setToUser(message.to());
        messageEntity.setMessage(message.message());
        messageEntity.setFromUser(message.from());
        System.out.println("messageEntity" + messageEntity);;
        messageRepository.save(messageEntity);
//        simpMessagingTemplate.convertAndSend("/topic", message);
        System.out.println("message.to()" + message.to());
        simpMessagingTemplate.convertAndSendToUser(message.to(), "/topic", message);
        simpMessagingTemplate.convertAndSendToUser(message.from(), "/topic", message);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Long userId = Long.valueOf(principal.toString());
        Optional<UserDTO> userOptional = userService.getUserById(Long.valueOf(userId.toString()));
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        UserDTO user = userOptional.get();
        String userEmail = user.getEmail();
        List<Message> messages = messageRepository.findAllByUser(userEmail);
        return ResponseEntity.ok(messages);
    }

}
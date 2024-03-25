package org.example.marketserver.controllers;

import org.example.marketserver.dtos.MessageDTO;
import org.example.marketserver.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessage(@RequestBody MessageDTO messageDTO) {
        MessageDTO sentMessage = messageService.sendMessage(messageDTO);
        return ResponseEntity.ok(sentMessage);
    }

    @GetMapping("/{senderId}/{receiverId}")
    public ResponseEntity<List<MessageDTO>> getMessagesBetweenUsers(
            @PathVariable Long senderId, @PathVariable Long receiverId) {
        List<MessageDTO> messages = messageService.getMessagesBetweenUsers(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }
}

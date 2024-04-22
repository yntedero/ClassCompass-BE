package org.example.marketserver.controllers;

import lombok.RequiredArgsConstructor;
import org.example.marketserver.dtos.MessageDTO;
import org.example.marketserver.security.services.AuthenticationService;
import org.example.marketserver.services.MessageService;
import org.example.marketserver.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.send")
    @SendTo("/user/messages")
    public ResponseEntity<?> processMessage(@Payload MessageDTO messageDTO) {
        MessageDTO savedMessage = messageService.sendMessage(messageDTO);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(messageDTO.getReceiverId()), "/queue/messages",
                savedMessage
        );
        return ResponseEntity.ok(savedMessage);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    @PreAuthorize("@messageService.getMessageByRecip(#recipientId) == authentication.principal")
    public ResponseEntity<List<MessageDTO>> findChatMessages(@PathVariable Long senderId,
                                                             @PathVariable Long recipientId) {
        return ResponseEntity.ok(messageService.getMessagesBetweenUsers(senderId, recipientId));
    }

}

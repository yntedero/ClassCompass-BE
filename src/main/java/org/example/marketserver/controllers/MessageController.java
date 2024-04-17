package org.example.marketserver.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.marketserver.dtos.MessageDTO;
import org.example.marketserver.security.core.CustomAuthentication;
import org.example.marketserver.services.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat.send")
    @SendTo("/user/messages")
    public MessageDTO processMessage(@Payload MessageDTO messageDTO) {
        MessageDTO savedMessage = sendMessageWithAuthentication(messageDTO);
        return savedMessage;
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<MessageDTO>> findChatMessages(@PathVariable Long senderId,
                                                             @PathVariable Long recipientId) {
        return ResponseEntity
                .ok(messageService.getMessagesBetweenUsers(senderId, recipientId));
    }

    private MessageDTO sendMessageWithAuthentication(MessageDTO messageDTO) {
        CustomAuthentication customAuth = (CustomAuthentication) SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) customAuth.getPrincipal();
        messageDTO.setSenderId(userId);
        return messageService.sendMessage(messageDTO);
    }
}

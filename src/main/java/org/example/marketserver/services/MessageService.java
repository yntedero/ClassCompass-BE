package org.example.marketserver.services;

import org.example.marketserver.dtos.MessageDTO;
import org.example.marketserver.models.Message;
import org.example.marketserver.models.User;
import org.example.marketserver.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;

    @Autowired
    public MessageService(MessageRepository messageRepository, UserService userService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    @Transactional
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        User sender = userService.getUserById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + messageDTO.getSenderId()));
        User receiver = userService.getUserById(messageDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + messageDTO.getReceiverId()));

        List<Message> existingMessages = messageRepository.findMessagesBetweenUsers(sender, receiver);
        Message message;
        if (existingMessages.isEmpty()) {
            message = new Message();
            message.setSender(sender);
            message.setReceiver(receiver);
        } else {
            message = existingMessages.get(0);
        }

        message.setContent(messageDTO.getContent());
        message.setTimestamp(LocalDateTime.now());
        message = messageRepository.save(message);
        return mapToDTO(message);
    }





    @Transactional
    public List<MessageDTO> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        User sender = userService.getUserById(senderId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + senderId));
        User receiver = userService.getUserById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + receiverId));

        List<Message> messages = messageRepository.findMessagesBetweenUsers(sender, receiver);
        return messages.stream().map(this::mapToDTO).collect(Collectors.toList());
    }


    private MessageDTO mapToDTO(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setSenderId(message.getSender().getId());
        messageDTO.setReceiverId(message.getReceiver().getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setTimestamp(message.getTimestamp());
        return messageDTO;
    }

}

package org.example.marketserver.services;

import org.example.marketserver.dtos.MessageDTO;
import org.example.marketserver.models.Message;
import org.example.marketserver.models.User;
import org.example.marketserver.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
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
        User sender = userService.getUserEntityById(messageDTO.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userService.getUserEntityById(messageDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(messageDTO.getContent());
        message.setTimestamp(messageDTO.getTimestamp());

        Message savedMessage = messageRepository.save(message);
        return mapToDTO(savedMessage);
    }

    public List<MessageDTO> getMessagesBetweenUsers(Long senderId, Long receiverId) {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverIdOrderByTimestampAsc(senderId, receiverId);
        return messages.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private MessageDTO mapToDTO(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }
}
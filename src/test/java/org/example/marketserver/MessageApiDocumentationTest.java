package org.example.marketserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.marketserver.controllers.MessageController;
import org.example.marketserver.dtos.MessageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MessageController.class)
public class MessageApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private MessageDTO messageDTO;

    @BeforeEach
    public void setUp() {
        messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setSenderId(1L);
        messageDTO.setReceiverId(2L);
        messageDTO.setContent("Hello");

    }

    @Test
    public void testSendMessage() throws Exception {
        when(messageService.sendMessage(any(MessageDTO.class))).thenReturn(messageDTO);

        mockMvc.perform(post("/chat.send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void testFindChatMessages() throws Exception {
        List<MessageDTO> messages = Collections.singletonList(messageDTO);
        when(messageService.getMessagesBetweenUsers(1L, 2L)).thenReturn(messages);

        mockMvc.perform(get("/messages/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Hello"));
    }

}

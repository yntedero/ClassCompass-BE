package org.example.marketserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.marketserver.controllers.OfferController;
import org.example.marketserver.dtos.OfferDTO;
import org.example.marketserver.services.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OfferController.class)
public class OfferApiDocumentationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfferService offerService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private OfferDTO offerDTO;

    @BeforeEach
    public void setUp() {
        offerDTO = new OfferDTO();
        offerDTO.setId(1L);
        offerDTO.setTitle("Special Offer");
        offerDTO.setDescription("A very special offer");
        offerDTO.setUserId(2L);
        offerDTO.setCityId(3L);
        offerDTO.setCategoryId(4L);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void createOfferTest() throws Exception {
        when(offerService.createOffer(any(OfferDTO.class))).thenReturn(offerDTO);

        mockMvc.perform(post("/api/offers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(offerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(offerDTO.getTitle()));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAllOffersTest() throws Exception {
        List<OfferDTO> offers = Collections.singletonList(offerDTO);
        when(offerService.getAllOffers(any(), any())).thenReturn(offers);

        mockMvc.perform(get("/api/offers")
                        .param("cityId", "3")
                        .param("categoryId", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(offerDTO.getTitle()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteOfferTest() throws Exception {
        doNothing().when(offerService).deleteOffer(anyLong());

        mockMvc.perform(delete("/api/offers/{id}", offerDTO.getId()))
                .andExpect(status().isNoContent());
    }
}

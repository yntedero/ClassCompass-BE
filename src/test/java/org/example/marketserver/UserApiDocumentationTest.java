package org.example.marketserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.marketserver.controllers.UserController;
import org.example.marketserver.dtos.UserDTO;
import org.example.marketserver.services.UserService;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private UserDTO userDTO;
    private UserDTO updatedUserDTO;

    @BeforeEach
    public void setUp() {
        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setEmail("test@example.com");
        userDTO.setFirstName("Test");
        userDTO.setLastName("User");
        userDTO.setContact("1234567890");
        userDTO.setPassword("password");
        userDTO.setRole("USER");
        userDTO.setStatus("ACTIVE");

        updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(1L);
        updatedUserDTO.setEmail("updated@example.com");
        updatedUserDTO.setFirstName("Updated");
        updatedUserDTO.setLastName("User");
        updatedUserDTO.setContact("0987654321");
        updatedUserDTO.setPassword("newpassword");
        updatedUserDTO.setRole("ADMIN");
        updatedUserDTO.setStatus("ACTIVE");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createUserTest() throws Exception {
        given(userService.createUser(any(UserDTO.class))).willReturn(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserByIdTest() throws Exception {
        given(userService.getUserById(anyLong())).willReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/{id}", userDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userDTO.getEmail()));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAllUsersTest() throws Exception {
        List<UserDTO> users = Collections.singletonList(userDTO);
        given(userService.getAllUsers()).willReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(userDTO.getEmail()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void updateUserTest() throws Exception {
        given(userService.updateUser(eq(userDTO.getId()), any(UserDTO.class))).willReturn(Optional.of(updatedUserDTO));

        mockMvc.perform(put("/api/users/{id}", userDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(updatedUserDTO.getEmail()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteUserTest() throws Exception {
        willDoNothing().given(userService).deleteUser(anyLong());

        mockMvc.perform(delete("/api/users/{id}", userDTO.getId()))
                .andExpect(status().isNoContent());
    }
}
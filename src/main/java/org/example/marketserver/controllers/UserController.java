package com.mustmarket.controller;

import com.mustmarket.dto.UserDTO;
import com.mustmarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoints to create, retrieve, update, and delete users
    // You should work with UserDTO in your endpoints to match the UserService
    // ...
}

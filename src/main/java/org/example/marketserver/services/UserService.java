package com.mustmarket.service;

import com.mustmarket.dto.UserDTO;
import com.mustmarket.repository.UserRepository;
import com.mustmarket.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Business logic to handle user operations using UserDTO
    // ...
}

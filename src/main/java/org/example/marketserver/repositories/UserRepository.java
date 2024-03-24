package com.example.classserver.repositories;

import com.mustmarket.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be declared here
}

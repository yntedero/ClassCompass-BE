package org.example.marketserver.repositories;

import org.example.marketserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findById(String username);
Optional<User> findByEmail(String email);
}

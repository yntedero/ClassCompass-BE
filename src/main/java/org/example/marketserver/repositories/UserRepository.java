package org.example.marketserver.repositories;

import org.example.marketserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be declared here
}

package org.example.marketserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.example.marketserver.models.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByToUser(String toUser);
    @Query("SELECT m FROM Message m WHERE m.toUser = :user OR m.fromUser = :user")
    List<Message> findAllByUser(@Param("user") String user);
}
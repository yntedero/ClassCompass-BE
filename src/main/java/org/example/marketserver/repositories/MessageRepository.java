package org.example.marketserver.repositories;

import org.example.marketserver.models.Message;
import org.example.marketserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE (m.sender = :sender AND m.receiver = :receiver) OR (m.sender = :receiver AND m.receiver = :sender)")
    List<Message> findMessagesBetweenUsers(@Param("sender") User sender, @Param("receiver") User receiver);
}
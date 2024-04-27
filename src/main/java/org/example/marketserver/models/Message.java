package org.example.marketserver.models;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "messages")
@Data
public class Message{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String toUser;
    private String message;
    private String fromUser;
}
package org.example.marketserver.security.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.marketserver.models.User;
import java.time.LocalDateTime;

@Entity(name = "token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "token")
    private String token;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "valid_until", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

}

package org.example.marketserver.security.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.example.marketserver.security.entities.TokenEntity;
import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);
    Long deleteByToken(String token);
}

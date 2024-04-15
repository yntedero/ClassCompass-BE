package org.example.marketserver.security.repositories;

import org.example.marketserver.security.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
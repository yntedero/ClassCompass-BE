package org.example.marketserver.repositories;


import org.example.marketserver.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Offer, Long> {
}

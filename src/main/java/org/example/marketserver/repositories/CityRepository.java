package org.example.marketserver.repositories;

import org.example.marketserver.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}

package org.example.marketserver.repositories;


import org.example.marketserver.models.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long>, JpaSpecificationExecutor<Offer> {
    List<Offer> findAllByCityIdAndCategoryId(Long cityId, Long categoryId);

}
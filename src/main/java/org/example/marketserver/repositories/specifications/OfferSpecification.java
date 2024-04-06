package org.example.marketserver.repositories.specifications;
import org.example.marketserver.models.Offer;
import org.springframework.data.jpa.domain.Specification;

public class OfferSpecification {

    public static Specification<Offer> hasCityId(Long cityId) {
        return (offer, cq, cb) -> cityId == null ? null : cb.equal(offer.get("cityId"), cityId);
    }

    public static Specification<Offer> hasCategoryId(Long categoryId) {
        return (offer, cq, cb) -> categoryId == null ? null : cb.equal(offer.get("categoryId"), categoryId);
    }
}
package org.example.marketserver.services;

import org.example.marketserver.dtos.OfferDTO;
import org.example.marketserver.models.Offer;
import org.example.marketserver.repositories.OfferRepository;
import org.example.marketserver.repositories.specifications.OfferSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.example.marketserver.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public OfferDTO createOffer(OfferDTO offerDTO) {
        Offer offer = new Offer();
        offer.setTitle(offerDTO.getTitle());
        offer.setDescription(offerDTO.getDescription());
        offer.setUserId(offerDTO.getUserId());
        offer.setCityId(offerDTO.getCityId());
        offer.setCategoryId(offerDTO.getCategoryId());

        offer = offerRepository.save(offer);

        OfferDTO savedOfferDTO = convertToDTO(offer);
//        savedOfferDTO.setId(offer.getId());
//        savedOfferDTO.setTitle(offer.getTitle());
//        savedOfferDTO.setDescription(offer.getDescription());
//        savedOfferDTO.setUserId(offer.getUserId());
//        savedOfferDTO.setCityId(offer.getCityId());
//        savedOfferDTO.setCategoryId(offer.getCategoryId());

        return savedOfferDTO;
    }

    public List<OfferDTO> getAllOffers(Long cityId, Long categoryId) {
        Specification<Offer> spec = Specification.where(null);

        if (cityId != null) {
            spec = spec.and(OfferSpecification.hasCityId(cityId));
        }

        if (categoryId != null) {
            spec = spec.and(OfferSpecification.hasCategoryId(categoryId));
        }

        List<Offer> offers = offerRepository.findAll(spec);

        return offers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public void deleteOffer(Long id) {
        offerRepository.deleteById(id);
    }

    private OfferDTO convertToDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setTitle(offer.getTitle());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setUserId(offer.getUserId());
        offerDTO.setCityId(offer.getCityId());
        offerDTO.setCategoryId(offer.getCategoryId());
        return offerDTO;
    }
    public OfferDTO getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id " + id));
        return convertToDTO(offer);
    }
}
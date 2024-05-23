package org.example.marketserver.services;

import org.example.marketserver.dtos.OfferDTO;
import org.example.marketserver.models.Offer;
import org.example.marketserver.repositories.OfferRepository;
import org.example.marketserver.repositories.specifications.OfferSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }
    @Transactional
    public OfferDTO createOffer(OfferDTO offerDTO) {

        Offer offer = new Offer();
        offer.setTitle(offerDTO.getTitle());
        offer.setDescription(offerDTO.getDescription());
        offer.setUserId(offerDTO.getUserId());
        offer.setUserEmail(offerDTO.getUserEmail());
        offer.setCityId(offerDTO.getCityId());
        offer.setCategoryId(offerDTO.getCategoryId());

        offer.setFile(offerDTO.getFile());

        offer = offerRepository.save(offer);

        offerRepository.flush();
        OfferDTO savedOfferDTO = convertToDTO(offer);
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
    public OfferDTO getOfferById(Long id) {
        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Offer not found with id " + id));
        return convertToDTO(offer);
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
        offerDTO.setUserEmail(offer.getUserEmail());
        offerDTO.setCityId(offer.getCityId());
        offerDTO.setCategoryId(offer.getCategoryId());
        offerDTO.setFile(offer.getFile());
        return offerDTO;
    }
}
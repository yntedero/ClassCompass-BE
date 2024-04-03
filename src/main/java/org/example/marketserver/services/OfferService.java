package org.example.marketserver.services;


import org.example.marketserver.dtos.OfferDTO;
import org.example.marketserver.models.Offer;
import org.example.marketserver.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.marketserver.repositories.UserRepository;
import org.example.marketserver.models.User;
import org.example.marketserver.exceptions.*;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    @Autowired
    public OfferService(OfferRepository offerRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    public OfferDTO createOffer(OfferDTO offerDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        User authenticatedUser = new User();
        authenticatedUser.setEmail(authenticatedUsername);

        if (offerDTO.getCategory() == null || offerDTO.getCity() == null) {
            throw new IllegalArgumentException("you must provide a catogyr and a city :(");
        }


        Offer offer = new Offer();
        offer.setTitle(offerDTO.getTitle());
        offer.setDescription(offerDTO.getDescription());
        offer.setUserId(authenticatedUser.getId());
        offer.setCity(offerDTO.getCity());
        offer.setCategory(offerDTO.getCategory());

        Offer savedOffer = offerRepository.save(offer);

        return mapToDTO(savedOffer);
    }
    public List<OfferDTO> getAllOffers(Long cityId, Long categoryId) {
        List<Offer> offers = offerRepository.findAllByCityIdAndCategoryId(cityId, categoryId);
        return offers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    public void deleteOffer(Long id) {
        if (!offerRepository.existsById(id)) {
            throw new NotFoundException("Offer not found");
        }

        offerRepository.deleteById(id);
    }

    private OfferDTO mapToDTO(Offer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setTitle(offer.getTitle());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setUserId(offer.getUserId()); // Set the user ID

        return offerDTO;
    }
}

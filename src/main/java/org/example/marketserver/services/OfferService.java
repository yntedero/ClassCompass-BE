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
        // Check if the user with the provided ID exists
        userRepository.findById(offerDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        // Create a new Offer entity and set its properties
        Offer offer = new Offer();
        offer.setTitle(offerDTO.getTitle());
        offer.setDescription(offerDTO.getDescription());
        offer.setUserId(offerDTO.getUserId()); // Set only the user ID

        // Save the Offer entity
        Offer savedOffer = offerRepository.save(offer);

        // Map the saved Offer entity to DTO and return it
        return mapToDTO(savedOffer);
    }

    public List<OfferDTO> getAllOffers() {
        // Retrieve all offers from the repository
        List<Offer> offers = offerRepository.findAll();

        // Map the offers to DTOs and collect them into a list
        return offers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteOffer(Long id) {
        // Check if the offer with the provided ID exists
        if (!offerRepository.existsById(id)) {
            throw new NotFoundException("Offer not found");
        }

        // Delete the offer
        offerRepository.deleteById(id);
    }

    private OfferDTO mapToDTO(Offer offer) {
        // Map the Offer entity to an OfferDTO
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setTitle(offer.getTitle());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setUserId(offer.getUserId()); // Set the user ID

        return offerDTO;
    }
}

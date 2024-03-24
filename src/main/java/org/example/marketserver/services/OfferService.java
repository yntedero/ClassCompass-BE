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
        User user = userRepository.findById(offerDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Offer offer = new Offer();
        offer.setName(offerDTO.getName());
        offer.setDescription(offerDTO.getDescription());
        offer.setUser(user);

        Offer savedOffer = offerRepository.save(offer);

        OfferDTO savedOfferDTO = mapToDTO(savedOffer);

        return savedOfferDTO;
    }

    public List<OfferDTO> getAllOffers() {
        List<Offer> offers = offerRepository.findAll();

        List<OfferDTO> offerDTOs = offers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return offerDTOs;
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
        offerDTO.setName(offer.getName());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setUserId(offer.getUser().getId());
        return offerDTO;
    }

}

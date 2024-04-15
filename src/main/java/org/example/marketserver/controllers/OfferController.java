package org.example.marketserver.controllers;


import org.example.marketserver.dtos.OfferDTO;
import org.example.marketserver.services.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
    private final OfferService offerService;

    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<OfferDTO> createOffer(@RequestBody OfferDTO offerDTO) {
        OfferDTO newOffer = offerService.createOffer(offerDTO);
        return ResponseEntity.ok(newOffer);
    }

    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers(@RequestParam(required = false) Long cityId,
                                                       @RequestParam(required = false) Long categoryId) {
        List<OfferDTO> filteredOffers = offerService.getAllOffers(cityId, categoryId);
        return ResponseEntity.ok(filteredOffers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOfferById(@PathVariable Long id) {
        OfferDTO offer = offerService.getOfferById(id);
        return ResponseEntity.ok(offer);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }
}

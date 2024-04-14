package org.example.marketserver.controllers;
import org.example.marketserver.dtos.CityDTO;
import org.example.marketserver.models.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.marketserver.services.CityService;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public CityDTO createCity(@RequestBody CityDTO cityDTO) {
        return cityService.createCity(cityDTO);
    }

    @GetMapping
    public List<CityDTO> getAllCities() {
        return cityService.getAllCities();
    }
}
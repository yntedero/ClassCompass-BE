package org.example.marketserver.dtos;

import org.example.marketserver.models.City;
import org.example.marketserver.models.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OfferDTO {
    private Long id;
    private String title;
    private String description;
    private Long userId;
    private Long cityId;
    private Long categoryId;
    private String file;
}
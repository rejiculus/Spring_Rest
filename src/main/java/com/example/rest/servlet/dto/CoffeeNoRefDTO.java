package com.example.rest.servlet.dto;

import com.example.rest.entity.Coffee;
import com.example.rest.service.dto.ICoffeeNoRefDTO;

public record CoffeeNoRefDTO(Long id,
                             String name,
                             Double price)
        implements ICoffeeNoRefDTO {

    public CoffeeNoRefDTO(Coffee coffee) {
        this(
                coffee.getId(),
                coffee.getName(),
                coffee.getPrice()
        );
    }

}

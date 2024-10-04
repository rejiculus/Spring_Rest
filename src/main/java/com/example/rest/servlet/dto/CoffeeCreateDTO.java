package com.example.rest.servlet.dto;

import com.example.rest.service.dto.ICoffeeCreateDTO;

public record CoffeeCreateDTO(String name,
                              Double price)
        implements ICoffeeCreateDTO {
}

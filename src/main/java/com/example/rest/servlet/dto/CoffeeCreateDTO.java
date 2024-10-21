package com.example.rest.servlet.dto;

import com.example.rest.service.dto.ICoffeeCreateDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

public record CoffeeCreateDTO(
        @NotEmpty(message = "Name can't be empty!")
        String name,
        @PositiveOrZero(message = "Tip size can't be less than zero!")
        Double price
) implements ICoffeeCreateDTO {
}

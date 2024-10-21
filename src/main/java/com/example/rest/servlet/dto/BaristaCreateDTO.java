package com.example.rest.servlet.dto;

import com.example.rest.service.dto.IBaristaCreateDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

public record BaristaCreateDTO(
        @NotEmpty(message = "Full name can't be empty!")
        String fullName,
        @PositiveOrZero(message = "Tip size can't be less than zero!")
        Double tipSize
) implements IBaristaCreateDTO {
}

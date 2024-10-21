package com.example.rest.servlet.dto;

import com.example.rest.service.dto.ICoffeeUpdateDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record CoffeeUpdateDTO(

        @PositiveOrZero(message = "Id can't be less than zero!")
        Long id,

        @NotEmpty(message = "Name can't be empty!")
        String name,

        @PositiveOrZero(message = "Tip size can't be less than zero!")
        Double price,

        List<Long> orderIdList
) implements ICoffeeUpdateDTO {
}

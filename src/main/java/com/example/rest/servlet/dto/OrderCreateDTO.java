package com.example.rest.servlet.dto;

import com.example.rest.service.dto.IOrderCreateDTO;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.List;

public record OrderCreateDTO(
        @PositiveOrZero(message = "Barista id can't be less than zero!")
        Long baristaId,
        List<Long> coffeeIdList)
        implements IOrderCreateDTO {
}

package com.example.rest.servlet.dto;

import com.example.rest.service.dto.IOrderUpdateDTO;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;

public record OrderUpdateDTO(
        @PositiveOrZero(message = "Id can't be less than zero!")
        Long id,

        @PositiveOrZero(message = "Barista id can't be less than zero!")
        Long baristaId,

        @PastOrPresent(message = "Order can be created only in past or in present, but not in the future!")
        LocalDateTime created,

        @PastOrPresent(message = "Order can be completed only in past or in present, but not in the future!")
        LocalDateTime completed,

        @PositiveOrZero(message = "Price can't be less than zero!")
        Double price,

        List<Long> coffeeIdList)
        implements IOrderUpdateDTO {
}

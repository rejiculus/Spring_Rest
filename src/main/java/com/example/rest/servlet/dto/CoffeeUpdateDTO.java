package com.example.rest.servlet.dto;

import com.example.rest.service.dto.ICoffeeUpdateDTO;

import java.util.List;

public record CoffeeUpdateDTO(Long id,
                              String name,
                              Double price,
                              List<Long> orderIdList)
        implements ICoffeeUpdateDTO {
}

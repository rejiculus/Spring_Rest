package com.example.rest.servlet.dto;

import com.example.rest.entity.Coffee;
import com.example.rest.service.dto.ICoffeePublicDTO;

import java.util.List;

public record CoffeePublicDTO(Long id,
                              String name,
                              Double price,
                              List<OrderNoRefDTO> orders)
        implements ICoffeePublicDTO {

    public CoffeePublicDTO(Coffee coffee) {
        this(
                coffee.getId(),
                coffee.getName(),
                coffee.getPrice(),
                coffee.getOrderList().stream()
                        .map(OrderNoRefDTO::new)
                        .toList()
        );
    }
}

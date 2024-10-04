package com.example.rest.servlet.dto;

import com.example.rest.entity.Coffee;
import com.example.rest.entity.Order;
import com.example.rest.service.dto.ICoffeeNoRefDTO;

import java.util.List;

public record CoffeeNoRefDTO(Long id,
                             String name,
                             Double price,
                             List<Long> orderIdList)
        implements ICoffeeNoRefDTO {

    public CoffeeNoRefDTO(Coffee coffee) {
        this(
                coffee.getId(),
                coffee.getName(),
                coffee.getPrice(),
                coffee.getOrderList().stream()
                        .map(Order::getId)
                        .toList()
        );
    }

}

package com.example.rest.servlet.dto;

import com.example.rest.entity.Order;
import com.example.rest.service.dto.IOrderPublicDTO;

import java.time.LocalDateTime;
import java.util.List;

public record OrderPublicDTO(Long id,
                             BaristaNoRefDTO baristaId,
                             LocalDateTime created,
                             LocalDateTime completed,
                             Double price,
                             List<CoffeeNoRefDTO> coffeeIdList)
        implements IOrderPublicDTO {

    public OrderPublicDTO(Order order) {
        this(
                order.getId(),
                new BaristaNoRefDTO(order.getBarista()),
                order.getCreated(),
                order.getCompleted(),
                order.getPrice(),
                order.getCoffeeList().stream()
                        .map(CoffeeNoRefDTO::new)
                        .toList()
        );
    }
}

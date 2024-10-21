package com.example.rest.servlet.dto;

import com.example.rest.entity.Order;
import com.example.rest.service.dto.IOrderNoRefDTO;

import java.time.LocalDateTime;

public record OrderNoRefDTO(Long id,
                            Long baristaId,
                            LocalDateTime created,
                            LocalDateTime completed,
                            Double price)
        implements IOrderNoRefDTO {

    public OrderNoRefDTO(Order order) {
        this(
                order.getId(),
                order.getBarista().getId(),
                order.getCreated(),
                order.getCompleted(),
                order.getPrice()
        );
    }
}

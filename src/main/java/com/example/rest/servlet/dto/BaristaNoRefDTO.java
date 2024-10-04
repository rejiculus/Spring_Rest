package com.example.rest.servlet.dto;

import com.example.rest.entity.Barista;
import com.example.rest.entity.Order;
import com.example.rest.service.dto.IBaristaNoRefDTO;

import java.util.List;

public record BaristaNoRefDTO(Long id,
                              String fullName,
                              Double tipSize,
                              List<Long> orderIdList) implements IBaristaNoRefDTO {

    public BaristaNoRefDTO(Barista barista) {
        this(
                barista.getId(),
                barista.getFullName(),
                barista.getTipSize(),
                barista.getOrderList().stream()
                        .map(Order::getId)
                        .toList()
        );
    }

}

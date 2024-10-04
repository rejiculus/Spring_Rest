package com.example.rest.servlet.dto;

import com.example.rest.entity.Barista;
import com.example.rest.service.dto.IBaristaPublicDTO;

import java.util.List;

public record BaristaPublicDTO(Long id,
                               String fullName,
                               Double tipSize,
                               List<OrderNoRefDTO> orders)
        implements IBaristaPublicDTO {

    public BaristaPublicDTO(Barista barista) {
        this(
                barista.getId(),
                barista.getFullName(),
                barista.getTipSize(),
                barista.getOrderList().stream()
                        .map(OrderNoRefDTO::new)
                        .toList()
        );
    }

}

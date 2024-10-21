package com.example.rest.servlet.dto;

import com.example.rest.entity.Barista;
import com.example.rest.service.dto.IBaristaNoRefDTO;

public record BaristaNoRefDTO(Long id,
                              String fullName,
                              Double tipSize)
        implements IBaristaNoRefDTO {

    public BaristaNoRefDTO(Barista barista) {
        this(
                barista.getId(),
                barista.getFullName(),
                barista.getTipSize()
        );
    }

}

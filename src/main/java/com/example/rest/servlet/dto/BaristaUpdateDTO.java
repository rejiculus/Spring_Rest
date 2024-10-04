package com.example.rest.servlet.dto;

import com.example.rest.service.dto.IBaristaUpdateDTO;

import java.util.List;

public record BaristaUpdateDTO(Long id,
                               String fullName,
                               Double tipSize,
                               List<Long> orderIdList)
        implements IBaristaUpdateDTO {
}

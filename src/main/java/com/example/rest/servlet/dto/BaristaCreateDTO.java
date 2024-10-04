package com.example.rest.servlet.dto;

import com.example.rest.service.dto.IBaristaCreateDTO;

public record BaristaCreateDTO(String fullName,
                               Double tipSize
) implements IBaristaCreateDTO {
}

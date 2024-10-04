package com.example.rest.servlet.dto;

import com.example.rest.service.dto.IOrderCreateDTO;

import java.util.List;

public record OrderCreateDTO(Long baristaId,
                             List<Long> coffeeIdList)
        implements IOrderCreateDTO {
}

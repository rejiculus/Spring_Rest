package com.example.rest.service.dto;

import java.util.List;

public interface ICoffeePublicDTO {
    Long id();

    String name();

    Double price();

    List<? extends IOrderNoRefDTO> orders();
}

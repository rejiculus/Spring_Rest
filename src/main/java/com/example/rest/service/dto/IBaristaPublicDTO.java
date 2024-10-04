package com.example.rest.service.dto;

import java.util.List;

public interface IBaristaPublicDTO {
    Long id();

    String fullName();

    Double tipSize();

    List<? extends IOrderNoRefDTO> orders();
}

package com.example.rest.service.dto;

import java.util.List;

public interface IBaristaNoRefDTO {
    Long id();

    String fullName();

    Double tipSize();

    List<Long> orderIdList();
}

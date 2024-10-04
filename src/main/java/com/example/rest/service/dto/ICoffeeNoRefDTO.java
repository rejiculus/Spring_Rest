package com.example.rest.service.dto;

import java.util.List;

public interface ICoffeeNoRefDTO {
    Long id();

    String name();

    Double price();

    List<Long> orderIdList();

}

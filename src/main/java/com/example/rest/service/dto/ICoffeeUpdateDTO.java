package com.example.rest.service.dto;

import java.util.List;

public interface ICoffeeUpdateDTO extends IUpdateDTO {
    Long id();

    String name();

    Double price();

    List<Long> orderIdList();
}

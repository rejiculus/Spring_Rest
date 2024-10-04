package com.example.rest.service.dto;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderUpdateDTO extends IUpdateDTO {
    Long id();

    Long baristaId();

    LocalDateTime created();

    LocalDateTime completed();

    Double price();

    List<Long> coffeeIdList();
}

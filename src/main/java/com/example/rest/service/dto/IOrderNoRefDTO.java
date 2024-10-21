package com.example.rest.service.dto;

import java.time.LocalDateTime;

public interface IOrderNoRefDTO {
    Long id();

    Long baristaId();

    LocalDateTime created();

    LocalDateTime completed();

    Double price();

}

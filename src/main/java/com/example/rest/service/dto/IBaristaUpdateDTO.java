package com.example.rest.service.dto;

import java.util.List;

public interface IBaristaUpdateDTO extends IUpdateDTO {
    Long id();

    String fullName();

    Double tipSize();

    List<Long> orderIdList();
}

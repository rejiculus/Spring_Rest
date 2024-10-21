package com.example.rest.service.dto;

public interface IBaristaUpdateDTO extends IUpdateDTO {
    Long id();

    String fullName();

    Double tipSize();

}

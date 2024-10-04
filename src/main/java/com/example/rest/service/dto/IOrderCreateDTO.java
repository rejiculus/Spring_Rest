package com.example.rest.service.dto;

import java.util.List;

public interface IOrderCreateDTO extends ICreateDTO {

    Long baristaId();

    List<Long> coffeeIdList();
}

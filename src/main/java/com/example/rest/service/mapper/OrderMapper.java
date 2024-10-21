package com.example.rest.service.mapper;

import com.example.rest.entity.Barista;
import com.example.rest.entity.Coffee;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.BaristaNotFoundException;
import com.example.rest.entity.exception.CoffeeNotFoundException;
import com.example.rest.repository.BaristaRepository;
import com.example.rest.repository.CoffeeRepository;
import com.example.rest.service.dto.IOrderCreateDTO;
import com.example.rest.service.dto.IOrderUpdateDTO;
import com.example.rest.service.exception.DuplicatedElementsException;
import com.example.rest.servlet.dto.OrderPublicDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {CoffeeRepository.class, BaristaRepository.class, CoffeeMapper.class, BaristaMapper.class},
        imports = {Coffee.class, Barista.class, BaristaNotFoundException.class, List.class}
)
@Component
public abstract class OrderMapper {
    @Autowired
    protected CoffeeRepository coffeeRepository;
    @Autowired
    protected CoffeeMapper coffeeMapper;
    @Autowired
    protected BaristaRepository baristaRepository;
    @Autowired
    protected BaristaMapper baristaMapper;

    @Mapping(target = "coffeeList", expression = "java(parseCoffees(orderCreateDTO.coffeeIdList()))")
    @Mapping(target = "barista", expression = "java(baristaRepository.findById(orderCreateDTO.baristaId()).orElseThrow(()-> new BaristaNotFoundException(orderCreateDTO.baristaId())))")
    public abstract Order createDtoToEntity(IOrderCreateDTO orderCreateDTO);

    @Mapping(target = "id", expression = "java(orderUpdateDTO.id())")
    @Mapping(target = "coffeeList", expression = "java(parseCoffees(orderUpdateDTO.coffeeIdList()))")
    @Mapping(target = "barista", expression = "java(baristaRepository.findById(orderUpdateDTO.baristaId()).orElseThrow(()-> new BaristaNotFoundException(orderUpdateDTO.baristaId())))")
    @Mapping(target = "created", expression = "java(orderUpdateDTO.created())")
    @Mapping(target = "completed", expression = "java(orderUpdateDTO.completed())")
    @Mapping(target = "price", expression = "java(orderUpdateDTO.price())")
    public abstract Order updateDtoToEntity(IOrderUpdateDTO orderUpdateDTO);

    @Mapping(target = "coffees", expression = "java(order.getCoffeeList().stream().map(coffeeMapper::entityToNoRefDto).toList())")
    @Mapping(target = "baristaId", expression = "java(baristaMapper.entityToNoRefDto(order.getBarista()))")
    public abstract OrderPublicDTO entityToDto(Order order);

    protected List<Coffee> parseCoffees(List<Long> coffeeIdList) {
        if (coffeeIdList.isEmpty())
            return List.of();

        long uniqueElements = coffeeIdList.stream().distinct().count();
        if (coffeeIdList.size() != uniqueElements)
            throw new DuplicatedElementsException();

        List<Coffee> existingCoffeeList = coffeeRepository.findAllById(coffeeIdList);
        if (existingCoffeeList.isEmpty())
            throw new CoffeeNotFoundException(uniqueElements);

        if (existingCoffeeList.size() != uniqueElements) {
            List<Long> existingIds = existingCoffeeList.stream().map(Coffee::getId).toList();
            coffeeIdList.removeAll(existingIds);
            throw new CoffeeNotFoundException(coffeeIdList);
        }
        return existingCoffeeList;
    }

}

package com.example.rest.service.mapper;

import com.example.rest.entity.Barista;
import com.example.rest.entity.Order;
import com.example.rest.repository.OrderRepository;
import com.example.rest.service.dto.IBaristaCreateDTO;
import com.example.rest.service.dto.IBaristaUpdateDTO;
import com.example.rest.servlet.dto.BaristaNoRefDTO;
import com.example.rest.servlet.dto.BaristaPublicDTO;
import com.example.rest.servlet.dto.OrderNoRefDTO;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {OrderMapper.class, OrderRepository.class},
        imports = {Order.class, List.class})
@Component
public abstract class BaristaMapper {
    @Autowired
    protected EntityManager entityManager;

    @Mapping(target = "fullName", expression = "java(baristaCreateDTO.fullName())")
    @Mapping(target = "tipSize", expression = "java(baristaCreateDTO.tipSize())")
    @Mapping(target = "orderList", expression = "java(List.of())")
    public abstract Barista createDtoToEntity(IBaristaCreateDTO baristaCreateDTO);


    @Mapping(target = "id", expression = "java(baristaUpdateDTO.id())")
    @Mapping(target = "fullName", expression = "java(baristaUpdateDTO.fullName())")
    @Mapping(target = "tipSize", expression = "java(baristaUpdateDTO.tipSize())")
    @Mapping(target = "orderList", expression = "java(List.of())")
    public abstract Barista updateDtoToEntity(IBaristaUpdateDTO baristaUpdateDTO);

    @Mapping(target = "orders", expression = "java(parseOrderIdList(barista))")
    public abstract BaristaPublicDTO entityToDto(Barista barista);

    public abstract BaristaNoRefDTO entityToNoRefDto(Barista barista);

    protected List<OrderNoRefDTO> parseOrderIdList(Barista barista) {
        entityManager.merge(barista);
        List<Order> orderList = barista.getOrderList();
        Hibernate.initialize(orderList);//todo нужно ли это еще?
        return barista.getOrderList().stream()
                .map(order -> new OrderNoRefDTO(order.getId(),
                        order.getBarista().getId(),
                        order.getCreated(),
                        order.getCompleted(),
                        order.getPrice()))
                .toList();

    }
}

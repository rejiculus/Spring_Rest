package com.example.rest.service.mapper;

import com.example.rest.entity.Coffee;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.OrderNotFoundException;
import com.example.rest.repository.OrderRepository;
import com.example.rest.service.dto.ICoffeeCreateDTO;
import com.example.rest.service.dto.ICoffeeUpdateDTO;
import com.example.rest.service.exception.DuplicatedElementsException;
import com.example.rest.servlet.dto.CoffeeNoRefDTO;
import com.example.rest.servlet.dto.CoffeePublicDTO;
import com.example.rest.servlet.dto.OrderNoRefDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring",
        uses = {OrderRepository.class, OrderMapper.class},
        imports = {Order.class, List.class})
@Component
public abstract class CoffeeMapper {
    @Autowired
    protected OrderRepository orderRepository;

    @Mapping(target = "name", expression = "java(coffeeCreateDTO.name())")
    @Mapping(target = "price", expression = "java(coffeeCreateDTO.price())")
    @Mapping(target = "orderList", expression = "java(List.of())")
    public abstract Coffee createDtoToEntity(ICoffeeCreateDTO coffeeCreateDTO);

    @Mapping(target = "id", expression = "java(coffeeUpdateDTO.id())")
    @Mapping(target = "name", expression = "java(coffeeUpdateDTO.name())")
    @Mapping(target = "price", expression = "java(coffeeUpdateDTO.price())")
    @Mapping(target = "orderList", expression = "java(parseOrders(coffeeUpdateDTO.orderIdList()))")
    public abstract Coffee updateDtoToEntity(ICoffeeUpdateDTO coffeeUpdateDTO);

    @Mapping(target = "orders", expression = "java(parseOrderDtoList(coffee.getOrderList()))")
    public abstract CoffeePublicDTO entityToDto(Coffee coffee);

    public abstract CoffeeNoRefDTO entityToNoRefDto(Coffee coffee);

    protected List<Order> parseOrders(List<Long> orderIdList) {
        if (orderIdList.isEmpty())
            return List.of();

        long uniqueElements = orderIdList.stream().distinct().count();
        if (orderIdList.size() != uniqueElements)
            throw new DuplicatedElementsException();

        List<Order> existingOrderList = orderRepository.findAllById(orderIdList);
        if (existingOrderList.isEmpty())
            throw new OrderNotFoundException(uniqueElements);

        if (existingOrderList.size() != uniqueElements) {
            List<Long> existingIds = existingOrderList.stream().map(Order::getId).toList();
            orderIdList.removeAll(existingIds);
            throw new OrderNotFoundException(orderIdList);
        }
        return existingOrderList;
    }

    protected List<OrderNoRefDTO> parseOrderDtoList(List<Order> orderList) {
        return orderList.stream()
                .map(order -> new OrderNoRefDTO(
                        order.getId(),
                        order.getBarista().getId(),
                        order.getCreated(),
                        order.getCompleted(),
                        order.getPrice()))
                .toList();
    }
}

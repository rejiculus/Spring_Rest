package com.example.rest.service.mapper;

import com.example.rest.entity.Coffee;
import com.example.rest.entity.exception.*;
import com.example.rest.repository.OrderRepository;
import com.example.rest.service.dto.ICoffeeCreateDTO;
import com.example.rest.service.dto.ICoffeeUpdateDTO;

/**
 * Mapper form ICoffeeCreateDTO and ICoffeeUpdateDTO to Coffee entity.
 * Required to specify order repository in constructor.
 */
public class CoffeeDtoToCoffeeMapper {
    private final OrderRepository orderRepository;

    public CoffeeDtoToCoffeeMapper(OrderRepository orderRepository) {
        if (orderRepository == null)
            throw new NullParamException();

        this.orderRepository = orderRepository;
    }

    /**
     * Mapping ICoffeeCreateDTO to Coffee.
     *
     * @param coffeeDTO object with ICoffeeCreateDTO type.
     * @return Coffee object, with not specified id!
     * @throws NullParamException    when coffeeDTO or fields in coffeeDTO is null.
     * @throws NoValidNameException  when name in coffeeDTO is empty.
     * @throws NoValidPriceException when price in coffeeDTO is NaN, Infinite or less than zero.
     */
    public Coffee map(ICoffeeCreateDTO coffeeDTO) {
        if (coffeeDTO == null)
            throw new NullParamException();

        return new Coffee(coffeeDTO.name(), coffeeDTO.price());
    }

    /**
     * Mapping ICoffeeUpdateDTO to Coffee.
     *
     * @param coffeeDTO object with ICoffeeUpdateDTO type.
     * @return Coffee object.
     * @throws NullParamException     when coffeeDTO or fields in coffeeDTO is null.
     * @throws NoValidIdException     when id in coffeeDTO is less than zero.
     * @throws NoValidNameException   when name in coffeeDTO is empty.
     * @throws NoValidPriceException  when price in coffeeDTO is NaN, Infinite or less than zero.
     * @throws OrderNotFoundException when order from coffeeDTO's orderIdList is not found.
     */
    public Coffee map(ICoffeeUpdateDTO coffeeDTO) {
        if (coffeeDTO == null || coffeeDTO.orderIdList() == null)
            throw new NullParamException();

        return new Coffee(
                coffeeDTO.id(),
                coffeeDTO.name(),
                coffeeDTO.price(),
                coffeeDTO.orderIdList()
                        .stream()
                        .map(orderId -> orderRepository.findById(orderId)
                                .orElseThrow(() -> new OrderNotFoundException(orderId)))
                        .toList());
    }
}

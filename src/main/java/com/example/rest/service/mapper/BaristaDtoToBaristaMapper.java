package com.example.rest.service.mapper;

import com.example.rest.entity.Barista;
import com.example.rest.entity.exception.*;
import com.example.rest.repository.OrderRepository;
import com.example.rest.service.dto.IBaristaCreateDTO;
import com.example.rest.service.dto.IBaristaUpdateDTO;


/**
 * Mapper IBaristaCreateDTO and IBaristaUpdateDTO to Barista.
 * Required order repository in constructor's param.
 */
public class BaristaDtoToBaristaMapper {
    private final OrderRepository orderRepository;

    public BaristaDtoToBaristaMapper(OrderRepository orderRepository) {
        if (orderRepository == null)
            throw new NullParamException();

        this.orderRepository = orderRepository;
    }

    /**
     * Mapping IBaristaCreateDTO to Barista.
     *
     * @param baristaDTO object with IBaristaCreateDTO type.
     * @return Barista object.
     * @throws NullParamException      thrown when baristaDTO is null, or fields in baristaDTO is null.
     * @throws NoValidNameException    thrown, from barista entity, when fullName in baristaDTO is empty.
     * @throws NoValidTipSizeException thrown, from barista entity, when tipSize in baristaDTO is NaN, Infinite or less than zero.
     */
    public Barista map(IBaristaCreateDTO baristaDTO) {
        if (baristaDTO == null)
            throw new NullParamException();

        Barista barista = new Barista(baristaDTO.fullName());

        if (baristaDTO.tipSize() != null)
            barista.setTipSize(baristaDTO.tipSize());

        return barista;
    }

    /**
     * Mapping IBaristaUpdateDTO to Barista
     *
     * @param baristaDTO object with IBaristaUpdateDTO type.
     * @return Barista object.
     * @throws NullParamException      thrown when baristaDTO is null, or fields in baristaDTO is null.
     * @throws NoValidIdException      thrown, from barista entity, when id in baristaDTO is less than zero.
     * @throws NoValidNameException    thrown, from barista entity, when fullName in baristaDTO is empty.
     * @throws OrderNotFoundException  thrown when order from baristaDTO orderIdList is not found in db.
     * @throws NoValidTipSizeException thrown, from barista entity, when tipSize in baristaDTO is NaN, Infinite or less than zero.
     */
    public Barista map(IBaristaUpdateDTO baristaDTO) {
        if (baristaDTO == null || baristaDTO.orderIdList() == null)
            throw new NullParamException();

        return new Barista(
                baristaDTO.id(),
                baristaDTO.fullName(),
                baristaDTO.orderIdList().stream()
                        .map(orderId -> orderRepository.findById(orderId)
                                .orElseThrow(() -> new OrderNotFoundException(orderId)))
                        .toList(),
                baristaDTO.tipSize());
    }
}

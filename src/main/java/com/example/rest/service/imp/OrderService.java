package com.example.rest.service.imp;

import com.example.rest.entity.Coffee;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.*;
import com.example.rest.repository.OrderRepository;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.IOrderService;
import com.example.rest.service.dto.IOrderCreateDTO;
import com.example.rest.service.dto.IOrderPublicDTO;
import com.example.rest.service.dto.IOrderUpdateDTO;
import com.example.rest.service.exception.OrderAlreadyCompletedException;
import com.example.rest.service.mapper.OrderMapper;
import com.example.rest.servlet.dto.OrderPublicDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Validated
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderMapper mapper) {
        if (orderRepository == null || mapper == null)
            throw new NullParamException();
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    /**
     * Create 'order' in db by IOrderCreateDTO.
     *
     * @param orderDTO object with IOrderCreateDTO type.
     * @return Order object.
     * @throws NullParamException when coffeeDTO is null or it's fields is null.
     */
    @Override
    @Transactional
    public IOrderPublicDTO create(@Valid IOrderCreateDTO orderDTO) {
        if (orderDTO == null)
            throw new NullParamException();

        Order order = mapper.createDtoToEntity(orderDTO);

        Double price = order.getCoffeeList().stream()
                .map(Coffee::getPrice)
                .reduce(0.0, Double::sum, Double::sum);

        order.setPrice(price * (1.0 + order.getBarista().getTipSize()));
        order.setCreated(LocalDateTime.now());

        order = this.orderRepository.save(order);
        return mapper.entityToDto(order);
    }

    /**
     * Update 'order' in db by IOrderUpdateDTO.
     *
     * @param orderDTO object with IOrderUpdateDTO type.
     * @return updated Order object.
     * @throws NullParamException              when coffeeDTO is null or it's fields is null.
     * @throws NoValidIdException              form mapper, when coffeeDTO's id is less than zero.
     * @throws CreatedNotDefinedException      from mapper, when completed field is specified but created field is not.
     * @throws CompletedBeforeCreatedException from mapper, when completed time is before created time.
     * @throws NoValidTipSizeException         from mapper, when coffeeDTO's price is NaN, Infinite or less than zero.
     */
    @Override
    @Transactional
    public IOrderPublicDTO update(@Valid IOrderUpdateDTO orderDTO) {
        if (orderDTO == null)
            throw new NullParamException();

        Order order = orderRepository.findById(orderDTO.id())
                .orElseThrow(() -> new OrderNotFoundException(orderDTO.id()));

        order = mapper.updateDtoToEntity(orderDTO);

        Double price = order.getCoffeeList().stream()
                .map(Coffee::getPrice)
                .reduce(0.0, Double::sum, Double::sum);
        order.setPrice(price * (1.0 + order.getBarista().getTipSize()));

        order = this.orderRepository.save(order);
        return mapper.entityToDto(order);
    }

    /**
     * Delete 'order' by specified id.
     *
     * @param id deleting order's id.
     * @throws NullParamException     when coffeeDTO is null or it's fields is null.
     * @throws NoValidIdException     form mapper, when coffeeDTO's id is less than zero.
     * @throws OrderNotFoundException when order with specific id is not found in db.
     */
    @Override
    public void delete(Long id) {
        if (id == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);

        this.orderRepository.deleteById(id);
    }


    /**
     * Get 'order' queue. Oldest created, but not completed
     * order - first, youngest - last.
     *
     * @return list of filtered and sorted orders.
     */
    @Override
    @Transactional
    public List<OrderPublicDTO> getOrderQueue() {
        List<Order> orderList = this.orderRepository.findAll();
        orderList = orderList.stream()
                .filter(order -> order.getCompleted() == null)
                .sorted(Comparator.comparing(Order::getCreated))
                .toList();
        return orderList.stream()
                .map(mapper::entityToDto)
                .toList();
    }

    /**
     * Complete 'order' with specified 'id'.
     * Specifying 'completed' field in 'order'.
     *
     * @param id completing order's id.
     * @return completed order.
     * @throws NullParamException             when coffeeDTO is null or it's fields is null.
     * @throws NoValidIdException             form mapper, when coffeeDTO's id is less than zero.
     * @throws OrderNotFoundException         when order with specific id is not found in db.
     * @throws OrderAlreadyCompletedException when order already has completed time.
     */
    @Override
    @Transactional
    public IOrderPublicDTO completeOrder(Long id) {
        if (id == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (order.getCompleted() != null)
            throw new OrderAlreadyCompletedException(order);

        order.setCompleted(LocalDateTime.now());
        order = this.orderRepository.save(order);

        return mapper.entityToDto(order);
    }

    /**
     * Find all 'order' in db.
     *
     * @return list of all 'order' objects
     */
    @Override
    @Transactional
    public List<OrderPublicDTO> findAll() {
        List<Order> orderList = this.orderRepository.findAll();

        return orderList.stream()
                .map(mapper::entityToDto)
                .toList();
    }

    /**
     * Find 'order' by specified id.
     *
     * @param id the desired order "id".
     * @return Order object with specified id.
     * @throws NullParamException     when coffeeDTO is null or it's fields is null.
     * @throws NoValidIdException     form mapper, when coffeeDTO's id is less than zero.
     * @throws OrderNotFoundException when order with specific id is not found in db.
     */
    @Override
    @Transactional
    public OrderPublicDTO findById(Long id) {
        if (id == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);

        Order order = this.orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        return mapper.entityToDto(order);
    }

    /**
     * Find all 'order' grouping by pages and limited.
     *
     * @param page  number of representing page. Can't be less than zero.
     * @param limit number maximum represented objects.
     * @return list of object from specified page. Maximum number object in list equals limit.
     * @throws NoValidPageException  when page is less than zero.
     * @throws NoValidLimitException when limit is less than one.
     */
    @Override
    @Transactional
    public List<OrderPublicDTO> findAllByPage(int page, int limit) {
        if (page < 0)
            throw new NoValidPageException(page);
        if (limit <= 0)
            throw new NoValidLimitException(limit);

        Pageable pageable = PageRequest.of(page, limit);

        Page<Order> orderList = this.orderRepository.findAll(pageable);

        return orderList.stream()
                .map(mapper::entityToDto)
                .toList();
    }

}

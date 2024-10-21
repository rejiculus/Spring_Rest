package com.example.rest.service.imp;

import com.example.rest.entity.Barista;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NullParamException;
import com.example.rest.entity.exception.OrderNotFoundException;
import com.example.rest.repository.OrderRepository;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.dto.IOrderCreateDTO;
import com.example.rest.service.dto.IOrderPublicDTO;
import com.example.rest.service.dto.IOrderUpdateDTO;
import com.example.rest.service.exception.OrderAlreadyCompletedException;
import com.example.rest.service.mapper.OrderMapper;
import com.example.rest.servlet.dto.OrderCreateDTO;
import com.example.rest.servlet.dto.OrderPublicDTO;
import com.example.rest.servlet.dto.OrderUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;

    private OrderService orderService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(orderRepository, orderMapper);
    }

    //constructor
    @Test
    void shouldReturnOrderService_WhenConstructorCorrectParams() {
        OrderService resultOrderService = new OrderService(orderRepository, orderMapper);

        assertNotNull(resultOrderService);
    }

    @Test
    void shouldThrowNullParamException_WhenConstructorNullParam() {
        assertThrows(NullParamException.class, () -> new OrderService(null, orderMapper));
        assertThrows(NullParamException.class, () -> new OrderService(orderRepository, null));
    }

    //create
    @Test
    void shouldCreateEntityByRepository_WhenCreateWithCorrectDto() {
        IOrderCreateDTO mockedCreateDto = Mockito.mock(OrderCreateDTO.class);
        Order mockedOrder = Mockito.mock(Order.class);
        Barista mockedBarista = Mockito.mock(Barista.class);
        OrderPublicDTO mockedPublicDto = Mockito.mock(OrderPublicDTO.class);

        Mockito.when(orderMapper.createDtoToEntity(mockedCreateDto))
                .thenReturn(mockedOrder);
        Mockito.when(mockedOrder.getBarista())
                .thenReturn(mockedBarista);
        Mockito.when(mockedBarista.getTipSize())
                .thenReturn(0.1);
        Mockito.when(mockedOrder.getCoffeeList())
                .thenReturn(List.of());
        Mockito.when(orderRepository.save(mockedOrder))
                .thenReturn(mockedOrder);
        Mockito.when(orderMapper.entityToDto(mockedOrder))
                .thenReturn(mockedPublicDto);


        IOrderPublicDTO resultPublicDto = orderService.create(mockedCreateDto);

        assertEquals(mockedPublicDto, resultPublicDto);
    }

    @Test
    void shouldThrowNullParamException_WhenCreateWithNullParam() {
        assertThrows(NullParamException.class, () -> orderService.create(null));
    }

    //update
    @Test
    void shouldUpdateEntityByRepository_WhenUpdateWithCorrectDto() {
        IOrderUpdateDTO mockedCreateDto = Mockito.mock(OrderUpdateDTO.class);
        Order mockedOrder = Mockito.mock(Order.class);
        Barista mockedBarista = Mockito.mock(Barista.class);
        OrderPublicDTO mockedPublicDto = Mockito.mock(OrderPublicDTO.class);

        Mockito.when(orderMapper.updateDtoToEntity(mockedCreateDto))
                .thenReturn(mockedOrder);
        Mockito.when(mockedOrder.getCoffeeList())
                .thenReturn(List.of());
        Mockito.when(mockedOrder.getBarista())
                .thenReturn(mockedBarista);
        Mockito.when(mockedBarista.getTipSize())
                .thenReturn(0.1);
        Mockito.when(orderRepository.save(mockedOrder))
                .thenReturn(mockedOrder);
        Mockito.when(mockedOrder.getId())
                .thenReturn(0L);
        Mockito.when(orderRepository.findById(0L))
                .thenReturn(Optional.of(mockedOrder));
        Mockito.when(orderMapper.entityToDto(mockedOrder))
                .thenReturn(mockedPublicDto);

        IOrderPublicDTO resultPublicDto = orderService.update(mockedCreateDto);

        assertEquals(mockedPublicDto, resultPublicDto);

    }

    @Test
    void shouldThrowNullParamException_WhenUpdateWithNullParam() {
        assertThrows(NullParamException.class, () -> orderService.update(null));
    }

    //delete
    @Test
    void shouldDeleteEntityByRepository_WhenDeleteWithCorrectId() {
        Long inputId = 99L;

        orderService.delete(inputId);

        Mockito.verify(orderRepository, Mockito.times(1)).deleteById(inputId);
    }

    @Test
    void shouldThrowNullParamException_WhenDeleteWithNullParam() {
        assertThrows(NullParamException.class, () -> orderService.delete(null));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -99L})
    void shouldThrowNoValidIdException_WhenDeleteWithLessThanZeroLong(Long id) {
        assertThrows(NoValidIdException.class, () -> orderService.delete(id));
    }

    //findById
    @Test
    void shouldReturnCorrectOrder_WhenFindByIdWithCorrectId() {
        Long inputId = 99L;
        Order mockedOrder = Mockito.mock(Order.class);
        OrderPublicDTO mockedPublicDto = Mockito.mock(OrderPublicDTO.class);

        Mockito.when(orderRepository.findById(inputId))
                .thenReturn(Optional.of(mockedOrder));
        Mockito.when(orderMapper.entityToDto(mockedOrder))
                .thenReturn(mockedPublicDto);


        IOrderPublicDTO resultPublicDto = orderService.findById(inputId);

        assertEquals(mockedPublicDto, resultPublicDto);
    }

    @Test
    void shouldThrowNullParamException_WhenFindByIdWithNullParam() {
        assertThrows(NullParamException.class, () -> orderService.findById(null));
    }

    @Test
    void shouldThrowNullParamException_WhenIdNotFound() {
        Long inputId = 99L;

        Mockito.when(orderRepository.findById(inputId))
                .thenReturn(Optional.empty());


        assertThrows(OrderNotFoundException.class, () -> orderService.findById(inputId));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -99L})
    void shouldThrowNoValidIdException_WhenFindByIdWithLessThanZeroLong(Long id) {
        assertThrows(NoValidIdException.class, () -> orderService.findById(id));
    }

    //findAll
    @Test
    void shouldReturnCorrectOrderList_WhenFindAll() {
        List<Order> mockedOrderList = List.of(
                Mockito.mock(Order.class),
                Mockito.mock(Order.class),
                Mockito.mock(Order.class)
        );
        List<OrderPublicDTO> mockedPublicDtoList = List.of(
                Mockito.mock(OrderPublicDTO.class),
                Mockito.mock(OrderPublicDTO.class),
                Mockito.mock(OrderPublicDTO.class)
        );

        Mockito.when(orderRepository.findAll())
                .thenReturn(mockedOrderList);
        for (int i = 0; i < mockedOrderList.size(); i++) {
            Mockito.when(orderMapper.entityToDto(mockedOrderList.get(i)))
                    .thenReturn(mockedPublicDtoList.get(i));
        }

        List<OrderPublicDTO> resultPublicDtoList = orderService.findAll();

        assertEquals(mockedPublicDtoList, resultPublicDtoList);

    }

    //findAllByPage
    @ParameterizedTest
    @CsvSource(value = {"0;2", "0;1", "1;1", "1;99", "99;99"}, delimiter = ';')
    void shouldReturnCorrectOrderList_WhenFindAllByPageCorrectParam(int page, int limit) {
        Pageable specifiedPageable = PageRequest.of(page, limit);
        Page<Order> mockedPage = Mockito.mock(Page.class);
        List<Order> mockedOrderList = List.of(
                Mockito.mock(Order.class),
                Mockito.mock(Order.class),
                Mockito.mock(Order.class)
        );
        List<OrderPublicDTO> mockedPublicDtoList = List.of(
                Mockito.mock(OrderPublicDTO.class),
                Mockito.mock(OrderPublicDTO.class),
                Mockito.mock(OrderPublicDTO.class)
        );

        Mockito.when(orderRepository.findAll(specifiedPageable))
                .thenReturn(mockedPage);
        Mockito.when(mockedPage.stream())
                .thenReturn(mockedOrderList.stream());
        for (int i = 0; i < mockedOrderList.size(); i++) {
            Mockito.when(orderMapper.entityToDto(mockedOrderList.get(i)))
                    .thenReturn(mockedPublicDtoList.get(i));
        }

        List<OrderPublicDTO> resultPublicDtoList = orderService.findAllByPage(page, limit);

        assertEquals(mockedPublicDtoList, resultPublicDtoList);
    }

    @ParameterizedTest
    @CsvSource(value = {"-1;2", "-99;1"}, delimiter = ';')
    void shouldThrowNoValidPageException_WhenFindAllByPageLessZeroPage(int page, int limit) {
        assertThrows(NoValidPageException.class, () -> orderService.findAllByPage(page, limit));
    }

    @ParameterizedTest
    @CsvSource(value = {"0;0", "0;-1"}, delimiter = ';')
    void shouldThrowNoValidLimitException_WhenFindAllByPageLessOneLimit(int page, int limit) {
        assertThrows(NoValidLimitException.class, () -> orderService.findAllByPage(page, limit));
    }

    //getOrderQueue
    @Test
    void shouldReturnCorrectOrderList_WhenGetOrderQueue() {

        Mockito.when(orderRepository.findAll())
                .thenReturn(List.of());

        List<OrderPublicDTO> resultPublicDtoList = orderService.getOrderQueue();

        assertEquals(List.of(), resultPublicDtoList);

    }

    //completeOrder
    @Test
    void shouldReturnCompletedOrder_WhenNotCompletedOrder() {
        Long inputId = 99L;
        Order mockedOrder = Mockito.mock(Order.class);
        OrderPublicDTO mockedPublicDto = Mockito.mock(OrderPublicDTO.class);

        Mockito.when(orderRepository.findById(inputId))
                .thenReturn(Optional.of(mockedOrder));
        Mockito.when(mockedOrder.getCompleted())
                .thenReturn(null);
        Mockito.when(orderRepository.save(mockedOrder))
                .thenReturn(mockedOrder);
        Mockito.when(orderMapper.entityToDto(mockedOrder))
                .thenReturn(mockedPublicDto);


        IOrderPublicDTO resultPublicDto = orderService.completeOrder(inputId);

        assertEquals(mockedPublicDto, resultPublicDto);
        Mockito.verify(mockedOrder, Mockito.times(1)).setCompleted(any());
    }

    @Test
    void shouldThrowNullParamException_WhenCompleteNullParam() {
        assertThrows(NullParamException.class, () -> orderService.completeOrder(null));
    }

    @Test
    void shouldThrowNullParamException_WhenCompleteIdNotFound() {
        Long inputId = 99L;

        Mockito.when(orderRepository.findById(inputId))
                .thenReturn(Optional.empty());


        assertThrows(OrderNotFoundException.class, () -> orderService.completeOrder(inputId));
    }

    @Test
    void shouldThrowOrderAlreadyCompletedException_WhenOrderCompleted() {
        Long inputId = 99L;
        Order mockedOrder = Mockito.mock(Order.class);

        Mockito.when(orderRepository.findById(inputId))
                .thenReturn(Optional.of(mockedOrder));
        Mockito.when(mockedOrder.getCompleted())
                .thenReturn(LocalDateTime.now());


        assertThrows(OrderAlreadyCompletedException.class, () -> orderService.completeOrder(inputId));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -99L})
    void shouldThrowNoValidIdException_WhenCompleteWithLessThanZeroLong(Long id) {
        assertThrows(NoValidIdException.class, () -> orderService.completeOrder(id));
    }

}
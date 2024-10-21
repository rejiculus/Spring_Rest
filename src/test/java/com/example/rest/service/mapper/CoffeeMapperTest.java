package com.example.rest.service.mapper;

import com.example.rest.entity.Barista;
import com.example.rest.entity.Coffee;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NoValidNameException;
import com.example.rest.entity.exception.NoValidPriceException;
import com.example.rest.entity.exception.OrderNotFoundException;
import com.example.rest.repository.OrderRepository;
import com.example.rest.service.exception.DuplicatedElementsException;
import com.example.rest.servlet.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CoffeeMapperTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private CoffeeMapper coffeeMapper = new CoffeeMapperImpl();


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDtoToEntity_WhenCorrectDto_ShouldReturnEntity() {
        String expectedName = "QQQ";
        Double expectedPrice = 999.0;

        CoffeeCreateDTO coffeeCreateDTO = new CoffeeCreateDTO(expectedName, expectedPrice);

        Coffee resultCoffee = coffeeMapper.createDtoToEntity(coffeeCreateDTO);

        assertEquals(expectedName, resultCoffee.getName());
        assertEquals(expectedPrice, resultCoffee.getPrice());
    }

    @Test
    void createDtoToEntity_WhenEmptyName_ShouldThrowNoValidNameException() {
        String expectedName = "";
        Double expectedPrice = 999.0;

        CoffeeCreateDTO coffeeCreateDTO = new CoffeeCreateDTO(expectedName, expectedPrice);

        assertThrows(NoValidNameException.class, () -> coffeeMapper.createDtoToEntity(coffeeCreateDTO));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    void createDtoToEntity_WhenWrongPrice_ShouldThrowNoValidPriceException(Double price) {
        String expectedName = "QQQ";

        CoffeeCreateDTO coffeeCreateDTO = new CoffeeCreateDTO(expectedName, price);

        assertThrows(NoValidPriceException.class, () -> coffeeMapper.createDtoToEntity(coffeeCreateDTO));
    }

    @Test
    void updateDtoToEntity_WhenCorrectDto_ShouldReturnEntity() {
        Long expectedId = 99L;
        String expectedName = "QQQ";
        Double expectedPrice = 999.0;
        Order specifiedOrder = new Order(0L, new Barista(), List.of(), LocalDateTime.MIN, null, 0.0);
        List<Order> expectedOrderList = List.of(specifiedOrder);
        List<Long> specifiedOrderIdList = List.of(0L);
        CoffeeUpdateDTO coffeeUpdateDTO = new CoffeeUpdateDTO(expectedId, expectedName, expectedPrice, specifiedOrderIdList);

        Mockito.when(orderRepository.findAllById(specifiedOrderIdList))
                .thenReturn(expectedOrderList);

        Coffee resultCoffee = coffeeMapper.updateDtoToEntity(coffeeUpdateDTO);

        assertEquals(expectedId, resultCoffee.getId());
        assertEquals(expectedName, resultCoffee.getName());
        assertEquals(expectedPrice, resultCoffee.getPrice());
        assertEquals(expectedOrderList, resultCoffee.getOrderList());
    }

    @Test
    void updateDtoToEntity_WhenWrongId_ShouldThrowNoValidIdException() {
        Long expectedId = -99L;
        String expectedName = "QQQ";
        Double expectedPrice = 999.0;
        Order specifiedOrder = new Order(0L, new Barista(), List.of(), LocalDateTime.MIN, null, 0.0);
        List<Order> expectedOrderList = List.of(specifiedOrder);
        List<Long> specifiedOrderIdList = List.of(0L);
        CoffeeUpdateDTO coffeeUpdateDTO = new CoffeeUpdateDTO(expectedId, expectedName, expectedPrice, specifiedOrderIdList);

        Mockito.when(orderRepository.findAllById(specifiedOrderIdList))
                .thenReturn(expectedOrderList);

        assertThrows(NoValidIdException.class, () -> coffeeMapper.updateDtoToEntity(coffeeUpdateDTO));
    }

    @Test
    void updateDtoToEntity_WhenNotFoundOrderId_ShouldThrowOrderNotFoundException() {
        Long expectedId = 99L;
        String expectedName = "QQQ";
        Double expectedPrice = 999.0;
        List<Long> specifiedOrderIdList = List.of(-1L);
        CoffeeUpdateDTO coffeeUpdateDTO = new CoffeeUpdateDTO(expectedId, expectedName, expectedPrice, specifiedOrderIdList);

        Mockito.when(orderRepository.findAllById(specifiedOrderIdList))
                .thenReturn(List.of());

        assertThrows(OrderNotFoundException.class, () -> coffeeMapper.updateDtoToEntity(coffeeUpdateDTO));
    }

    @Test
    void updateDtoToEntity_WhenDuplicateOrderId_ShouldThrowDuplicatedElementsException() {
        Long expectedId = 99L;
        String expectedName = "QQQ";
        Double expectedPrice = 999.0;
        List<Long> specifiedOrderIdList = List.of(1L, 1L);
        CoffeeUpdateDTO coffeeUpdateDTO = new CoffeeUpdateDTO(expectedId, expectedName, expectedPrice, specifiedOrderIdList);

        Mockito.when(orderRepository.findAllById(specifiedOrderIdList))
                .thenReturn(List.of());

        assertThrows(DuplicatedElementsException.class, () -> coffeeMapper.updateDtoToEntity(coffeeUpdateDTO));
    }

    @Test
    void updateDtoToEntity_WhenEmptyName_ShouldThrowNoValidNameException() {
        Long expectedId = 99L;
        String expectedName = "";
        Double expectedPrice = 999.0;
        Order specifiedOrder = new Order(0L, new Barista(), List.of(), LocalDateTime.MIN, null, 0.0);
        List<Order> expectedOrderList = List.of(specifiedOrder);
        List<Long> specifiedOrderIdList = List.of(0L);
        CoffeeUpdateDTO coffeeUpdateDTO = new CoffeeUpdateDTO(expectedId, expectedName, expectedPrice, specifiedOrderIdList);

        Mockito.when(orderRepository.findAllById(specifiedOrderIdList))
                .thenReturn(expectedOrderList);

        assertThrows(NoValidNameException.class, () -> coffeeMapper.updateDtoToEntity(coffeeUpdateDTO));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    void updateDtoToEntity_WhenWrongPrice_ShouldThrowNoValidPriceException(Double price) {
        Long expectedId = 99L;
        String expectedName = "QQQ";
        List<Long> specifiedOrderIdList = List.of(-1L);
        CoffeeUpdateDTO coffeeUpdateDTO = new CoffeeUpdateDTO(expectedId, expectedName, price, specifiedOrderIdList);

        Mockito.when(orderRepository.findAllById(specifiedOrderIdList))
                .thenReturn(List.of());

        assertThrows(NoValidPriceException.class, () -> coffeeMapper.updateDtoToEntity(coffeeUpdateDTO));
    }


    @Test
    void entityToDto_WhenCorrectEntity_ShouldReturnDto() {
        Long expectedId = 99L;
        String expectedName = "QQQ";
        Double expectedPrice = 999.0;
        Barista specifiedBarista = new Barista(0L, "John Doe", List.of(), 0.1);
        Order specifiedOrder = new Order(0L, specifiedBarista, List.of(), LocalDateTime.MIN, null, 0.0);
        List<Order> specifiedOrderList = List.of(specifiedOrder);
        List<OrderNoRefDTO> expectedOrderIdList = List.of(
                new OrderNoRefDTO(specifiedOrder.getId(),
                        specifiedOrder.getBarista().getId(),
                        specifiedOrder.getCreated(),
                        specifiedOrder.getCompleted(),
                        specifiedOrder.getPrice())
        );

        Coffee specifiedCoffee = new Coffee(expectedId, expectedName, expectedPrice, specifiedOrderList);

        CoffeePublicDTO resultPublicDto = coffeeMapper.entityToDto(specifiedCoffee);

        assertEquals(expectedId, resultPublicDto.id());
        assertEquals(expectedName, resultPublicDto.name());
        assertEquals(expectedPrice, resultPublicDto.price());
        assertEquals(expectedOrderIdList, resultPublicDto.orders());
    }

    @Test
    void entityToNoRefDto_WhenCorrectEntity_ShouldReturnDto() {
        Long expectedId = 99L;
        String expectedName = "QQQ";
        Double expectedPrice = 999.0;
        Barista specifiedBarista = new Barista(0L, "John Doe", List.of(), 0.1);
        Order specifiedOrder = new Order(0L, specifiedBarista, List.of(), LocalDateTime.MIN, null, 0.0);
        List<Order> specifiedOrderList = List.of(specifiedOrder);


        Coffee specifiedCoffee = new Coffee(expectedId, expectedName, expectedPrice, specifiedOrderList);

        CoffeeNoRefDTO resultNoRefDto = coffeeMapper.entityToNoRefDto(specifiedCoffee);

        assertEquals(expectedId, resultNoRefDto.id());
        assertEquals(expectedName, resultNoRefDto.name());
        assertEquals(expectedPrice, resultNoRefDto.price());
    }
}
package com.example.rest.service.mapper;

import com.example.rest.entity.Barista;
import com.example.rest.entity.Coffee;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.BaristaNotFoundException;
import com.example.rest.entity.exception.CoffeeNotFoundException;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NoValidPriceException;
import com.example.rest.repository.BaristaRepository;
import com.example.rest.repository.CoffeeRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class OrderMapperTest {
    @Mock
    protected CoffeeRepository coffeeRepository;
    @Mock
    protected CoffeeMapper coffeeMapper;
    @Mock
    protected BaristaRepository baristaRepository;
    @Mock
    protected BaristaMapper baristaMapper;

    @InjectMocks
    private OrderMapper orderMapper = new OrderMapperImpl();


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createDtoToEntity_WhenCorrectDto_ShouldReturnEntity() {
        Long expectedBaristaId = 0L;
        List<Long> expectedCoffeeIdList = List.of(1L, 2L);
        Barista mockedBarista = Mockito.mock(Barista.class);
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );

        OrderCreateDTO orderCreateDTO = new OrderCreateDTO(expectedBaristaId, expectedCoffeeIdList);

        Mockito.when(baristaRepository.findById(expectedBaristaId))
                .thenReturn(Optional.of(mockedBarista));
        Mockito.when(coffeeRepository.findAllById(expectedCoffeeIdList))
                .thenReturn(mockedCoffeeList);

        Order resultOrder = orderMapper.createDtoToEntity(orderCreateDTO);

        assertEquals(mockedBarista, resultOrder.getBarista());
        assertEquals(mockedCoffeeList, resultOrder.getCoffeeList());
    }

    @Test
    void createDtoToEntity_WhenBaristaNotFound_ShouldThrowBaristaNotFoundException() {
        Long expectedBaristaId = 0L;
        List<Long> expectedCoffeeIdList = List.of(1L, 2L);
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );

        OrderCreateDTO orderCreateDTO = new OrderCreateDTO(expectedBaristaId, expectedCoffeeIdList);

        Mockito.when(baristaRepository.findById(expectedBaristaId))
                .thenReturn(Optional.empty());
        Mockito.when(coffeeRepository.findAllById(expectedCoffeeIdList))
                .thenReturn(mockedCoffeeList);

        assertThrows(BaristaNotFoundException.class, () -> orderMapper.createDtoToEntity(orderCreateDTO));
    }

    @Test
    void createDtoToEntity_WhenBaristaNotFound_ShouldThrowCoffeeNotFoundException() {
        Long expectedBaristaId = 0L;
        List<Long> expectedCoffeeIdList = List.of(1L, 2L);
        Barista mockedBarista = Mockito.mock(Barista.class);
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO(expectedBaristaId, expectedCoffeeIdList);

        Mockito.when(baristaRepository.findById(expectedBaristaId))
                .thenReturn(Optional.of(mockedBarista));
        Mockito.when(coffeeRepository.findAllById(expectedCoffeeIdList))
                .thenReturn(List.of());

        assertThrows(CoffeeNotFoundException.class, () -> orderMapper.createDtoToEntity(orderCreateDTO));
    }

    @Test
    void updateDtoToEntity_WhenCorrectDto_ShouldReturnEntity() {
        Long expectedId = 0L;
        Long expectedBaristaId = 0L;
        LocalDateTime expectedCreated = LocalDateTime.MIN;
        LocalDateTime expectedCompleted = LocalDateTime.MAX;
        Double expectedPrice = 0.0;
        List<Long> expectedCoffeeIdList = List.of(1L, 2L);
        Barista mockedBarista = Mockito.mock(Barista.class);
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );

        OrderUpdateDTO orderUpdateDTO = new OrderUpdateDTO(
                expectedId,
                expectedBaristaId,
                expectedCreated,
                expectedCompleted,
                expectedPrice,
                expectedCoffeeIdList
        );

        Mockito.when(baristaRepository.findById(expectedBaristaId))
                .thenReturn(Optional.of(mockedBarista));
        Mockito.when(coffeeRepository.findAllById(expectedCoffeeIdList))
                .thenReturn(mockedCoffeeList);

        Order resultOrder = orderMapper.updateDtoToEntity(orderUpdateDTO);

        assertEquals(mockedBarista, resultOrder.getBarista());
        assertEquals(mockedCoffeeList, resultOrder.getCoffeeList());
        assertEquals(expectedId, resultOrder.getId());
        assertEquals(expectedCreated, resultOrder.getCreated());
        assertEquals(expectedCompleted, resultOrder.getCompleted());
        assertEquals(expectedPrice, resultOrder.getPrice());
    }

    @Test
    void updateDtoToEntity_WhenWrongId_ShouldThrowNoValidIdException() {
        Long expectedId = -1L;
        Long expectedBaristaId = 0L;
        LocalDateTime expectedCreated = LocalDateTime.MIN;
        LocalDateTime expectedCompleted = LocalDateTime.MAX;
        Double expectedPrice = 0.0;
        List<Long> expectedCoffeeIdList = List.of(1L, 2L);

        OrderUpdateDTO orderUpdateDTO = new OrderUpdateDTO(
                expectedId,
                expectedBaristaId,
                expectedCreated,
                expectedCompleted,
                expectedPrice,
                expectedCoffeeIdList
        );

        assertThrows(NoValidIdException.class, () -> orderMapper.updateDtoToEntity(orderUpdateDTO));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    void updateDtoToEntity_WhenWrongPrice_ShouldThrowNoValidPriceException(Double price) {
        Long expectedId = 1L;
        Long expectedBaristaId = 0L;
        LocalDateTime expectedCreated = LocalDateTime.MIN;
        LocalDateTime expectedCompleted = LocalDateTime.MAX;
        List<Long> expectedCoffeeIdList = List.of(1L, 2L);
        Barista mockedBarista = Mockito.mock(Barista.class);
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );

        OrderUpdateDTO orderUpdateDTO = new OrderUpdateDTO(
                expectedId,
                expectedBaristaId,
                expectedCreated,
                expectedCompleted,
                price,
                expectedCoffeeIdList
        );
        Mockito.when(baristaRepository.findById(expectedBaristaId))
                .thenReturn(Optional.of(mockedBarista));
        Mockito.when(coffeeRepository.findAllById(expectedCoffeeIdList))
                .thenReturn(mockedCoffeeList);

        assertThrows(NoValidPriceException.class, () -> orderMapper.updateDtoToEntity(orderUpdateDTO));
    }

    @Test
    void entityToDto_WhenCorrectEntity_ShouldReturnDto() {
        Long expectedId = 0L;
        LocalDateTime expectedCreated = LocalDateTime.MIN;
        LocalDateTime expectedCompleted = LocalDateTime.MAX;
        Double expectedPrice = 0.0;
        Barista specifiedBarista = new Barista(0L, "Name", List.of(), 0.0);
        List<Coffee> specifiedCoffeeList = List.of(
                new Coffee(0L, "naem", 0.0, List.of()),
                new Coffee(0L, "naem", 0.0, List.of())
        );
        BaristaNoRefDTO mockedBaristaDto = Mockito.mock(BaristaNoRefDTO.class);
        CoffeeNoRefDTO mockedCoffeeDto = Mockito.mock(CoffeeNoRefDTO.class);


        Order specifiedOrder = new Order(
                expectedId,
                specifiedBarista,
                specifiedCoffeeList,
                LocalDateTime.MIN,
                LocalDateTime.MAX,
                0.0
        );

        Mockito.when(baristaMapper.entityToNoRefDto(specifiedBarista))
                .thenReturn(mockedBaristaDto);
        Mockito.when(coffeeMapper.entityToNoRefDto(any()))
                .thenReturn(mockedCoffeeDto);

        OrderPublicDTO resultPublicDto = orderMapper.entityToDto(specifiedOrder);

        assertEquals(mockedBaristaDto, resultPublicDto.baristaId());
        assertEquals(List.of(mockedCoffeeDto, mockedCoffeeDto), resultPublicDto.coffees());
        assertEquals(expectedId, resultPublicDto.id());
        assertEquals(expectedCreated, resultPublicDto.created());
        assertEquals(expectedCompleted, resultPublicDto.completed());
        assertEquals(expectedPrice, resultPublicDto.price());
    }
}
package com.example.rest.service.imp;

import com.example.rest.entity.Coffee;
import com.example.rest.entity.exception.CoffeeNotFoundException;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NullParamException;
import com.example.rest.repository.CoffeeRepository;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.dto.ICoffeeCreateDTO;
import com.example.rest.service.dto.ICoffeePublicDTO;
import com.example.rest.service.dto.ICoffeeUpdateDTO;
import com.example.rest.service.mapper.CoffeeMapper;
import com.example.rest.servlet.dto.CoffeeCreateDTO;
import com.example.rest.servlet.dto.CoffeePublicDTO;
import com.example.rest.servlet.dto.CoffeeUpdateDTO;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CoffeeServiceTest {
    @Mock
    private CoffeeRepository coffeeRepository;
    @Mock
    private CoffeeMapper coffeeMapper;

    private CoffeeService coffeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        coffeeService = new CoffeeService(coffeeRepository, coffeeMapper);
    }

    //constructor
    @Test
    void shouldReturnCoffeeService_WhenConstructorCorrectParams() {
        CoffeeService resultCoffeeService = new CoffeeService(coffeeRepository, coffeeMapper);

        assertNotNull(resultCoffeeService);
    }

    @Test
    void shouldThrowNullParamException_WhenConstructorNullParam() {
        assertThrows(NullParamException.class, () -> new CoffeeService(null, coffeeMapper));
        assertThrows(NullParamException.class, () -> new CoffeeService(coffeeRepository, null));
    }


    //create
    @Test
    void shouldCreateEntityByRepository_WhenCreateWithCorrectDto() {
        ICoffeeCreateDTO mockedCreateDto = Mockito.mock(CoffeeCreateDTO.class);
        Coffee mockedCoffee = Mockito.mock(Coffee.class);
        CoffeePublicDTO mockedPublicDto = Mockito.mock(CoffeePublicDTO.class);

        Mockito.when(coffeeMapper.createDtoToEntity(mockedCreateDto))
                .thenReturn(mockedCoffee);
        Mockito.when(coffeeRepository.save(mockedCoffee))
                .thenReturn(mockedCoffee);
        Mockito.when(coffeeMapper.entityToDto(mockedCoffee))
                .thenReturn(mockedPublicDto);

        ICoffeePublicDTO resultPublicDto = coffeeService.create(mockedCreateDto);

        assertEquals(mockedPublicDto, resultPublicDto);
    }

    @Test
    void shouldThrowNullParamException_WhenCreateWithNullParam() {
        assertThrows(NullParamException.class, () -> coffeeService.create(null));
    }

    //update
    @Test
    void shouldUpdateEntityByRepository_WhenUpdateWithCorrectDto() {
        ICoffeeUpdateDTO mockedUpdateDto = Mockito.mock(CoffeeUpdateDTO.class);
        Coffee mockedCoffee = Mockito.mock(Coffee.class);
        CoffeePublicDTO mockedPublicDto = Mockito.mock(CoffeePublicDTO.class);

        Mockito.when(coffeeMapper.updateDtoToEntity(mockedUpdateDto))
                .thenReturn(mockedCoffee);
        Mockito.when(coffeeRepository.save(mockedCoffee))
                .thenReturn(mockedCoffee);
        Mockito.when(mockedCoffee.getId())
                .thenReturn(0L);
        Mockito.when(coffeeRepository.findById(0L))
                .thenReturn(Optional.of(mockedCoffee));
        Mockito.when(coffeeMapper.entityToDto(mockedCoffee))
                .thenReturn(mockedPublicDto);

        ICoffeePublicDTO resultPublicDto = coffeeService.update(mockedUpdateDto);

        assertEquals(mockedPublicDto, resultPublicDto);
    }

    @Test
    void shouldThrowNullParamException_WhenUpdateWithNullParam() {
        assertThrows(NullParamException.class, () -> coffeeService.update(null));
    }

    //delete
    @Test
    void shouldDeleteEntityByRepository_WhenDeleteWithCorrectId() {
        Long inputId = 99L;

        coffeeService.delete(inputId);

        Mockito.verify(coffeeRepository, Mockito.times(1)).deleteById(inputId);
    }

    @Test
    void shouldThrowNullParamException_WhenDeleteWithNullParam() {
        assertThrows(NullParamException.class, () -> coffeeService.delete(null));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -99L})
    void shouldThrowNoValidIdException_WhenDeleteWithLessThanZeroLong(Long id) {
        assertThrows(NoValidIdException.class, () -> coffeeService.delete(id));
    }

    //findById
    @Test
    void shouldReturnCorrectOrder_WhenFindByIdWithCorrectId() {
        Long inputId = 99L;
        Coffee mockedCoffee = Mockito.mock(Coffee.class);
        CoffeePublicDTO mockedPublicDto = Mockito.mock(CoffeePublicDTO.class);

        Mockito.when(coffeeRepository.findById(inputId))
                .thenReturn(Optional.of(mockedCoffee));
        Mockito.when(coffeeMapper.entityToDto(mockedCoffee))
                .thenReturn(mockedPublicDto);

        ICoffeePublicDTO resultPublicDto = coffeeService.findById(inputId);

        assertEquals(mockedPublicDto, resultPublicDto);
    }

    @Test
    void shouldThrowNullParamException_WhenFindByIdWithNullParam() {
        assertThrows(NullParamException.class, () -> coffeeService.findById(null));
    }

    @Test
    void shouldThrowNullParamException_WhenIdNotFound() {
        Long inputId = 99L;

        Mockito.when(coffeeRepository.findById(inputId))
                .thenReturn(Optional.empty());


        assertThrows(CoffeeNotFoundException.class, () -> coffeeService.findById(inputId));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -99L})
    void shouldThrowNoValidIdException_WhenFindByIdWithLessThanZeroLong(Long id) {
        assertThrows(NoValidIdException.class, () -> coffeeService.findById(id));
    }

    //findAll
    @Test
    void shouldReturnCorrectOrderList_WhenFindAll() {
        List<Coffee> mockedOrderList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        List<CoffeePublicDTO> mockedPublicDtoList = List.of(
                Mockito.mock(CoffeePublicDTO.class),
                Mockito.mock(CoffeePublicDTO.class),
                Mockito.mock(CoffeePublicDTO.class)
        );

        Mockito.when(coffeeRepository.findAll())
                .thenReturn(mockedOrderList);
        for (int i = 0; i < mockedOrderList.size(); i++) {
            Mockito.when(coffeeMapper.entityToDto(mockedOrderList.get(i)))
                    .thenReturn(mockedPublicDtoList.get(i));
        }

        List<CoffeePublicDTO> resultPublicDtoList = coffeeService.findAll();

        assertEquals(mockedPublicDtoList, resultPublicDtoList);
    }

    //findAllByPage
    @ParameterizedTest
    @CsvSource(value = {"0;2", "0;1", "1;1", "1;99", "99;99"}, delimiter = ';')
    void shouldReturnCorrectOrderList_WhenFindAllByPageCorrectParam(int page, int limit) {
        Pageable specifiedPageable = PageRequest.of(page, limit);
        Page<Coffee> mockedPage = Mockito.mock(Page.class);
        List<Coffee> mockedOrderList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        List<CoffeePublicDTO> mockedPublicDtoList = List.of(
                Mockito.mock(CoffeePublicDTO.class),
                Mockito.mock(CoffeePublicDTO.class),
                Mockito.mock(CoffeePublicDTO.class)
        );

        Mockito.when(coffeeRepository.findAll(specifiedPageable))
                .thenReturn(mockedPage);
        Mockito.when(mockedPage.stream())
                .thenReturn(mockedOrderList.stream());
        for (int i = 0; i < mockedOrderList.size(); i++) {
            Mockito.when(coffeeMapper.entityToDto(mockedOrderList.get(i)))
                    .thenReturn(mockedPublicDtoList.get(i));
        }

        List<CoffeePublicDTO> resultPublicDtoList = coffeeService.findAllByPage(page, limit);

        assertEquals(mockedPublicDtoList, resultPublicDtoList);
    }

    @ParameterizedTest
    @CsvSource(value = {"-1;2", "-99;1"}, delimiter = ';')
    void shouldThrowNoValidPageException_WhenFindAllByPageLessZeroPage(int page, int limit) {
        assertThrows(NoValidPageException.class, () -> coffeeService.findAllByPage(page, limit));
    }

    @ParameterizedTest
    @CsvSource(value = {"0;0", "0;-1"}, delimiter = ';')
    void shouldThrowNoValidLimitException_WhenFindAllByPageLessOneLimit(int page, int limit) {
        assertThrows(NoValidLimitException.class, () -> coffeeService.findAllByPage(page, limit));
    }


}
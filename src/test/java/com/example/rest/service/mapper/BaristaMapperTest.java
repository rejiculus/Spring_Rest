package com.example.rest.service.mapper;

import com.example.rest.entity.Barista;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NoValidNameException;
import com.example.rest.entity.exception.NoValidTipSizeException;
import com.example.rest.servlet.dto.BaristaCreateDTO;
import com.example.rest.servlet.dto.BaristaNoRefDTO;
import com.example.rest.servlet.dto.BaristaPublicDTO;
import com.example.rest.servlet.dto.BaristaUpdateDTO;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BaristaMapperTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private BaristaMapper baristaMapper = new BaristaMapperImpl();


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createDtoToEntity_WhenCorrectCreateDto_ShouldReturnEntity() {
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.99;
        BaristaCreateDTO specifiedCreateDto = new BaristaCreateDTO(expectedFullName, expectedTipSize);

        Barista resultBarista = baristaMapper.createDtoToEntity(specifiedCreateDto);

        assertEquals(expectedFullName, resultBarista.getFullName());
        assertEquals(expectedTipSize, resultBarista.getTipSize());
    }

    @Test
    void createDtoToEntity_WhenEmptyName_ShouldThrowNoValidNameException() {
        String expectedFullName = "";
        Double expectedTipSize = 0.99;
        BaristaCreateDTO specifiedCreateDto = new BaristaCreateDTO(expectedFullName, expectedTipSize);

        assertThrows(NoValidNameException.class, () -> baristaMapper.createDtoToEntity(specifiedCreateDto));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    void createDtoToEntity_WhenWrongTipSize_ShouldThrowNoValidTipSizeException(Double tipSize) {
        String expectedFullName = "John Doe";
        BaristaCreateDTO specifiedCreateDto = new BaristaCreateDTO(expectedFullName, tipSize);

        assertThrows(NoValidTipSizeException.class, () -> baristaMapper.createDtoToEntity(specifiedCreateDto));
    }

    //updateDtoToEntity

    @Test
    void updateDtoToEntity_WhenCorrectDto_ShouldReturnEntity() {
        Long expectedId = 99L;
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.99;
        BaristaUpdateDTO specifiedUpdateDto = new BaristaUpdateDTO(expectedId, expectedFullName, expectedTipSize);

        Barista resultBarista = baristaMapper.updateDtoToEntity(specifiedUpdateDto);

        assertEquals(expectedId, resultBarista.getId());
        assertEquals(expectedFullName, resultBarista.getFullName());
        assertEquals(expectedTipSize, resultBarista.getTipSize());
    }

    @Test
    void updateDtoToEntity_WhenWrongId_ShouldThrowNoValidIdException() {
        Long expectedId = -99L;
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.99;
        BaristaUpdateDTO specifiedUpdateDto = new BaristaUpdateDTO(expectedId, expectedFullName, expectedTipSize);

        assertThrows(NoValidIdException.class, () -> baristaMapper.updateDtoToEntity(specifiedUpdateDto));
    }

    @Test
    void updateDtoToEntity_WhenEmptyName_ShouldThrowNoValidNamedException() {
        Long expectedId = 99L;
        String expectedFullName = "";
        Double expectedTipSize = 0.99;
        BaristaUpdateDTO specifiedUpdateDto = new BaristaUpdateDTO(expectedId, expectedFullName, expectedTipSize);

        assertThrows(NoValidNameException.class, () -> baristaMapper.updateDtoToEntity(specifiedUpdateDto));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    void updateDtoToEntity_WhenWrongTipSize_ShouldThrowNoValidTipSizeException(Double tipSize) {
        Long expectedId = 99L;
        String expectedFullName = "John Doe";
        BaristaUpdateDTO specifiedUpdateDto = new BaristaUpdateDTO(expectedId, expectedFullName, tipSize);

        assertThrows(NoValidTipSizeException.class, () -> baristaMapper.updateDtoToEntity(specifiedUpdateDto));
    }


    @Test
    void entityToDto_WhenCorrectEntity_ShouldReturnDto() {
        Long expectedId = 99L;
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.99;
        Barista specifiedBarista = new Barista(expectedId, expectedFullName, List.of(), expectedTipSize);
        List<Order> orderList = List.of(
                new Order(0L, specifiedBarista, List.of(), LocalDateTime.MIN, null, 0.0)
        );
        specifiedBarista.setOrderList(orderList);

        BaristaPublicDTO resultBaristaDTO = baristaMapper.entityToDto(specifiedBarista);

        assertEquals(expectedId, resultBaristaDTO.id());
        assertEquals(expectedFullName, resultBaristaDTO.fullName());
        assertEquals(expectedTipSize, resultBaristaDTO.tipSize());
        assertEquals(1, resultBaristaDTO.orders().size());
        assertEquals(0L, resultBaristaDTO.orders().getFirst().id());
    }

    @Test
    void entityToNoRefDto_WhenCorrectEntity_ShouldReturnDto() {
        Long expectedId = 99L;
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.99;
        Barista specifiedBarista = new Barista(expectedId, expectedFullName, List.of(), expectedTipSize);
        List<Order> orderList = List.of(
                new Order(0L, specifiedBarista, List.of(), LocalDateTime.MIN, null, 0.0)
        );
        specifiedBarista.setOrderList(orderList);

        BaristaNoRefDTO resultBaristaDTO = baristaMapper.entityToNoRefDto(specifiedBarista);

        assertEquals(expectedId, resultBaristaDTO.id());
        assertEquals(expectedFullName, resultBaristaDTO.fullName());
        assertEquals(expectedTipSize, resultBaristaDTO.tipSize());
    }
}
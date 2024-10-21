package com.example.rest.service.imp;

import com.example.rest.entity.Barista;
import com.example.rest.entity.exception.BaristaNotFoundException;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NullParamException;
import com.example.rest.repository.BaristaRepository;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.dto.IBaristaCreateDTO;
import com.example.rest.service.dto.IBaristaPublicDTO;
import com.example.rest.service.dto.IBaristaUpdateDTO;
import com.example.rest.service.mapper.BaristaMapper;
import com.example.rest.servlet.dto.BaristaCreateDTO;
import com.example.rest.servlet.dto.BaristaPublicDTO;
import com.example.rest.servlet.dto.BaristaUpdateDTO;
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

class BaristaServiceTest {
    @Mock
    private static BaristaRepository baristaRepository;
    @Mock
    private static BaristaMapper baristaMapper;

    private static BaristaService baristaService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        baristaService = new BaristaService(baristaRepository, baristaMapper);
    }

    //constructor
    @Test
    void shouldReturnBaristaService_WhenCorrectParams() {
        BaristaService resultBaristaService = new BaristaService(baristaRepository, baristaMapper);

        assertNotNull(resultBaristaService);
    }

    @Test
    void shouldThrowNullParamException_WhenConstructorNullParam() {
        assertThrows(NullParamException.class, () -> new BaristaService(null, baristaMapper));
        assertThrows(NullParamException.class, () -> new BaristaService(baristaRepository, null));
    }

    //create
    @Test
    void shouldCreateEntityByRepository_WhenCreateWithCorrectDto() {
        IBaristaCreateDTO mockedCreateDto = Mockito.mock(BaristaCreateDTO.class);
        Barista mockedBarista = Mockito.mock(Barista.class);
        BaristaPublicDTO mockedPublicDto = Mockito.mock(BaristaPublicDTO.class);

        Mockito.when(baristaMapper.createDtoToEntity(mockedCreateDto))
                .thenReturn(mockedBarista);
        Mockito.when(baristaRepository.save(mockedBarista))
                .thenReturn(mockedBarista);
        Mockito.when(baristaMapper.entityToDto(mockedBarista))
                .thenReturn(mockedPublicDto);

        IBaristaPublicDTO resultPublicDto = baristaService.create(mockedCreateDto);

        assertEquals(mockedPublicDto, resultPublicDto);
    }

    @Test
    void shouldThrowNullParamException_WhenCreateWithNullParam() {
        assertThrows(NullParamException.class, () -> baristaService.create(null));
    }

    //update
    @Test
    void shouldUpdateEntityByRepository_WhenUpdateWithCorrectDto() {
        IBaristaUpdateDTO mockedUpdateDto = Mockito.mock(BaristaUpdateDTO.class);
        Barista mockedBarista = Mockito.mock(Barista.class);
        BaristaPublicDTO mockedPublicDto = Mockito.mock(BaristaPublicDTO.class);

        Mockito.when(baristaMapper.updateDtoToEntity(mockedUpdateDto))
                .thenReturn(mockedBarista);
        Mockito.when(baristaRepository.save(mockedBarista))
                .thenReturn(mockedBarista);
        Mockito.when(baristaMapper.entityToDto(mockedBarista))
                .thenReturn(mockedPublicDto);

        IBaristaPublicDTO resultPublicDto = baristaService.update(mockedUpdateDto);

        assertEquals(mockedPublicDto, resultPublicDto);
    }

    @Test
    void shouldThrowNullParamException_WhenUpdateWithNullParam() {
        assertThrows(NullParamException.class, () -> baristaService.update(null));
    }

    //delete
    @Test
    void shouldDeleteEntityByRepository_WhenDeleteWithCorrectId() {
        Long inputId = 99L;

        baristaService.delete(inputId);

        Mockito.verify(baristaRepository, Mockito.times(1)).deleteById(inputId);
    }

    @Test
    void shouldThrowNullParamException_WhenDeleteWithNullParam() {
        assertThrows(NullParamException.class, () -> baristaService.delete(null));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -99L})
    void shouldThrowNoValidIdException_WhenDeleteWithLessThanZeroLong(Long id) {
        assertThrows(NoValidIdException.class, () -> baristaService.delete(id));
    }

    //findById
    @Test
    void shouldReturnCorrectBarista_WhenFindByIDWithCorrectId() {
        Long inputId = 99L;
        Barista mockedBarista = Mockito.mock(Barista.class);
        BaristaPublicDTO mockedPublicDto = Mockito.mock(BaristaPublicDTO.class);

        Mockito.when(baristaRepository.findById(inputId))
                .thenReturn(Optional.of(mockedBarista));
        Mockito.when(baristaMapper.entityToDto(mockedBarista))
                .thenReturn(mockedPublicDto);

        IBaristaPublicDTO resultBaristaDto = baristaService.findById(inputId);

        assertEquals(mockedPublicDto, resultBaristaDto);
    }

    @Test
    void shouldThrowNullParamException_WhenFindByIdWithNullParam() {
        assertThrows(NullParamException.class, () -> baristaService.findById(null));
    }

    @Test
    void shouldThrowNullParamException_WhenIdNotFound() {
        Long inputId = 99L;

        Mockito.when(baristaRepository.findById(inputId))
                .thenReturn(Optional.empty());

        assertThrows(BaristaNotFoundException.class, () -> baristaService.findById(inputId));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, -99L})
    void shouldThrowNoValidIdException_WhenFindByIdWithLessThanZeroLong(Long id) {
        assertThrows(NoValidIdException.class, () -> baristaService.findById(id));
    }

    //findAll
    @Test
    void shouldReturnCorrectBaristaList_WhenFindAll() {
        List<Barista> mockedBaristaList = List.of(
                Mockito.mock(Barista.class),
                Mockito.mock(Barista.class),
                Mockito.mock(Barista.class),
                Mockito.mock(Barista.class)
        );
        List<BaristaPublicDTO> mockedPublicDto = List.of(
                Mockito.mock(BaristaPublicDTO.class),
                Mockito.mock(BaristaPublicDTO.class),
                Mockito.mock(BaristaPublicDTO.class),
                Mockito.mock(BaristaPublicDTO.class)
        );

        Mockito.when(baristaRepository.findAll())
                .thenReturn(mockedBaristaList);
        for (int i = 0; i < mockedBaristaList.size(); i++) {
            Mockito.when(baristaMapper.entityToDto(mockedBaristaList.get(i)))
                    .thenReturn(mockedPublicDto.get(i));
        }

        List<BaristaPublicDTO> resultBaristaDtoList = baristaService.findAll();

        assertEquals(mockedPublicDto, resultBaristaDtoList);
    }

    //findByPage
    @ParameterizedTest
    @CsvSource(value = {"0;2", "0;1", "1;1", "1;99", "99;99"}, delimiter = ';')
    void shouldReturnCorrectBaristaList_WhenFindAllByPageCorrectParam(int page, int limit) {
        Page<Barista> mockedPage = Mockito.mock(Page.class);
        List<Barista> mockedBaristaList = List.of(
                Mockito.mock(Barista.class),
                Mockito.mock(Barista.class),
                Mockito.mock(Barista.class),
                Mockito.mock(Barista.class)
        );
        List<BaristaPublicDTO> mockedPublicDto = List.of(
                Mockito.mock(BaristaPublicDTO.class),
                Mockito.mock(BaristaPublicDTO.class),
                Mockito.mock(BaristaPublicDTO.class),
                Mockito.mock(BaristaPublicDTO.class)
        );
        Pageable specifiedPageable = PageRequest.of(page, limit);

        Mockito.when(baristaRepository.findAll(specifiedPageable))
                .thenReturn(mockedPage);
        Mockito.when(mockedPage.stream())
                .thenReturn(mockedBaristaList.stream());
        for (int i = 0; i < mockedBaristaList.size(); i++) {
            Mockito.when(baristaMapper.entityToDto(mockedBaristaList.get(i)))
                    .thenReturn(mockedPublicDto.get(i));
        }

        List<BaristaPublicDTO> resultBaristaDtoList = baristaService.findAllByPage(page, limit);

        assertEquals(mockedPublicDto, resultBaristaDtoList);
    }

    @ParameterizedTest
    @CsvSource(value = {"-1;2", "-99;1"}, delimiter = ';')
    void shouldThrowNoValidPageException_WhenFindAllByPageLessZeroPage(int page, int limit) {

        assertThrows(NoValidPageException.class, () -> baristaService.findAllByPage(page, limit));

    }

    @ParameterizedTest
    @CsvSource(value = {"0;0", "0;-1"}, delimiter = ';')
    void shouldThrowNoValidLimitException_WhenFindAllByPageLessOneLimit(int page, int limit) {

        assertThrows(NoValidLimitException.class, () -> baristaService.findAllByPage(page, limit));

    }

}
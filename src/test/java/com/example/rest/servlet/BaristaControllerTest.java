package com.example.rest.servlet;

import com.example.rest.entity.exception.BaristaNotFoundException;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NoValidNameException;
import com.example.rest.entity.exception.NoValidTipSizeException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.dto.IBaristaPublicDTO;
import com.example.rest.service.imp.BaristaService;
import com.example.rest.servlet.dto.BaristaPublicDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:testContextConfig.xml")
@Import({NoValidPageException.class, RuntimeException.class})
class BaristaControllerTest {
    @Mock
    private BaristaService baristaService;

    private MockMvc mockMvc;

    @BeforeAll
    public static void init() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        GlobalControllerAdvice advice = new GlobalControllerAdvice();
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new BaristaController(baristaService))
                .setControllerAdvice(advice)
                .build();
    }

    @Test
    void findAll_WhenAnyCall_ShouldReturnAllEntities() throws Exception {
        List<BaristaPublicDTO> mockedPublicDtoList = List.of(
                new BaristaPublicDTO(0L, "name", 0.1, List.of()),
                new BaristaPublicDTO(1L, "name", 0.1, List.of()),
                new BaristaPublicDTO(2L, "name", 0.1, List.of())
        );
        Mockito.when(baristaService.findAll())
                .thenReturn(mockedPublicDtoList);
        mockMvc.perform(get("/baristas"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //findAllByPage
    @Test
    void findAllByPage_WhenCorrectPage_ShouldReturnCorrectEntityCount() throws Exception {
        List<BaristaPublicDTO> mockedPublicDtoList = List.of(
                new BaristaPublicDTO(0L, "name", 0.1, List.of()),
                new BaristaPublicDTO(1L, "name", 0.1, List.of()),
                new BaristaPublicDTO(2L, "name", 0.1, List.of())
        );
        Mockito.when(baristaService.findAllByPage(0, 3))
                .thenReturn(mockedPublicDtoList);
        mockMvc.perform(
                        get("/baristas")
                                .param("page", "0")
                                .param("limit", "3")
                )
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.[0].id").value(0L))
                .andExpect(jsonPath("$.[1].id").value(1L))
                .andExpect(jsonPath("$.[2].id").value(2L));
    }

    @Test
    void findAllByPage_WhenWrongPage_ShouldReturnBadRequest() throws Exception {
        Mockito.when(baristaService.findAllByPage(-1, 3))
                .thenThrow(NoValidPageException.class);
        mockMvc.perform(
                        get("/baristas")
                                .param("page", "-1")
                                .param("limit", "3")
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void findAllByPage_WhenWrongLimit_ShouldReturnBadRequest(int limit) throws Exception {
        Mockito.when(baristaService.findAllByPage(0, limit))
                .thenThrow(NoValidPageException.class);
        mockMvc.perform(
                        get("/baristas")
                                .param("page", "0")
                                .param("limit", "" + limit)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    //findById
    @Test
    void findById_WhenCorrectId_ShouldReturnEntityJson() throws Exception {
        IBaristaPublicDTO specifiedPublicDto = new BaristaPublicDTO(0L, "name", 0.1, List.of());

        Mockito.when(baristaService.findById(0L))
                .thenReturn(specifiedPublicDto);

        mockMvc.perform(get("/baristas/0"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.fullName").value("name"))
                .andExpect(jsonPath("$.tipSize").value(0.1));
    }

    @Test
    void findById_WhenIncorrectId_ShouldReturnBadRequest() throws Exception {

        Mockito.when(baristaService.findById(-1L))
                .thenThrow(NoValidIdException.class);

        mockMvc.perform(get("/baristas/-1"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void findById_WhenNotFoundId_ShouldReturnPageNotFound() throws Exception {

        Mockito.when(baristaService.findById(99L))
                .thenThrow(BaristaNotFoundException.class);

        mockMvc.perform(get("/baristas/99"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    //create
    @Test
    void create_WhenCorrectDto_ShouldReturnEntityJson() throws Exception {
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.3;

        String json = String.format(Locale.ENGLISH, """
                {
                    "fullName": "%s",
                    "tipSize": %f
                }
                """, expectedFullName, expectedTipSize);
        IBaristaPublicDTO specifiedPublicDto = new BaristaPublicDTO(0L, expectedFullName, expectedTipSize, List.of());

        Mockito.when(baristaService.create(argThat(barista ->
                        barista.fullName().equals(expectedFullName) &&
                                barista.tipSize().equals(expectedTipSize))))
                .thenReturn(specifiedPublicDto);

        mockMvc.perform(post("/baristas/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(0L))
                .andExpect(jsonPath("$.fullName").value(expectedFullName))
                .andExpect(jsonPath("$.tipSize").value(expectedTipSize));
    }

    @Test
    void create_WhenEmptyRequest_ShouldReturnBadRequest() throws Exception {

        mockMvc.perform(post("/baristas/").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenEmptyName_ShouldReturnBadRequest() throws Exception {
        String expectedFullName = "";
        Double expectedTipSize = 0.3;

        String json = String.format(Locale.ENGLISH, """
                {
                    "fullName": "%s",
                    "tipSize": %f
                }
                """, expectedFullName, expectedTipSize);

        Mockito.when(baristaService.create(argThat(barista ->
                        barista.fullName().equals(expectedFullName) &&
                                barista.tipSize().equals(expectedTipSize))))
                .thenThrow(NoValidNameException.class);

        mockMvc.perform(post("/baristas/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN})
    void create_WhenWrongTipSize_ShouldReturnBadRequest(Double tipSize) throws Exception {
        String expectedFullName = "";

        String json = String.format(Locale.ENGLISH, """
                {
                    "fullName": "%s",
                    "tipSize": %f
                }
                """, expectedFullName, tipSize);

        Mockito.when(baristaService.create(argThat(barista ->
                        barista.fullName().equals(expectedFullName) &&
                                barista.tipSize().equals(tipSize))))
                .thenThrow(NoValidTipSizeException.class);

        mockMvc.perform(post("/baristas/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    //update
    @Test
    void update_WhenCorrectDto_ShouldReturnEntityJson() throws Exception {
        Long expectedId = 0L;
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.3;

        String json = String.format(Locale.ENGLISH, """
                {
                    "id": %d,
                    "fullName": "%s",
                    "tipSize": %f
                }
                """, expectedId, expectedFullName, expectedTipSize);
        IBaristaPublicDTO specifiedPublicDto = new BaristaPublicDTO(0L, expectedFullName, expectedTipSize, List.of());

        Mockito.when(baristaService.update(argThat(barista ->
                        barista.id().equals(expectedId) &&
                                barista.fullName().equals(expectedFullName) &&
                                barista.tipSize().equals(expectedTipSize))))
                .thenReturn(specifiedPublicDto);

        mockMvc.perform(put("/baristas/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.fullName").value(expectedFullName))
                .andExpect(jsonPath("$.tipSize").value(expectedTipSize));
    }

    @Test
    void update_WhenIncorrectId_ShouldReturnBadRequest() throws Exception {
        Long expectedId = -1L;
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.3;

        String json = String.format(Locale.ENGLISH, """
                {
                    "id": %d,
                    "fullName": "%s",
                    "tipSize": %f
                }
                """, expectedId, expectedFullName, expectedTipSize);

        Mockito.when(baristaService.update(argThat(barista -> barista.id().equals(expectedId))))
                .thenThrow(new NoValidIdException(-1L));

        mockMvc.perform(put("/baristas/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNotFound_ShouldReturnNotFound() throws Exception {
        Long expectedId = 99L;
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.3;

        String json = String.format(Locale.ENGLISH, """
                {
                    "id": %d,
                    "fullName": "%s",
                    "tipSize": %f
                }
                """, expectedId, expectedFullName, expectedTipSize);

        Mockito.when(baristaService.update(argThat(barista -> barista.id().equals(expectedId))))
                .thenThrow(new BaristaNotFoundException(expectedId));

        mockMvc.perform(put("/baristas/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update_WhenEmptyName_ShouldReturnBadRequest() throws Exception {
        Long expectedId = 99L;
        String expectedFullName = "";
        Double expectedTipSize = 0.3;

        String json = String.format(Locale.ENGLISH, """
                {
                    "id": %d,
                    "fullName": "%s",
                    "tipSize": %f
                }
                """, expectedId, expectedFullName, expectedTipSize);

        Mockito.when(baristaService.update(argThat(barista -> barista.fullName().equals(expectedFullName))))
                .thenThrow(new NoValidNameException());

        mockMvc.perform(put("/baristas/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN})
    void update_WhenIncorrectTipSize_ShouldReturnBadRequest(Double tipSize) throws Exception {
        Long expectedId = 99L;
        String expectedFullName = "";

        String json = String.format(Locale.ENGLISH, """
                {
                    "id": %d,
                    "fullName": "%s",
                    "tipSize": %f
                }
                """, expectedId, expectedFullName, tipSize);

        Mockito.when(baristaService.update(argThat(barista -> barista.tipSize().equals(tipSize))))
                .thenThrow(new NoValidTipSizeException(tipSize));

        mockMvc.perform(put("/baristas/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //delete
    @Test
    void delete_WhenCorrectId_ShouldReturnOk() throws Exception {
        Long expectedId = 0L;

        mockMvc.perform(delete("/baristas/" + expectedId))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(baristaService, Mockito.times(1)).delete(expectedId);
    }
}
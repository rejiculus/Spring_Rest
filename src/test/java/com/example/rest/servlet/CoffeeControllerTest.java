package com.example.rest.servlet;

import com.example.rest.entity.exception.CoffeeNotFoundException;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NoValidNameException;
import com.example.rest.entity.exception.NoValidPriceException;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.imp.CoffeeService;
import com.example.rest.servlet.dto.CoffeePublicDTO;
import com.example.rest.servlet.dto.OrderNoRefDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.argThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CoffeeControllerTest {

    @Mock
    private CoffeeService coffeeService;

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
                .standaloneSetup(new CoffeeController(coffeeService))
                .setControllerAdvice(advice)
                .build();
    }

    //findAll
    @Test
    void findAll_WhenAnyRequest_ShouldReturnAllEntities() throws Exception {
        List<CoffeePublicDTO> coffeePublicDTOList = List.of(
                new CoffeePublicDTO(0L, "name", 0.1, List.of()),
                new CoffeePublicDTO(0L, "name", 0.1, List.of()),
                new CoffeePublicDTO(0L, "name", 0.1, List.of(
                        new OrderNoRefDTO(0L, 0L, LocalDateTime.MIN, null, 0.0)
                ))
        );


        Mockito.when(coffeeService.findAll())
                .thenReturn(coffeePublicDTOList);


        mockMvc.perform(get("/coffees"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //findById
    @Test
    void findById_WhenCorrectId_ShouldReturnEntity() throws Exception {
        Long expectedId = 0L;
        CoffeePublicDTO coffeePublicDTO = new CoffeePublicDTO(expectedId, "name", 0.1, List.of(
                new OrderNoRefDTO(0L, 0L, LocalDateTime.MIN, null, 0.0)
        ));


        Mockito.when(coffeeService.findById(expectedId))
                .thenReturn(coffeePublicDTO);


        mockMvc.perform(get("/coffees/" + expectedId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId));
    }

    @Test
    void findById_WhenWrongId_ShouldReturnBadRequest() throws Exception {
        Long expectedId = -10L;

        Mockito.when(coffeeService.findById(expectedId))
                .thenThrow(new NoValidIdException(expectedId));

        mockMvc.perform(get("/coffees/" + expectedId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_WhenNotFoundId_ShouldReturnNotFound() throws Exception {
        Long expectedId = 99L;

        Mockito.when(coffeeService.findById(expectedId))
                .thenThrow(new CoffeeNotFoundException(expectedId));

        mockMvc.perform(get("/coffees/" + expectedId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    //findAllByPage
    @Test
    void findAllByPage_WhenCorrectParams_ShouldReturnEntity() throws Exception {
        int page = 0;
        int limit = 1;
        List<CoffeePublicDTO> coffeePublicDTOList = List.of(
                new CoffeePublicDTO(0L, "name", 0.1, List.of()),
                new CoffeePublicDTO(1L, "name", 0.1, List.of()),
                new CoffeePublicDTO(2L, "name", 0.1, List.of(
                        new OrderNoRefDTO(0L, 0L, LocalDateTime.MIN, null, 0.0)
                ))
        );


        Mockito.when(coffeeService.findAllByPage(page, limit))
                .thenReturn(coffeePublicDTOList);


        mockMvc.perform(get("/coffees/")
                        .param("page", "" + page)
                        .param("limit", "" + limit))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(0L))
                .andExpect(jsonPath("$.[1].id").value(1L))
                .andExpect(jsonPath("$.[2].id").value(2L));
    }

    @Test
    void findAllByPage_WhenWrongPage_ShouldReturnBadRequest() throws Exception {
        int page = -1;
        int limit = 1;

        Mockito.when(coffeeService.findAllByPage(page, limit))
                .thenThrow(new NoValidPageException(page));


        mockMvc.perform(get("/coffees/")
                        .param("page", "" + page)
                        .param("limit", "" + limit))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void findAllByPage_WhenWrongLimit_ShouldReturnBadRequest(int limit) throws Exception {
        int page = 0;

        Mockito.when(coffeeService.findAllByPage(page, limit))
                .thenThrow(new NoValidLimitException(limit));


        mockMvc.perform(get("/coffees/")
                        .param("page", "" + page)
                        .param("limit", "" + limit))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //create
    @Test
    void create_WhenCorrectJson_ShouldReturnEntity() throws Exception {
        String expectedName = "Wow!";
        Double expectedPrice = 200.0;

        String json = String.format(Locale.ENGLISH, """
                {
                    "name":"%s",
                    "price":%f
                }
                """, expectedName, expectedPrice);
        CoffeePublicDTO coffeePublicDTO = new CoffeePublicDTO(0L, expectedName, expectedPrice, List.of());


        Mockito.when(coffeeService.create(argThat(coffee ->
                        coffee.name().equals(expectedName) &&
                                coffee.price().equals(expectedPrice))))
                .thenReturn(coffeePublicDTO);


        mockMvc.perform(post("/coffees/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.price").value(expectedPrice));
    }

    @Test
    void create_WhenEmptyName_ShouldReturnBadRequest() throws Exception {
        String expectedName = "";
        Double expectedPrice = 200.0;

        String json = String.format(Locale.ENGLISH, """
                {
                    "name":"%s",
                    "price":%f
                }
                """, expectedName, expectedPrice);


        Mockito.when(coffeeService.create(argThat(coffee -> coffee.name().equals(expectedName))))
                .thenThrow(new NoValidNameException());


        mockMvc.perform(post("/coffees/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN})
    void create_WhenWrongPrice_ShouldReturnBadRequest(Double price) throws Exception {
        String expectedName = "Wow";

        String json = String.format(Locale.ENGLISH, """
                {
                    "name":"%s",
                    "price":%f
                }
                """, expectedName, price);


        Mockito.when(coffeeService.create(argThat(coffee -> coffee.price().equals(price))))
                .thenThrow(new NoValidPriceException(price));


        mockMvc.perform(post("/coffees/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //update
    @Test
    void update_WhenCorrectJson_ShouldReturnEntity() throws Exception {
        Long expectedId = 0L;
        String expectedName = "Wow!";
        Double expectedPrice = 200.0;
        List<Long> expectedOrderIdList = List.of(0L);

        List<OrderNoRefDTO> specifiedOrderDtoList = List.of(
                new OrderNoRefDTO(0L, 0L, LocalDateTime.MIN, null, 0.0)
        );

        String json = String.format(Locale.ENGLISH, """
                {
                    "name":"%s",
                    "price":%f,
                    "orderIdList":%s
                }
                """, expectedName, expectedPrice, expectedOrderIdList);
        CoffeePublicDTO coffeePublicDTO = new CoffeePublicDTO(0L, expectedName, expectedPrice, specifiedOrderDtoList);


        Mockito.when(coffeeService.update(argThat(coffee ->
                        coffee.id().equals(expectedId) &&
                                coffee.name().equals(expectedName) &&
                                coffee.price().equals(expectedPrice))))
                .thenReturn(coffeePublicDTO);


        mockMvc.perform(put("/coffees/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.price").value(expectedPrice))
                .andExpect(jsonPath("$.orders.[0].id").value(expectedOrderIdList.get(0)));
    }

    @Test
    void update_WhenWrongId_ShouldReturnBadRequest() throws Exception {
        Long expectedId = -1L;
        String expectedName = "Wow!";
        Double expectedPrice = 200.0;
        List<Long> expectedOrderIdList = List.of(0L);


        String json = String.format(Locale.ENGLISH, """
                {
                    "name":"%s",
                    "price":%f,
                    "orderIdList":%s
                }
                """, expectedName, expectedPrice, expectedOrderIdList);


        Mockito.when(coffeeService.update(argThat(coffee ->
                        coffee.id().equals(expectedId))))
                .thenThrow(new NoValidIdException(expectedId));


        mockMvc.perform(put("/coffees/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNotFoundId_ShouldReturnNotFound() throws Exception {
        Long expectedId = 0L;
        String expectedName = "Wow!";
        Double expectedPrice = 200.0;
        List<Long> expectedOrderIdList = List.of(0L);


        String json = String.format(Locale.ENGLISH, """
                {
                    "name":"%s",
                    "price":%f,
                    "orderIdList":%s
                }
                """, expectedName, expectedPrice, expectedOrderIdList);


        Mockito.when(coffeeService.update(argThat(coffee ->
                        coffee.id().equals(expectedId))))
                .thenThrow(new CoffeeNotFoundException(expectedId));


        mockMvc.perform(put("/coffees/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update_WhenEmptyName_ShouldReturnBadRequest() throws Exception {
        Long expectedId = 0L;
        String expectedName = "";
        Double expectedPrice = 200.0;
        List<Long> expectedOrderIdList = List.of(0L);


        String json = String.format(Locale.ENGLISH, """
                {
                    "name":"%s",
                    "price":%f,
                    "orderIdList":%s
                }
                """, expectedName, expectedPrice, expectedOrderIdList);


        Mockito.when(coffeeService.update(argThat(coffee ->
                        coffee.name().equals(expectedName))))
                .thenThrow(new NoValidNameException());


        mockMvc.perform(put("/coffees/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN})
    void update_WhenWrongPrice_ShouldReturnBadRequest(Double price) throws Exception {
        Long expectedId = 0L;
        String expectedName = "";
        List<Long> expectedOrderIdList = List.of(0L);


        String json = String.format(Locale.ENGLISH, """
                {
                    "name":"%s",
                    "price":%f,
                    "orderIdList":%s
                }
                """, expectedName, price, expectedOrderIdList);


        Mockito.when(coffeeService.update(argThat(coffee ->
                        coffee.name().equals(expectedName))))
                .thenThrow(new NoValidPriceException(price));


        mockMvc.perform(put("/coffees/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //delete
    @Test
    void delete_WhenCorrectId_ShouldReturnOk() throws Exception {
        Long expectedId = 0L;

        mockMvc.perform(delete("/coffees/" + expectedId))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(coffeeService, Mockito.times(1)).delete(expectedId);
    }

}
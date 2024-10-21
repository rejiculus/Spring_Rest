package com.example.rest.servlet;

import com.example.rest.entity.exception.BaristaNotFoundException;
import com.example.rest.entity.exception.CoffeeNotFoundException;
import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.OrderNotFoundException;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.exception.OrderAlreadyCompletedException;
import com.example.rest.service.imp.OrderService;
import com.example.rest.servlet.dto.BaristaNoRefDTO;
import com.example.rest.servlet.dto.CoffeeNoRefDTO;
import com.example.rest.servlet.dto.OrderPublicDTO;
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

class OrderControllerTest {
    @Mock
    private OrderService orderService;

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
                .standaloneSetup(new OrderController(orderService))
                .setControllerAdvice(advice)
                .build();
    }

    //findAll
    @Test
    void findAll_WhenAnyRequest_ShouldReturnAllEntities() throws Exception {
        BaristaNoRefDTO baristaNoRefDTO = new BaristaNoRefDTO(0L, "John Doe", 0.1);
        List<OrderPublicDTO> orderPublicDTOList = List.of(
                new OrderPublicDTO(0L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of()),
                new OrderPublicDTO(1L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of()),
                new OrderPublicDTO(2L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of())
        );


        Mockito.when(orderService.findAll())
                .thenReturn(orderPublicDTOList);


        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //findAllByPage
    @Test
    void findAllByPage_WhenCorrectPage_ShouldReturnEntities() throws Exception {
        int page = 0;
        int limit = 1;
        BaristaNoRefDTO baristaNoRefDTO = new BaristaNoRefDTO(0L, "John Doe", 0.1);
        List<OrderPublicDTO> orderPublicDTOList = List.of(
                new OrderPublicDTO(0L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of()),
                new OrderPublicDTO(1L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of()),
                new OrderPublicDTO(2L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of())
        );


        Mockito.when(orderService.findAllByPage(page, limit))
                .thenReturn(orderPublicDTOList);


        mockMvc.perform(get("/orders")
                        .param("page", "" + page)
                        .param("limit", "" + limit))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void findAllByPage_WhenWrongPage_ShouldReturnBadRequest() throws Exception {
        int page = -1;
        int limit = 1;

        Mockito.when(orderService.findAllByPage(page, limit))
                .thenThrow(new NoValidPageException(page));


        mockMvc.perform(get("/orders")
                        .param("page", "" + page)
                        .param("limit", "" + limit))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void findAllByPage_WhenWrongLimit_ShouldReturnBadRequest(int limit) throws Exception {
        int page = 0;

        Mockito.when(orderService.findAllByPage(page, limit))
                .thenThrow(new NoValidLimitException(limit));


        mockMvc.perform(get("/orders")
                        .param("page", "" + page)
                        .param("limit", "" + limit))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    //getQueue
    @Test
    void getQueue_WhenAnyRequest_ShouldReturnAllNotCompletedEntities() throws Exception {
        BaristaNoRefDTO baristaNoRefDTO = new BaristaNoRefDTO(0L, "John Doe", 0.1);
        List<OrderPublicDTO> orderPublicDTOList = List.of(
                new OrderPublicDTO(0L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of()),
                new OrderPublicDTO(1L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of()),
                new OrderPublicDTO(2L, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of())
        );


        Mockito.when(orderService.getOrderQueue())
                .thenReturn(orderPublicDTOList);


        mockMvc.perform(get("/orders/queue"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    //findById
    @Test
    void findById_WhenCorrectId_ShouldReturnEntity() throws Exception {
        Long expectedId = 0L;
        BaristaNoRefDTO baristaNoRefDTO = new BaristaNoRefDTO(0L, "John Doe", 0.1);
        OrderPublicDTO orderPublicDTO = new OrderPublicDTO(expectedId, baristaNoRefDTO, LocalDateTime.MIN, null, 0.0, List.of());


        Mockito.when(orderService.findById(expectedId))
                .thenReturn(orderPublicDTO);


        mockMvc.perform(get("/orders/" + expectedId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId));
    }

    @Test
    void findById_WhenWrongId_ShouldReturnBadRequest() throws Exception {
        Long expectedId = -1L;

        Mockito.when(orderService.findById(expectedId))
                .thenThrow(new NoValidIdException(expectedId));

        mockMvc.perform(get("/orders/" + expectedId))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void findById_WhenNotFoundId_ShouldReturnNotFound() throws Exception {
        Long expectedId = 99L;

        Mockito.when(orderService.findById(expectedId))
                .thenThrow(new OrderNotFoundException(expectedId));

        mockMvc.perform(get("/orders/" + expectedId))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    //complete
    @Test
    void complete_WhenCorrectId_ShouldReturnEntity() throws Exception {
        Long expectedId = 0L;
        BaristaNoRefDTO baristaNoRefDTO = new BaristaNoRefDTO(0L, "John Doe", 0.1);
        OrderPublicDTO orderPublicDTO = new OrderPublicDTO(expectedId, baristaNoRefDTO, LocalDateTime.MIN, LocalDateTime.MAX, 0.0, List.of());


        Mockito.when(orderService.completeOrder(expectedId))
                .thenReturn(orderPublicDTO);


        mockMvc.perform(put("/orders/" + expectedId + "/complete"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId));
    }

    @Test
    void complete_WhenWrongId_ShouldReturnBadRequest() throws Exception {
        Long expectedId = -1L;

        Mockito.when(orderService.completeOrder(expectedId))
                .thenThrow(new NoValidIdException(expectedId));

        mockMvc.perform(put("/orders/" + expectedId + "/complete"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void complete_WhenNotFoundId_ShouldReturnNotFound() throws Exception {
        Long expectedId = 99L;

        Mockito.when(orderService.completeOrder(expectedId))
                .thenThrow(new OrderNotFoundException(expectedId));

        mockMvc.perform(put("/orders/" + expectedId + "/complete"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void complete_WhenCompleted_ShouldReturnBadRequest() throws Exception {
        Long expectedId = 99L;

        Mockito.when(orderService.completeOrder(expectedId))
                .thenThrow(OrderAlreadyCompletedException.class);

        mockMvc.perform(put("/orders/" + expectedId + "/complete"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    //delete
    @Test
    void delete_WhenCorrectId_ShouldReturnOk() throws Exception {
        Long expectedId = 0L;

        mockMvc.perform(delete("/orders/" + expectedId))
                .andDo(print())
                .andExpect(status().isOk());

        Mockito.verify(orderService, Mockito.times(1)).delete(expectedId);
    }

    //create
    @Test
    void create_WhenCorrectData_ShouldReturnEntity() throws Exception {
        Long expectedId = 0L;
        Long expectedBaristaId = 0L;
        List<Long> expectedCoffeeIdList = List.of(1L);

        List<CoffeeNoRefDTO> coffeeNoRefDTOList = List.of(new CoffeeNoRefDTO(1L, "name", 0.0));
        BaristaNoRefDTO baristaNoRefDTO = new BaristaNoRefDTO(expectedBaristaId, "John Doe", 0.1);
        OrderPublicDTO orderPublicDTO = new OrderPublicDTO(expectedId, baristaNoRefDTO, LocalDateTime.MIN, LocalDateTime.MAX, 0.0, coffeeNoRefDTOList);


        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s
                }
                """, expectedBaristaId, expectedCoffeeIdList);

        Mockito.when(orderService.create(argThat(order ->
                        order.baristaId().equals(expectedBaristaId) &&
                                order.coffeeIdList().equals(expectedCoffeeIdList))))
                .thenReturn(orderPublicDTO);


        mockMvc.perform(post("/orders/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.baristaId.id").value(expectedBaristaId))
                .andExpect(jsonPath("$.coffees.[0].id").value(expectedCoffeeIdList.get(0)));
    }

    @Test
    void create_WhenWrongBaristaId_ShouldReturnBadRequest() throws Exception {
        Long expectedBaristaId = -1L;
        List<Long> expectedCoffeeIdList = List.of(1L);


        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s
                }
                """, expectedBaristaId, expectedCoffeeIdList);

        Mockito.when(orderService.create(argThat(order ->
                        order.baristaId().equals(expectedBaristaId))))
                .thenThrow(new NoValidIdException(expectedBaristaId));


        mockMvc.perform(post("/orders/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenBaristaNotFound_ShouldReturnNotFound() throws Exception {
        Long expectedBaristaId = 99L;
        List<Long> expectedCoffeeIdList = List.of(1L);


        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s
                }
                """, expectedBaristaId, expectedCoffeeIdList);

        Mockito.when(orderService.create(argThat(order ->
                        order.baristaId().equals(expectedBaristaId))))
                .thenThrow(new BaristaNotFoundException(expectedBaristaId));


        mockMvc.perform(post("/orders/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WhenWrongOrderId_ShouldReturnBadRequest() throws Exception {
        Long expectedBaristaId = 99L;
        List<Long> expectedCoffeeIdList = List.of(-1L);


        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s
                }
                """, expectedBaristaId, expectedCoffeeIdList);

        Mockito.when(orderService.create(argThat(order ->
                        order.coffeeIdList().equals(expectedCoffeeIdList))))
                .thenThrow(new NoValidIdException(expectedCoffeeIdList.get(0)));


        mockMvc.perform(post("/orders/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenOrderNotFound_ShouldReturnNotFound() throws Exception {
        Long expectedBaristaId = 99L;
        List<Long> expectedCoffeeIdList = List.of(99L);


        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s
                }
                """, expectedBaristaId, expectedCoffeeIdList);

        Mockito.when(orderService.create(argThat(order ->
                        order.coffeeIdList().equals(expectedCoffeeIdList))))
                .thenThrow(new OrderNotFoundException(expectedCoffeeIdList.get(0)));


        mockMvc.perform(post("/orders/").contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    //update
    @Test
    void update_WhenCorrectData_ShouldReturnEntity() throws Exception {
        Long expectedId = 0L;
        Long expectedBaristaId = 0L;
        Double expectedPrice = 102324.0;
        List<Long> expectedCoffeeIdList = List.of(1L);

        List<CoffeeNoRefDTO> coffeeNoRefDTOList = List.of(new CoffeeNoRefDTO(1L, "name", 0.0));
        BaristaNoRefDTO baristaNoRefDTO = new BaristaNoRefDTO(expectedBaristaId, "John Doe", 0.1);
        OrderPublicDTO orderPublicDTO = new OrderPublicDTO(expectedId, baristaNoRefDTO, LocalDateTime.MIN, LocalDateTime.MAX, expectedPrice, coffeeNoRefDTOList);


        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s,
                    "created":"2024-09-13T14:20:00",
                    "price":%f
                }
                """, expectedBaristaId, expectedCoffeeIdList, expectedPrice);

        Mockito.when(orderService.update(argThat(order ->
                        order.baristaId().equals(expectedBaristaId) &&
                                order.coffeeIdList().equals(expectedCoffeeIdList))))
                .thenReturn(orderPublicDTO);


        mockMvc.perform(put("/orders/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expectedId))
                .andExpect(jsonPath("$.baristaId.id").value(expectedBaristaId))
                .andExpect(jsonPath("$.coffees.[0].id").value(expectedCoffeeIdList.get(0)));
    }

    @Test
    void update_WhenWrongId_ShouldReturnBadRequest() throws Exception {
        Long expectedId = -1L;
        Long expectedBaristaId = 0L;
        Double expectedPrice = 102324.0;
        List<Long> expectedCoffeeIdList = List.of(1L);

        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s,
                    "created":"2024-09-13T14:20:00",
                    "price":%f
                }
                """, expectedBaristaId, expectedCoffeeIdList, expectedPrice);

        Mockito.when(orderService.update(argThat(order -> order.id().equals(expectedId))))
                .thenThrow(new NoValidIdException(expectedId));


        mockMvc.perform(put("/orders/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenWrongBaristaId_ShouldReturnBadRequest() throws Exception {
        Long expectedId = 0L;
        Long expectedBaristaId = -1L;
        Double expectedPrice = 102324.0;
        List<Long> expectedCoffeeIdList = List.of(1L);

        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s,
                    "created":"2024-09-13T14:20:00",
                    "price":%f
                }
                """, expectedBaristaId, expectedCoffeeIdList, expectedPrice);

        Mockito.when(orderService.update(argThat(order -> order.baristaId().equals(expectedBaristaId))))
                .thenThrow(new NoValidIdException(expectedId));


        mockMvc.perform(put("/orders/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenWrongCoffeeId_ShouldReturnBadRequest() throws Exception {
        Long expectedId = 0L;
        Long expectedBaristaId = 0L;
        Double expectedPrice = 102324.0;
        List<Long> expectedCoffeeIdList = List.of(-1L);

        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s,
                    "created":"2024-09-13T14:20:00",
                    "price":%f
                }
                """, expectedBaristaId, expectedCoffeeIdList, expectedPrice);

        Mockito.when(orderService.update(argThat(order -> order.coffeeIdList().contains(-1L))))
                .thenThrow(new NoValidIdException(-1L));


        mockMvc.perform(put("/orders/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNotFoundCoffee_ShouldReturnNotFound() throws Exception {
        Long expectedId = 0L;
        Long expectedBaristaId = 0L;
        Double expectedPrice = 102324.0;
        List<Long> expectedCoffeeIdList = List.of(99L);

        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s,
                    "created":"2024-09-13T14:20:00",
                    "price":%f
                }
                """, expectedBaristaId, expectedCoffeeIdList, expectedPrice);

        Mockito.when(orderService.update(argThat(order -> order.coffeeIdList().contains(99L))))
                .thenThrow(new CoffeeNotFoundException(99L));


        mockMvc.perform(put("/orders/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update_WhenNotFoundBarista_ShouldReturnNotFound() throws Exception {
        Long expectedId = 0L;
        Long expectedBaristaId = 99L;
        Double expectedPrice = 102324.0;
        List<Long> expectedCoffeeIdList = List.of(99L);

        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s,
                    "created":"2024-09-13T14:20:00",
                    "price":%f
                }
                """, expectedBaristaId, expectedCoffeeIdList, expectedPrice);

        Mockito.when(orderService.update(argThat(order -> order.baristaId().equals(expectedBaristaId))))
                .thenThrow(new BaristaNotFoundException(expectedBaristaId));


        mockMvc.perform(put("/orders/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void update_WhenNotFoundOrder_ShouldReturnNotFound() throws Exception {
        Long expectedId = 0L;
        Long expectedBaristaId = 99L;
        Double expectedPrice = 102324.0;
        List<Long> expectedCoffeeIdList = List.of(99L);

        String json = String.format(Locale.ENGLISH, """
                {
                    "baristaId":%d,
                    "coffeeIdList":%s,
                    "created":"2024-09-13T14:20:00",
                    "price":%f
                }
                """, expectedBaristaId, expectedCoffeeIdList, expectedPrice);

        Mockito.when(orderService.update(argThat(order -> order.id().equals(expectedId))))
                .thenThrow(new OrderNotFoundException(expectedId));


        mockMvc.perform(put("/orders/" + expectedId).contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
package com.example.rest.entity;

import com.example.rest.entity.exception.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    @Test
    void shouldReturnOrder_WhenCorrectParams() {
        Long expectedId = 99L;
        Barista expectedBarista = Mockito.mock(Barista.class);
        List<Coffee> expectedCoffeeList = new ArrayList<>(List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        ));
        LocalDateTime expectedCreated = LocalDateTime.now().minusMinutes(1);
        LocalDateTime expectedCompleted = LocalDateTime.now();
        Double expectedPrice = 299.9;

        Order order = new Order(expectedId, expectedBarista, expectedCoffeeList, expectedCreated, expectedCompleted, expectedPrice);

        assertEquals(expectedId, order.getId());
        assertEquals(expectedBarista, order.getBarista());
        assertEquals(expectedCoffeeList, order.getCoffeeList());
        assertEquals(expectedCreated, order.getCreated());
        assertEquals(expectedCompleted, order.getCompleted());
        assertEquals(expectedPrice, order.getPrice());
    }

    @Test
    void shouldThrowNullParamException_WhenConstructorNullParam() {
        List<Coffee> inputCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista inputBarista = Mockito.mock(Barista.class);

        Assertions.assertThrows(NullParamException.class, () -> new Order(null, inputBarista, inputCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 9.0));
        Assertions.assertThrows(NullParamException.class, () -> new Order(99L, null, inputCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 9.0));
        Assertions.assertThrows(NullParamException.class, () -> new Order(99L, inputBarista, null, LocalDateTime.MIN, LocalDateTime.MAX, 9.0));
        Assertions.assertThrows(CreatedNotDefinedException.class, () -> new Order(99L, inputBarista, inputCoffeeList, null, LocalDateTime.MAX, 9.0));
        Assertions.assertThrows(NullParamException.class, () -> new Order(99L, inputBarista, inputCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, null));
    }

    @Test
    void shouldThrowCompletedBeforeCreatedException_WhenConstructorCompletedBeforeCreated() {
        List<Coffee> inputCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista inputBarista = Mockito.mock(Barista.class);

        Assertions.assertThrows(CompletedBeforeCreatedException.class, () -> new Order(99L, inputBarista, inputCoffeeList, LocalDateTime.MAX, LocalDateTime.MIN, 9.0));

    }

    @ParameterizedTest
    @ValueSource(longs = {-99, -10, -1})
    void shouldThrowNoValidIdException_WhenConstructorIdParamIncorrect(Long id) {
        List<Coffee> inputCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista inputBarista = Mockito.mock(Barista.class);

        Assertions.assertThrows(NoValidIdException.class, () -> new Order(id, inputBarista, inputCoffeeList, LocalDateTime.MAX, LocalDateTime.MIN, 9.0));
    }


    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    void shouldThrowNoValidPriceException_WhenConstructorPriceParamIncorrect(Double price) {
        List<Coffee> inputCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista inputBarista = Mockito.mock(Barista.class);

        Assertions.assertThrows(NoValidPriceException.class, () -> new Order(0L, inputBarista, inputCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, price));
    }


    @ParameterizedTest
    @ValueSource(longs = {0, 1, 99})
    void shouldReturnCorrectLong_WhenGetId(Long id) {
        List<Coffee> inputCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista inputBarista = Mockito.mock(Barista.class);

        Order order = new Order(id, inputBarista, inputCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 9.0);

        assertEquals(id, order.getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 99})
    void shouldSetCorrectLong_WhenSetId(Long id) {
        List<Coffee> inputCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista inputBarista = Mockito.mock(Barista.class);
        Order order = new Order(id, inputBarista, inputCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 9.0);

        order.setId(id);

        assertEquals(id, order.getId());

    }

    @ParameterizedTest
    @ValueSource(longs = {-99, -2, -1})
    void shouldThrowNoValidIdException_WhenSetIncorrectId(Long id) {
        Order order = new Order();

        Assertions.assertThrows(NoValidIdException.class, () -> order.setId(id));
    }

    @Test
    void shouldReturnCorrectBarista_WhenGetBarista() {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista mockedBarista = Mockito.mock(Barista.class);

        Order order = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 9.0);

        assertEquals(mockedBarista, order.getBarista());
    }

    @Test
    void shouldSetCorrectBarista_WhenSetBarista() {
        Barista mockedBarista = Mockito.mock(Barista.class);
        Order order = new Order();

        order.setBarista(mockedBarista);

        assertEquals(mockedBarista, order.getBarista());
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullBarista() {
        Order order = new Order();

        Assertions.assertThrows(NullParamException.class, () -> order.setBarista(null));
    }

    @Test
    void shouldReturnCorrectList_WhenGetCoffeeList() {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista mockedBarista = Mockito.mock(Barista.class);

        Order order = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 9.0);

        assertEquals(mockedCoffeeList, order.getCoffeeList());
    }

    @Test
    void shouldSetCorrectList_WhenSetCoffeeList() {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Order order = new Order();

        order.setCoffeeList(mockedCoffeeList);

        assertEquals(mockedCoffeeList, order.getCoffeeList());

    }

    @Test
    void shouldTrowNullParamException_WhenSetNullCoffeeList() {
        Order order = new Order();

        assertThrows(NullParamException.class, () -> order.setCoffeeList(null));
    }


    @Test
    void shouldReturnCorrectLocalDateTime_WhenGetCreated() {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista mockedBarista = Mockito.mock(Barista.class);

        Order order = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, null, 9.0);

        assertEquals(LocalDateTime.MIN, order.getCreated());
    }

    @Test
    void shouldSetCorrectLocalDateTime_WhenSetCreated() {
        Order order = new Order();

        order.setCreated(LocalDateTime.MIN);

        assertEquals(LocalDateTime.MIN, order.getCreated());
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullCreated() {
        Order order = new Order();

        Assertions.assertThrows(NullParamException.class, () -> order.setCreated(null));
    }

    @Test
    void shouldReturnCorrectLocalDateTime_WhenGetCompleted() {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista mockedBarista = Mockito.mock(Barista.class);
        Order order = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 9.0);

        assertEquals(LocalDateTime.MAX, order.getCompleted());
    }

    @Test
    void shouldSetCorrectLocalDateTime_WhenSetCompleted() {
        Order order = new Order();
        order.setCreated(LocalDateTime.MIN);

        order.setCompleted(LocalDateTime.MAX);

        assertEquals(LocalDateTime.MAX, order.getCompleted());
    }

    @Test
    void shouldThrowCompletedBeforeCreatedException_WhenSetCompletedBeforeCreated() {
        Order order = new Order();
        LocalDateTime dateTime = LocalDateTime.now();
        order.setCreated(dateTime);
        LocalDateTime lessDateTime = dateTime.minusNanos(1);

        Assertions.assertThrows(CompletedBeforeCreatedException.class, () -> order.setCompleted(lessDateTime));
    }

    @Test
    void shouldThrowCreatedNotDefinedException_WhenSetCompletedAndCreatedNotSpecified() {
        Order order = new Order();
        LocalDateTime dateTime = LocalDateTime.now();
        LocalDateTime lessDateTime = dateTime.minusNanos(1);

        Assertions.assertThrows(CreatedNotDefinedException.class, () -> order.setCompleted(lessDateTime));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 22.0})
    void shouldReturnCorrectDouble_WhenGetPrice(Double price) {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista mockedBarista = Mockito.mock(Barista.class);
        Order order = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, null, price);

        assertEquals(price, order.getPrice());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 22.0})
    void shouldSetCorrectDouble_WhenSetPrice(Double price) {
        Order order = new Order();

        order.setPrice(price);

        assertEquals(price, order.getPrice());
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullPrice() {
        Order order = new Order();

        Assertions.assertThrows(NullParamException.class, () -> order.setPrice(null));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, -0.00001, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void shouldThrowNoValidPriceException_WhenSetIncorrectTipSize(Double price) {
        Order order = new Order();

        Assertions.assertThrows(NoValidPriceException.class, () -> order.setPrice(price));
    }

    @Test
    void shouldReturnTrue_WhenEquals() {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista mockedBarista = Mockito.mock(Barista.class);
        Order order1 = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 299.0);
        Order order2 = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MAX, null, 100.0);

        assertEquals(order1, order2);
    }

    @Test
    void shouldReturnFalse_WhenNotEquals() {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista mockedBarista = Mockito.mock(Barista.class);
        Order order1 = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 299.0);
        Order order2 = new Order(1L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 299.0);

        assertNotEquals(order1, order2);

        assertNotEquals(null, order1);
        assertNotEquals(new Object(), order1);
    }

    @Test
    void shouldReturnSameHashCode_WhenEquals() {
        List<Coffee> mockedCoffeeList = List.of(
                Mockito.mock(Coffee.class),
                Mockito.mock(Coffee.class)
        );
        Barista mockedBarista = Mockito.mock(Barista.class);
        Order order1 = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 299.0);
        Order order2 = new Order(1L, mockedBarista, mockedCoffeeList, LocalDateTime.MIN, LocalDateTime.MAX, 299.0);
        Order order3 = new Order(0L, mockedBarista, mockedCoffeeList, LocalDateTime.MAX, null, 100.0);

        assertEquals(order1.hashCode(), order3.hashCode());
        assertNotEquals(order1.hashCode(), order2.hashCode());
        assertNotEquals(order3.hashCode(), order2.hashCode());
    }

}
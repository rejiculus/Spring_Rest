package com.example.rest.entity;

import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NoValidNameException;
import com.example.rest.entity.exception.NoValidPriceException;
import com.example.rest.entity.exception.NullParamException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CoffeeTest {
    @Test
    void shouldReturnCoffee_WhenCorrectParams() {
        Long expectedId = 99L;
        String expectedName = "Frapuchino";
        Double expectedPrice = 299.9;
        List<Order> expectedOrderList = new ArrayList<>();

        Coffee coffee = new Coffee(expectedId, expectedName, expectedPrice, expectedOrderList);

        assertEquals(expectedId, coffee.getId());
        assertEquals(expectedName, coffee.getName());
        assertEquals(expectedPrice, coffee.getPrice());
        assertEquals(expectedOrderList, coffee.getOrderList());
    }

    @Test
    void shouldThrowNullParamException_WhenConstructorParamNull() {
        List<Order> inputOrders = new ArrayList<>();

        Assertions.assertThrows(NullParamException.class, () -> new Coffee(null, "Frapuchino", 199.9, inputOrders));
        Assertions.assertThrows(NullParamException.class, () -> new Coffee(99L, null, 199.9, inputOrders));
        Assertions.assertThrows(NullParamException.class, () -> new Coffee(99L, "Frapuchino", null, inputOrders));
        Assertions.assertThrows(NullParamException.class, () -> new Coffee(99L, "Frapuchino", 199.9, null));
    }

    @ParameterizedTest
    @ValueSource(longs = {-99, -10, -1})
    void shouldThrowNoValidIdException_WhenConstructorIdParamIncorrect(Long id) {
        List<Order> inputOrders = new ArrayList<>();

        Assertions.assertThrows(NoValidIdException.class, () -> new Coffee(id, "Frapuchino", 199.9, inputOrders));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void shouldThrowNoValidNameException_WhenConstructorNameParamIncorrect(String name) {
        List<Order> inputOrders = new ArrayList<>();

        Assertions.assertThrows(NoValidNameException.class, () -> new Coffee(0L, name, 199.9, inputOrders));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.000001, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY})
    void shouldThrowNoValidPriceException_WhenConstructorPriceIncorrect(Double price) {
        List<Order> inputOrders = new ArrayList<>();

        Assertions.assertThrows(NoValidPriceException.class, () -> new Coffee(0L, "Frapuchino", price, inputOrders));
    }


    @ParameterizedTest
    @ValueSource(longs = {0, 1, 99})
    void shouldReturnSameLong_WhenGetId(Long id) {
        Coffee coffee = new Coffee(id, "Frapuchino", 199.9, new ArrayList<>());

        assertEquals(id, coffee.getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 99})
    void shouldSetCorrectLong_WhenSetId(Long id) {
        Coffee coffee = new Coffee();

        coffee.setId(id);

        assertEquals(id, coffee.getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {-99, -2, -1})
    void shouldThrowNoValidIdException_WhenSetIncorrectId(Long id) {
        Coffee coffee = new Coffee();

        Assertions.assertThrows(NoValidIdException.class, () -> coffee.setId(id));
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullId() {
        Coffee coffee = new Coffee();

        Assertions.assertThrows(NullParamException.class, () -> coffee.setId(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ping pong", "Abra kadabra", "John Doe"})
    void shouldReturnCorrectName_WhenGetName(String name) {
        Coffee coffee = new Coffee(0L, name, 0.1, List.of());

        assertEquals(name, coffee.getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ping pong", "Abra kadabra", "John Doe"})
    void shouldSetCorrectName_WhenSetName(String name) {
        Coffee coffee = new Coffee();

        coffee.setName(name);

        assertEquals(name, coffee.getName());
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullName() {
        Coffee coffee = new Coffee();

        Assertions.assertThrows(NullParamException.class, () -> coffee.setName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void shouldThrowNoValidNameException_WhenSetIncorrectName(String name) {
        Coffee coffee = new Coffee();

        Assertions.assertThrows(NoValidNameException.class, () -> coffee.setName(name));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 22.0})
    void shouldReturnCorrectDouble_WhenGetPrice(Double price) {
        Coffee coffee = new Coffee(0L, "Frapuchino", price, List.of());

        assertEquals(price, coffee.getPrice());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 22.0})
    void shouldSetCorrectDouble_WhenSetPrice(Double price) {
        Coffee coffee = new Coffee();

        coffee.setPrice(price);

        assertEquals(price, coffee.getPrice());
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullPrice() {
        Coffee coffee = new Coffee();
        Assertions.assertThrows(NullParamException.class, () -> coffee.setPrice(null));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, -0.3, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void shouldThrowNoValidPriceException_WhenSetIncorrectPrice(Double price) {
        Coffee coffee = new Coffee();

        Assertions.assertThrows(NoValidPriceException.class, () -> coffee.setPrice(price));
    }


    @Test
    void shouldReturnEmptyList_WhenGetOrderListWithoutDefinition() {
        Coffee coffee = new Coffee();

        assertEquals(new ArrayList<Order>(), coffee.getOrderList());
    }

    @Test
    void shouldReturnCorrectList_WhenGetOrderList() {
        List<Order> specifiedOrderList = List.of(Mockito.mock(Order.class), Mockito.mock(Order.class));
        Coffee coffee = new Coffee(0L, "frappuchino", 0.0, specifiedOrderList);

        assertEquals(specifiedOrderList, coffee.getOrderList());
    }

    @Test
    void shouldSetCorrectList_WhenSetOrderList() {
        List<Order> specifiedOrderList = List.of(Mockito.mock(Order.class), Mockito.mock(Order.class));
        Coffee coffee = new Coffee();

        coffee.setOrderList(specifiedOrderList);

        assertEquals(specifiedOrderList, coffee.getOrderList());

    }

    @Test
    void shouldThrowNullParamException_WhenSetNullList() {
        Coffee coffee = new Coffee();

        Assertions.assertThrows(NullParamException.class, () -> coffee.setOrderList(null));
    }

    @Test
    void shouldReturnTrue_WhenEquals() {
        Coffee coffee1 = new Coffee(0L, "John Doe", 199.0, new ArrayList<>());
        Coffee coffee2 = new Coffee(0L, "John Doe", 129.0, new ArrayList<>());
        Coffee coffee3 = new Coffee(0L, "Wow", 439.0, new ArrayList<>());

        assertEquals(coffee1, coffee2);
        assertEquals(coffee1, coffee3);
        assertEquals(coffee2, coffee3);
    }

    @Test
    void shouldReturnFalse_WhenNotEquals() {
        Coffee coffee1 = new Coffee(0L, "John Doe", 199.0, new ArrayList<>());

        assertNotEquals(null, coffee1);
        assertNotEquals(new Object(), coffee1);
    }

    @Test
    void shouldReturnSameHashCode_WhenEquals() {
        Coffee coffee1 = new Coffee(0L, "John Doe", 199.0, new ArrayList<>());
        Coffee coffee2 = new Coffee(0L, "John Doe", 129.0, new ArrayList<>());
        Coffee coffee3 = new Coffee(0L, "Wow", 439.0, new ArrayList<>());

        assertEquals(coffee1.hashCode(), coffee2.hashCode());
        assertEquals(coffee1.hashCode(), coffee3.hashCode());
        assertEquals(coffee2.hashCode(), coffee3.hashCode());
    }

}
package com.example.rest.entity;

import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NoValidNameException;
import com.example.rest.entity.exception.NoValidTipSizeException;
import com.example.rest.entity.exception.NullParamException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BaristaTest {
    Random random = new Random();

    @Test
    void shouldReturnBarista_WhenCorrectParams() {
        Long expectedId = 0L;
        String expectedFullName = "John Doe";
        List<Order> expectedOrderList = new ArrayList<>();
        Double expectedTipSize = 0.3;

        Barista barista = new Barista(expectedId, expectedFullName, expectedOrderList, expectedTipSize);

        assertEquals(expectedId, barista.getId());
        assertEquals(expectedFullName, barista.getFullName());
        assertEquals(expectedOrderList, barista.getOrderList());
        assertEquals(expectedTipSize, barista.getTipSize());
    }

    @Test
    void shouldThrowNullParamException_WhenNullParam() {
        ArrayList<Order> inputOrders = new ArrayList<>();
        Assertions.assertThrows(NullParamException.class, () -> new Barista(null, "John Doe", inputOrders, 0.3));
        Assertions.assertThrows(NullParamException.class, () -> new Barista(0L, null, inputOrders, 0.3));
        Assertions.assertThrows(NullParamException.class, () -> new Barista(0L, "John Doe", null, 0.3));
        Assertions.assertThrows(NullParamException.class, () -> new Barista(0L, "John Doe", inputOrders, null));
    }

    @ParameterizedTest
    @ValueSource(longs = {-99, -10, -1})
    void shouldThrowNoValidIdException_WhenIdLessZero(Long id) {
        ArrayList<Order> inputOrders = new ArrayList<>();

        Assertions.assertThrows(NoValidIdException.class, () -> new Barista(id, "John Doe", inputOrders, 0.3));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void shouldThrowNoValidNameException_WhenIncorrectName(String name) {
        ArrayList<Order> inputOrders = new ArrayList<>();

        Assertions.assertThrows(NoValidNameException.class, () -> new Barista(0L, name, inputOrders, 0.3));
        Assertions.assertThrows(NoValidNameException.class, () -> new Barista(name, 0.3));
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, -0.3, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void shouldThrowNoValidTipSizeException_WhenTipSizeIncorrect(Double tipSize) {
        ArrayList<Order> inputOrders = new ArrayList<>();

        Assertions.assertThrows(NoValidTipSizeException.class, () -> new Barista(0L, "John Doe", inputOrders, tipSize));
        Assertions.assertThrows(NoValidTipSizeException.class, () -> new Barista("John Doe", tipSize));
    }

    // name - tip constructor
    @Test
    void shouldReturnBarista_WhenParamsCorrect() {
        String expectedFullName = "John Doe";
        Double expectedTipSize = 0.3;

        Barista barista = new Barista(expectedFullName, expectedTipSize);

        assertThrows(NoValidIdException.class, barista::getId);
        assertEquals(expectedFullName, barista.getFullName());
        assertEquals(new ArrayList<>(), barista.getOrderList());
        assertEquals(expectedTipSize, barista.getTipSize());
    }

    @Test
    void shouldThrowNullParamException_WhenParamNull() {
        Assertions.assertThrows(NullParamException.class, () -> new Barista(null, 0.3));
        Assertions.assertThrows(NullParamException.class, () -> new Barista("John Doe", null));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 99})
    void shouldReturnSameLong_WhenGetId(Long id) {
        Barista barista = new Barista(id, "John Doe", new ArrayList<>(), 0.1);

        assertEquals(id, barista.getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, 1, 99})
    void shouldSetCorrectLong_WhenSetId(Long id) {
        Barista barista = new Barista();

        Assertions.assertDoesNotThrow(() -> barista.setId(id));

        assertEquals(id, barista.getId());

    }

    @ParameterizedTest
    @ValueSource(longs = {-99, -2, -1})
    void shouldThrowNoValidIdException_WhenSetIncorrectId(Long id) {
        Barista barista = new Barista();

        Assertions.assertThrows(NoValidIdException.class, () -> barista.setId(id));
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullId() {
        Barista barista = new Barista();

        Assertions.assertThrows(NullParamException.class, () -> barista.setId(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Ping pong", "Abra kadabra", "John Doe"})
    void shouldReturnSameFullName_WhenGetFullName(String name) {
        Barista barista = new Barista(name, 0.1);

        assertEquals(name, barista.getFullName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Kizaru", "Alabasta", "Island"})
    void shouldSetCorrectName_WhenSetFullName(String fullName) {
        Barista barista = new Barista();

        barista.setFullName(fullName);

        assertEquals(fullName, barista.getFullName());
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullName() {
        Barista barista = new Barista();

        Assertions.assertThrows(NullParamException.class, () -> barista.setFullName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {""})
    void shouldThrowNoValidNameException_WhenSetIncorrectName(String name) {
        Barista barista = new Barista();

        Assertions.assertThrows(NoValidNameException.class, () -> barista.setFullName(name));
    }

    @Test
    void shouldReturnEmptyList_WhenGetOrderListWithoutDefinition() {
        Barista barista = new Barista();

        assertEquals(new ArrayList<Order>(), barista.getOrderList());
    }

    @Test
    void shouldSetEqualsListButNotSame_WhenSetOrderList() {
        Barista barista = new Barista();
        List<Order> orders = List.of(
                Mockito.mock(Order.class),
                Mockito.mock(Order.class),
                Mockito.mock(Order.class)
        );

        barista.setOrderList(orders);

        assertEquals(orders, barista.getOrderList());
        assertNotSame(orders, barista.getOrderList());
    }

    @Test
    void shouldThrowNullParamException_WhenSetNullList() {
        Barista barista = new Barista();

        Assertions.assertThrows(NullParamException.class, () -> barista.setOrderList(null));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 22.0})
    void shouldReturnSameTipSize_WhenGetTipSize(Double tipSize) {
        Barista barista = new Barista("John Doe", tipSize);

        assertEquals(tipSize, barista.getTipSize());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 0.1, 22.0})
    void shouldSetCorrectTipSize_WhenSetTipSize(Double tipSize) {
        Barista barista = new Barista("John Doe", 0.1);

        barista.setTipSize(tipSize);

        assertEquals(tipSize, barista.getTipSize());
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, -0.3, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void shouldThrowNoValidTipSizeException_WhenSetIncorrectTipSize(Double tipSizeTest) {
        Barista barista = new Barista();

        Assertions.assertThrows(NoValidTipSizeException.class, () -> barista.setTipSize(tipSizeTest));
    }

    @Test
    void shouldThrowNullParamException_WhenNullTipSize() {
        Barista barista = new Barista();

        Assertions.assertThrows(NullParamException.class, () -> barista.setTipSize(null));
    }

    @Test
    void shouldReturnTrue_WhenEquals() {
        Barista barista1 = new Barista(0L, "John Doe", new ArrayList<>(), 0.3);
        Barista barista2 = new Barista(0L, "John Doe", new ArrayList<>(), 0.3);
        Barista barista3 = new Barista(0L, "Wow", new ArrayList<>(), 0.1);

        assertEquals(barista2, barista1);
        assertEquals(barista2, barista3);
        assertEquals(barista1, barista3);

        assertNotEquals(null, barista1);
        assertNotEquals(new Object(), barista1);
    }

    @Test
    void shouldReturnFalse_WhenNotEquals() {
        Barista barista1 = new Barista(0L, "John Doe", new ArrayList<>(), 0.3);

        assertNotEquals(null, barista1);
        assertNotEquals(new Object(), barista1);
    }

    @Test
    void shouldReturnSameHashCode_WhenEquals() {
        Barista barista1 = new Barista(0L, "John Doe", new ArrayList<>(), 0.3);
        Barista barista2 = new Barista(0L, "John Doe", new ArrayList<>(), 0.3);
        Barista barista3 = new Barista(0L, "Wow", new ArrayList<>(), 0.1);

        assertEquals(barista1.hashCode(), barista2.hashCode());
        assertEquals(barista1.hashCode(), barista3.hashCode());
        assertEquals(barista2.hashCode(), barista3.hashCode());
    }


}
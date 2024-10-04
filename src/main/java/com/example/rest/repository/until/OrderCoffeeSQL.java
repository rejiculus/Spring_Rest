package com.example.rest.repository.until;

public enum OrderCoffeeSQL {
    UPDATE_PAIRS("INSERT INTO order_coffee (order_id, coffee_id) VALUES (?, ?) ON CONFLICT DO NOTHING"),
    DELETE_BY_ORDER_ID("DELETE FROM order_coffee WHERE order_id=?"),
    DELETE_PAIR("DELETE FROM order_coffee WHERE order_id=? AND coffee_id=?"),
    DELETE_BY_COFFEE_ID("DELETE FROM order_coffee WHERE coffee_id=?");

    private final String sql;

    OrderCoffeeSQL(String sql) {
        this.sql = sql;
    }

    @Override
    public String toString() {
        return sql;
    }
}

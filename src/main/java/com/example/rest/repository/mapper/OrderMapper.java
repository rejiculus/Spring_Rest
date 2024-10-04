package com.example.rest.repository.mapper;

import com.example.rest.entity.Barista;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.BaristaNotFoundException;
import com.example.rest.entity.exception.NullParamException;
import com.example.rest.repository.BaristaRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderMapper implements SimpleMapper<Order> {
    private final BaristaRepository baristaRepository;

    public OrderMapper(BaristaRepository baristaRepository) {
        this.baristaRepository = baristaRepository;
    }

    /**
     * Mapping result set to Order object.
     *
     * @param resultSet result data from sql query.
     * @return Order object.
     * @throws NullParamException       when required params is not defined.
     * @throws BaristaNotFoundException when specified barista is not found in db.
     */
    @Override
    public Order map(ResultSet resultSet) {
        try {
            int idColumn = resultSet.findColumn("id");
            int priceColumn = resultSet.findColumn("price");
            int baristaColumn = resultSet.findColumn("barista");

            Long id = resultSet.getLong(idColumn);
            Timestamp createTimestamp = resultSet.getTimestamp("created");
            Timestamp completedTimestamp = resultSet.getTimestamp("completed");
            LocalDateTime created;
            LocalDateTime completed;

            if (createTimestamp != null)
                created = createTimestamp.toLocalDateTime();
            else created = null;

            if (completedTimestamp != null)
                completed = completedTimestamp.toLocalDateTime();
            else completed = null;

            Double price = resultSet.getDouble(priceColumn);


            Long baristaId = resultSet.getLong(baristaColumn);
            Barista barista = baristaRepository.findById(baristaId)
                    .orElseThrow(() -> new BaristaNotFoundException(baristaId));

            Order order = new Order(id, barista, new ArrayList<>(), created, completed, price);

            order.setCoffeeList(new ArrayList<>());//lazy load

            return order;
        } catch (SQLException e) {
            throw new NullParamException();
        }
    }
}

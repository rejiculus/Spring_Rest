package com.example.rest.repository;

import com.example.rest.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface to interact with order in db.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {
            "id",
            "barista",
            "coffeeList",
            "created",
            "completed",
            "price"

    })
    @Override
    List<Order> findAll();

    @EntityGraph(attributePaths = {
            "id",
            "barista",
            "coffeeList",
            "created",
            "completed",
            "price"

    })
    @Override
    List<Order> findAllById(Iterable<Long> longs);

    @EntityGraph(attributePaths = {
            "id",
            "barista",
            "coffeeList",
            "created",
            "completed",
            "price"

    })
    @Override
    Page<Order> findAll(Pageable pageable);
}

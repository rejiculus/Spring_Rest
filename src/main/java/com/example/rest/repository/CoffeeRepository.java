package com.example.rest.repository;

import com.example.rest.entity.Coffee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Interface to interaction with Coffee entity in db.
 */
@Repository
public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

    @EntityGraph(attributePaths = {
            "id",
            "name",
            "price",
            "orderList"
    })
    @Override
    Page<Coffee> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {
            "id",
            "name",
            "price",
            "orderList"
    })
    @Override
    List<Coffee> findAll();

    @EntityGraph(attributePaths = {
            "id",
            "name",
            "price",
            "orderList"
    })
    @Override
    List<Coffee> findAllById(Iterable<Long> longs);
}

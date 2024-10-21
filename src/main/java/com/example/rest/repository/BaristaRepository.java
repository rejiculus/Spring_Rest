package com.example.rest.repository;

import com.example.rest.entity.Barista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface to interaction with barista in db.
 */
@Repository
public interface BaristaRepository extends JpaRepository<Barista, Long> {

    @EntityGraph(attributePaths = {
            "id",
            "fullName",
            "tipSize",
            "orderList"
    })
    @Override
    List<Barista> findAll();

    @EntityGraph(attributePaths = {
            "id",
            "fullName",
            "tipSize",
            "orderList"
    })
    @Override
    Optional<Barista> findById(Long id);

    @EntityGraph(attributePaths = {
            "id",
            "fullName",
            "tipSize",
            "orderList"
    })
    @Override
    Page<Barista> findAll(Pageable page);

}

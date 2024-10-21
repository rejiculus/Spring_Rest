package com.example.rest.service;

import com.example.rest.service.dto.ICoffeeCreateDTO;
import com.example.rest.service.dto.ICoffeePublicDTO;
import com.example.rest.service.dto.ICoffeeUpdateDTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Interface to interacting with coffee's in db.
 */
public interface ICoffeeService {

    /**
     * Creating coffee by ICoffeeCreateDTO.
     *
     * @param coffeeDTO object with ICoffeeCreateDTO type.
     * @return Coffee object.
     */
    ICoffeePublicDTO create(@Valid ICoffeeCreateDTO coffeeDTO);

    /**
     * Updating coffee by ICoffeeUpdateDTO.
     * Deleting references that has in db but hasn't in coffeeDTO.
     * Add references that hasn't in db but has in coffeeDTO.
     *
     * @param coffeeDTO object with ICoffeeUpdateDTO type.
     * @return Coffee object.
     */
    ICoffeePublicDTO update(@Valid ICoffeeUpdateDTO coffeeDTO);

    /**
     * Delete coffee with specified id.
     *
     * @param id deleting coffee id.
     */
    void delete(Long id);

    /**
     * Find coffee by specified id.
     *
     * @param id finding coffee's id.
     * @return Coffee order with specified id.
     */
    ICoffeePublicDTO findById(Long id);

    /**
     * Find all coffees.
     *
     * @return all coffee from db.
     */
    List<? extends ICoffeePublicDTO> findAll();

    /**
     * Find all coffee grouping by pages and limited.
     *
     * @param page  number of representing page. Can't be less than zero.
     * @param limit number maximum represented objects.
     * @return list of object from specified page. Maximum number object in list equals limit.
     */
    List<? extends ICoffeePublicDTO> findAllByPage(int page, int limit);
}

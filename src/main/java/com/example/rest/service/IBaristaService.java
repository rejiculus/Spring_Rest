package com.example.rest.service;

import com.example.rest.service.dto.IBaristaCreateDTO;
import com.example.rest.service.dto.IBaristaPublicDTO;
import com.example.rest.service.dto.IBaristaUpdateDTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Interface to interacting with barista's in db.
 */
public interface IBaristaService {
    /**
     * Creating barista by IBaristaCreateDTO.
     *
     * @param baristaDTO object with IBaristaCreateDTO type.
     * @return Barista with specified id.
     */
    IBaristaPublicDTO create(@Valid IBaristaCreateDTO baristaDTO);

    /**
     * Updating barista by IBaristaUpdateDTO.
     * Set default barista to orders that already not contains in orderIdList.
     * Adding reference with orders that already contains in orderIdList.
     *
     * @param baristaDTO object with IBaristaUpdateDTO type.
     * @return Barista object.
     */
    IBaristaPublicDTO update(@Valid IBaristaUpdateDTO baristaDTO);

    /**
     * Delete barista from db.
     * Updating couped orders, setting default barista.
     *
     * @param id id of the barista to be deleted
     */
    void delete(Long id);

    /**
     * Found barista by id.
     *
     * @param id barista's id.
     * @return Barista's object.
     */
    IBaristaPublicDTO findById(Long id);

    /**
     * Found all barista.
     *
     * @return list of barista's objects
     */
    List<? extends IBaristaPublicDTO> findAll();

    /**
     * Found all barista, grouped by page and limited.
     *
     * @param page  number of page. Can't be less than zero.
     * @param limit number of maximum objects in list.
     * @return list of barista's objects
     */
    List<? extends IBaristaPublicDTO> findAllByPage(int page, int limit);
}

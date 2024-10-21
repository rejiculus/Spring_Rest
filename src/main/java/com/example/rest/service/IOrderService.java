package com.example.rest.service;

import com.example.rest.service.dto.IOrderCreateDTO;
import com.example.rest.service.dto.IOrderPublicDTO;
import com.example.rest.service.dto.IOrderUpdateDTO;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Interface to interacting with order's in db.
 */
public interface IOrderService {

    /**
     * Create 'order' in db by IOrderCreateDTO.
     *
     * @param orderDTO object with IOrderCreateDTO type.
     * @return Order object.
     */
    IOrderPublicDTO create(@Valid IOrderCreateDTO orderDTO);

    /**
     * Update 'order' in db by IOrderUpdateDTO.
     *
     * @param orderDTO object with IOrderUpdateDTO type.
     * @return updated Order object.
     */
    IOrderPublicDTO update(@Valid IOrderUpdateDTO orderDTO);

    /**
     * Delete 'order' by specified id.
     *
     * @param id deleting order's id.
     */
    void delete(Long id);

    /**
     * Get 'order' queue. Oldest created, but not completed
     * order - first, youngest - last.
     *
     * @return list of filtered and sorted orders.
     */
    List<? extends IOrderPublicDTO> getOrderQueue();

    /**
     * Complete 'order' with specified 'id'.
     * Specifying 'completed' field in 'order'.
     *
     * @param id completing order's id.
     * @return completed order.
     */
    IOrderPublicDTO completeOrder(Long id);

    /**
     * Find all 'order' in db.
     *
     * @return list of all 'order' objects
     */
    List<? extends IOrderPublicDTO> findAll();

    /**
     * Find 'order' by specified id.
     *
     * @param id the desired order "id".
     * @return Order object with specified id.
     */
    IOrderPublicDTO findById(Long id);

    /**
     * Find all 'order' grouping by pages and limited.
     *
     * @param page  number of representing page. Can't be less than zero.
     * @param limit number maximum represented objects.
     * @return list of object from specified page. Maximum number object in list equals limit.
     */
    List<? extends IOrderPublicDTO> findAllByPage(int page, int limit);

}

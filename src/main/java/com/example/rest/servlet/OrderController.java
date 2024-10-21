package com.example.rest.servlet;

import com.example.rest.service.IOrderService;
import com.example.rest.service.dto.IOrderPublicDTO;
import com.example.rest.servlet.dto.OrderCreateDTO;
import com.example.rest.servlet.dto.OrderUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final IOrderService orderService;

    @Autowired
    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping({"", "/"})
    public List<?> findAll() {

        return orderService.findAll();
    }

    @GetMapping(value = {"", "/"}, params = {"page", "limit"})
    public List<?> findAllByPage(@RequestParam("page") int page, @RequestParam("limit") int limit) {

        return orderService.findAllByPage(page, limit);
    }

    @GetMapping({"/queue", "/queue/"})

    public List<?> getQueue() {

        return orderService.getOrderQueue();
    }

    @GetMapping({"/{id}", "/{id}/"})
    public IOrderPublicDTO findById(@PathVariable("id") Long id) {

        return orderService.findById(id);
    }

    @PostMapping({"", "/"})
    public IOrderPublicDTO create(@RequestBody OrderCreateDTO orderCreateDTO) {

        return orderService.create(orderCreateDTO);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public IOrderPublicDTO update(@PathVariable("id") Long id, @RequestBody OrderUpdateDTO orderUpdateDTO) {
        OrderUpdateDTO orderNoRefDTO = new OrderUpdateDTO(id,
                orderUpdateDTO.baristaId(),
                orderUpdateDTO.created(),
                orderUpdateDTO.completed(),
                orderUpdateDTO.price(),
                orderUpdateDTO.coffeeIdList());

        return orderService.update(orderNoRefDTO);
    }

    @PutMapping({"/{id}/complete", "/{id}/complete/"})
    public IOrderPublicDTO complete(@PathVariable("id") Long id) {
        return orderService.completeOrder(id);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public void delete(@PathVariable("id") Long id) {
        orderService.delete(id);
    }

}

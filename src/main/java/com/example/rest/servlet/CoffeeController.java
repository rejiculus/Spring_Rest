package com.example.rest.servlet;

import com.example.rest.service.ICoffeeService;
import com.example.rest.service.dto.ICoffeePublicDTO;
import com.example.rest.servlet.dto.CoffeeCreateDTO;
import com.example.rest.servlet.dto.CoffeeUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coffees")
public class CoffeeController {
    private final ICoffeeService coffeeService;


    @Autowired
    public CoffeeController(ICoffeeService coffeeService) {
        this.coffeeService = coffeeService;
    }

    /**
     * Send all coffee objects that found using the service.
     * Set status OK.
     */
    @GetMapping({"", "/"})
    public List<ICoffeePublicDTO> findAll() {
        return (List<ICoffeePublicDTO>) coffeeService.findAll();
    }

    /**
     * Send coffee object found by id using the service.
     * Set status OK.
     *
     * @param id
     */
    @GetMapping({"/{id}", "/{id}/"})
    public ICoffeePublicDTO findById(@PathVariable("id") Long id) {
        return coffeeService.findById(id);
    }

    @GetMapping(value = {"", "/"}, params = {"page", "limit"})
    public List<ICoffeePublicDTO> findAllByPage(@RequestParam("page") int page, @RequestParam("limit") int limit) {

        return (List<ICoffeePublicDTO>) coffeeService.findAllByPage(page, limit);
    }

    @PostMapping({"", "/"})
    public ICoffeePublicDTO create(@RequestBody CoffeeCreateDTO coffeeCreateDTO) {

        return coffeeService.create(coffeeCreateDTO);
    }

    @PutMapping({"/{id}", "/{id}/"})
    public ICoffeePublicDTO update(@PathVariable("id") Long id, @RequestBody CoffeeUpdateDTO coffeeUpdateDTO) {
        CoffeeUpdateDTO coffeeNoRefDTO = new CoffeeUpdateDTO(id,
                coffeeUpdateDTO.name(),
                coffeeUpdateDTO.price(),
                coffeeUpdateDTO.orderIdList());
        return coffeeService.update(coffeeNoRefDTO);
    }

    @DeleteMapping({"/{id}", "/{id}/"})
    public void delete(@PathVariable("id") Long id) {
        coffeeService.delete(id);
    }

}

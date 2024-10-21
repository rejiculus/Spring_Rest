package com.example.rest.servlet;

import com.example.rest.service.IBaristaService;
import com.example.rest.service.dto.IBaristaPublicDTO;
import com.example.rest.servlet.dto.BaristaCreateDTO;
import com.example.rest.servlet.dto.BaristaUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/baristas", "/baristas/"})

public class BaristaController {
    private final IBaristaService baristaService;

    @Autowired
    public BaristaController(IBaristaService baristaService) {
        this.baristaService = baristaService;
    }

    /**
     * Send to response all barista objects using the service.
     * Set status OK.
     */
    @GetMapping({"", "/"})
    public List<IBaristaPublicDTO> findAll() {
        return (List<IBaristaPublicDTO>) baristaService.findAll();
    }

    /**
     * Send to response all barista objects using the service grouped by page.
     * Set status OK.
     *
     * @param page  number of page.
     * @param limit maximum objects in page.
     */
    @GetMapping(value = {"", "/"}, params = {"page", "limit"})
    public List<IBaristaPublicDTO> findAllByPage(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return (List<IBaristaPublicDTO>) baristaService.findAllByPage(page, limit);
    }

    /**
     * Send to response barista object with specified id using the service grouped by page.
     * Set status OK.
     *
     * @param id searched barista's id.
     */
    @GetMapping({"/{id}", "/{id}/"})
    public IBaristaPublicDTO findById(@PathVariable("id") Long id) {

        return baristaService.findById(id);
    }

    /**
     * Create barista using the service and send it back with defined id.
     */
    @PostMapping({"", "/"})
    public IBaristaPublicDTO create(@RequestBody BaristaCreateDTO baristaCreateDTO) {

        return baristaService.create(baristaCreateDTO);
    }

    /**
     * Update barista using the service.
     *
     * @param id updated barista id from url.
     */
    @PutMapping({"/{id}", "/{id}/"})
    public IBaristaPublicDTO update(@PathVariable("id") Long id, @RequestBody BaristaUpdateDTO baristaUpdateDTO) {
        BaristaUpdateDTO baristaDTO = new BaristaUpdateDTO(id,
                baristaUpdateDTO.fullName(),
                baristaUpdateDTO.tipSize());

        return baristaService.update(baristaDTO);
    }

    /**
     * Delete barista with specified id using the service.
     *
     * @param id id of the barista to be deleted
     */
    @DeleteMapping({"/{id}", "/{id}/"})
    public void delete(@PathVariable("id") Long id) {
        baristaService.delete(id);
    }

}

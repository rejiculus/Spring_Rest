package com.example.rest.service.imp;

import com.example.rest.entity.Coffee;
import com.example.rest.entity.exception.*;
import com.example.rest.repository.CoffeeRepository;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.ICoffeeService;
import com.example.rest.service.dto.ICoffeeCreateDTO;
import com.example.rest.service.dto.ICoffeePublicDTO;
import com.example.rest.service.dto.ICoffeeUpdateDTO;
import com.example.rest.service.mapper.CoffeeMapper;
import com.example.rest.servlet.dto.CoffeePublicDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Service to processing coffee entity.
 */
@Service
@Validated
public class CoffeeService implements ICoffeeService {
    private final CoffeeRepository coffeeRepository;
    private final CoffeeMapper mapper;


    /**
     * Constructor based on repositories.
     * Create mapper by orderRepository.
     *
     * @param coffeeRepository repository to interact with coffee in db.
     * @throws NullParamException when orderRepository of coffeeRepository is null.
     */
    @Autowired
    public CoffeeService(CoffeeRepository coffeeRepository, CoffeeMapper mapper) {
        if (coffeeRepository == null || mapper == null)
            throw new NullParamException();
        this.coffeeRepository = coffeeRepository;
        this.mapper = mapper;
    }

    /**
     * Creating coffee by ICoffeeCreateDTO.
     *
     * @param coffeeDTO object with ICoffeeCreateDTO type.
     * @return Coffee object.
     * @throws NullParamException    when coffeeDTO is null or it's fields is null.
     * @throws NoValidNameException  when coffeeDTO's name is empty.
     * @throws NoValidPriceException when coffeeDTO's price is NaN, Infinite or less than zero.
     */
    @Override
    @Transactional
    public ICoffeePublicDTO create(@Valid ICoffeeCreateDTO coffeeDTO) {
        if (coffeeDTO == null)
            throw new NullParamException();

        Coffee coffee = mapper.createDtoToEntity(coffeeDTO);
        coffee = this.coffeeRepository.save(coffee);
        return mapper.entityToDto(coffee);
    }

    /**
     * Updating coffee by ICoffeeUpdateDTO.
     * Deleting references that has in db but hasn't in coffeeDTO.
     * Add references that hasn't in db but has in coffeeDTO.
     *
     * @param coffeeDTO object with ICoffeeUpdateDTO type.
     * @return Coffee object.
     * @throws NullParamException      when coffeeDTO is null or some of it fields is null.
     * @throws NoValidIdException      form mapper, when coffeeDTO's id is less than zero.
     * @throws NoValidNameException    form mapper, when coffeeDTO's name is empty.
     * @throws NoValidPriceException   from mapper, when coffeeDTO's price is NaN, Infinite or less than zero.
     * @throws CoffeeNotFoundException when coffee with this id is not found.
     * @throws OrderNotFoundException  when order for coffee's orderList is not found.
     */
    @Override
    @Transactional
    public ICoffeePublicDTO update(@Valid ICoffeeUpdateDTO coffeeDTO) {
        if (coffeeDTO == null)
            throw new NullParamException();

        Coffee coffee = coffeeRepository.findById(coffeeDTO.id())
                .orElseThrow(() -> new CoffeeNotFoundException(coffeeDTO.id()));

        coffee = mapper.updateDtoToEntity(coffeeDTO);

        coffee = this.coffeeRepository.save(coffee);

        return mapper.entityToDto(coffee);
    }

    /**
     * Delete coffee with specified id.
     *
     * @param id deleting coffee id.
     * @throws NullParamException      when id is null.
     * @throws NoValidIdException      when id is less than zero.
     * @throws CoffeeNotFoundException when coffee with specific id is not found.
     */
    @Override
    public void delete(Long id) {
        if (id == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);

        this.coffeeRepository.deleteById(id);
    }

    /**
     * Find coffee by specified id.
     *
     * @param id finding coffee's id.
     * @return Coffee order with specified id.
     * @throws NullParamException      when id param is null.
     * @throws NoValidIdException      when id less than zero.
     * @throws CoffeeNotFoundException when coffee with specified id is not found.
     */
    @Override
    @Transactional
    public ICoffeePublicDTO findById(Long id) {
        if (id == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);

        Coffee coffee = this.coffeeRepository.findById(id)
                .orElseThrow(() -> new CoffeeNotFoundException(id));

        return mapper.entityToDto(coffee);
    }


    /**
     * Find all coffees.
     *
     * @return all coffee from db.
     */
    @Override
    @Transactional
    public List<CoffeePublicDTO> findAll() {
        List<Coffee> coffeeList = this.coffeeRepository.findAll();

        return coffeeList.stream()
                .map(mapper::entityToDto)
                .toList();
    }

    /**
     * Find all coffee grouping by pages and limited.
     *
     * @param page  number of representing page. Can't be less than zero.
     * @param limit number maximum represented objects.
     * @return list of object from specified page. Maximum number object in list equals limit.
     * @throws NoValidPageException  when page is less than zero.
     * @throws NoValidLimitException when limit is less than one.
     */
    @Override
    @Transactional
    public List<CoffeePublicDTO> findAllByPage(int page, int limit) {
        if (page < 0)
            throw new NoValidPageException(page);
        if (limit <= 0)
            throw new NoValidLimitException(limit);

        Pageable pageable = PageRequest.of(page, limit);

        Page<Coffee> coffeeList = this.coffeeRepository.findAll(pageable);

        return coffeeList.stream()
                .map(mapper::entityToDto)
                .toList();
    }
}

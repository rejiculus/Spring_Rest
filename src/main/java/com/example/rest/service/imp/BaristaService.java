package com.example.rest.service.imp;

import com.example.rest.entity.Barista;
import com.example.rest.entity.exception.*;
import com.example.rest.repository.BaristaRepository;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.IBaristaService;
import com.example.rest.service.dto.IBaristaCreateDTO;
import com.example.rest.service.dto.IBaristaPublicDTO;
import com.example.rest.service.dto.IBaristaUpdateDTO;
import com.example.rest.service.mapper.BaristaMapper;
import com.example.rest.servlet.dto.BaristaPublicDTO;
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
 * Service for processing barista entity.
 */
@Service
@Validated
public class BaristaService implements IBaristaService {
    private final BaristaRepository baristaRepository;
    private final BaristaMapper mapper;

    /**
     * Constructor based on repositories. Create mapper by order repository.
     *
     * @param baristaRepository repository to interact with barista schema in db.
     * @throws NullParamException when some of params is null.
     */
    @Autowired
    public BaristaService(BaristaRepository baristaRepository, BaristaMapper mapper) {
        if (baristaRepository == null || mapper == null)
            throw new NullParamException();

        this.baristaRepository = baristaRepository;
        this.mapper = mapper;
    }

    /**
     * Creating barista by IBaristaCreateDTO.
     *
     * @param baristaDTO object with IBaristaCreateDTO type.
     * @return Barista with specified id.
     * @throws NullParamException      when baristaDTO is null, or in mapper when some fields in baristaDTO is null.
     * @throws NoValidNameException    form mapper, when baristaDTO's fullName is empty.
     * @throws NoValidTipSizeException from mapper, when baristaDTO's tipSize is NaN, Infinite or less than zero.
     */
    @Override
    @Transactional
    public IBaristaPublicDTO create(@Valid IBaristaCreateDTO baristaDTO) {
        if (baristaDTO == null)
            throw new NullParamException();

        Barista barista = mapper.createDtoToEntity(baristaDTO);
        barista = this.baristaRepository.save(barista);

        return mapper.entityToDto(barista);
    }

    /**
     * Updating barista by IBaristaUpdateDTO.
     * Set default barista to orders that already not contains in orderIdList.
     * Adding reference with orders that already contains in orderIdList.
     *
     * @param baristaDTO object with IBaristaUpdateDTO type.
     * @return Barista object.
     * @throws NullParamException       when baristaDTO is null, or in mapper when some fields in baristaDTO is null.
     * @throws NoValidIdException       from mapper, when baristaDTO's id is less than zero.
     * @throws NoValidNameException     form mapper, when baristaDTO's fullName is empty.
     * @throws NoValidTipSizeException  from mapper, when baristaDTO's tipSize is NaN, Infinite or less than zero.
     * @throws BaristaNotFoundException from baristaRepository, when id is not found in db.
     * @throws OrderNotFoundException   from orderRepository, when order form baristaDTO's orderIdList is not found in db.
     */
    @Transactional
    public IBaristaPublicDTO update(@Valid IBaristaUpdateDTO baristaDTO) {
        if (baristaDTO == null)
            throw new NullParamException();

        Barista barista = mapper.updateDtoToEntity(baristaDTO);

        barista = this.baristaRepository.save(barista);

        return mapper.entityToDto(barista);
    }

    /**
     * Delete barista from db.
     * Updating couped orders, setting default barista.
     *
     * @param id deleting barista.
     * @throws NullParamException       when id is null.
     * @throws NoValidIdException       when id is less than zero.
     * @throws BaristaNotFoundException when barista with this id is not found in db.
     */
    @Override
    public void delete(Long id) {
        if (id == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);

        this.baristaRepository.deleteById(id);
    }

    /**
     * Found barista by id.
     *
     * @param id barista's id.
     * @return Barista's object.
     * @throws NullParamException       when id is null.
     * @throws NoValidIdException       when id is less than zero.
     * @throws BaristaNotFoundException when barista with this id is not found in db.
     */
    @Override
    @Transactional
    public IBaristaPublicDTO findById(Long id) {
        if (id == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);

        Barista barista = this.baristaRepository.findById(id)
                .orElseThrow(() -> new BaristaNotFoundException(id));


        return mapper.entityToDto(barista);
    }

    /**
     * Found all barista.
     *
     * @return list of barista's objects
     */
    @Override
    @Transactional
    public List<BaristaPublicDTO> findAll() {
        List<Barista> baristaList = this.baristaRepository.findAll();
        return baristaList.stream()
                .map(mapper::entityToDto)
                .toList();
    }

    /**
     * Found all barista, grouped by page and limited.
     *
     * @param page  number of page. Can't be less than zero.
     * @param limit number of maximum objects in list.
     * @return list of barista's objects
     * @throws NoValidPageException  when page is less than zero.
     * @throws NoValidLimitException when limit is less than one.
     */
    @Override
    @Transactional
    public List<BaristaPublicDTO> findAllByPage(int page, int limit) {
        if (page < 0)
            throw new NoValidPageException(page);
        if (limit <= 0)
            throw new NoValidLimitException(limit);

        Pageable pageable = PageRequest.of(page, limit);

        Page<Barista> baristaList = this.baristaRepository.findAll(pageable);
        return baristaList.stream()
                .map(mapper::entityToDto)
                .toList();
    }

}

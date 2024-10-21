package com.example.rest.servlet;

import com.example.rest.entity.exception.*;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.exception.DuplicatedElementsException;
import com.example.rest.service.exception.OrderAlreadyCompletedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalControllerAdvice {
    public static final String DEFAULT_ERROR_VIEW = "error";
    public static final String DEBUG_INFO = "Error: {}";
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalControllerAdvice.class);

    @ExceptionHandler(value = {BaristaNotFoundException.class,
            OrderNotFoundException.class,
            CoffeeNotFoundException.class})
    protected ResponseEntity<?> notFoundHandler(RuntimeException e) {
        String message = e.getMessage();
        LOGGER.debug(message, e);
        List<String> errorList = List.of("" + message);
        return new ResponseEntity<>(getErrorsMap(errorList), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NullParamException.class, NoValidIdException.class, NoValidNameException.class,
            NoValidPageException.class, NoValidTipSizeException.class, NoValidLimitException.class,
            OrderAlreadyCompletedException.class, DuplicatedElementsException.class, NoValidPriceException.class})
    protected ResponseEntity<?> badRequestHandler(RuntimeException e) {
        String error = e.getMessage();
        LOGGER.debug(DEBUG_INFO, error, e);

        List<String> errorList = List.of("" + error);


        return new ResponseEntity<>(getErrorsMap(errorList), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        LOGGER.debug(DEBUG_INFO, errors, ex);
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        LOGGER.debug(DEBUG_INFO, errors, ex);
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }
}

package com.example.rest.servlet;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.rest.entity.Order;
import com.example.rest.entity.exception.*;
import com.example.rest.repository.exception.DataBaseException;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.IOrderService;
import com.example.rest.service.exception.OrderAlreadyCompletedException;
import com.example.rest.service.exception.OrderHasReferencesException;
import com.example.rest.servlet.adapter.LocalDateTimeAdapter;
import com.example.rest.servlet.dto.OrderCreateDTO;
import com.example.rest.servlet.dto.OrderPublicDTO;
import com.example.rest.servlet.dto.OrderUpdateDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class OrderServlet extends SimpleServlet {

    private static final Logger LOGGER = Logger.getLogger(OrderServlet.class.getName());
    private final transient IOrderService orderService;
    private final transient Gson mapper = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private static final String BAD_PATH = "Bad path! Path '%s' is not processing!";
    private static final String NOT_FOUND = "Not found: %s";
    private static final String HAS_REF = "Has references: %s";
    private static final String BAD_PARAMS = "Bad params: %s";
    private static final String SOME_DATA_BASE_EXCEPTION = "Some database error: %s";
    private static final String ALREADY_EXIST = "Already exist: %s";

    private static final String ORDER_QUEUE = "/queue/?"; // regex match "/queue" or "/queue/"
    private static final String SPECIFIED_ORDER_REGEX = "/\\d+/?"; //regex путь соответствующий "/[цифры]/" или "/[цифры]"
    private static final String SPECIFIED_ORDER_COMPLETE_REGEX = "/\\d+/complete/?"; //regex match "/[digits]/complete" or "/[digits]/complete/"

    public OrderServlet(IOrderService orderService) {
        if (orderService == null)
            throw new NullParamException();

        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            resp.setContentType("text/html");

            if (pathInfo == null || pathInfo.equals("/")) {
                Map<String, String[]> params = req.getParameterMap();
                if (params.containsKey("page") && params.containsKey("limit")) {
                    int page = Integer.parseInt(params.get("page")[0]);
                    int limit = Integer.parseInt(params.get("limit")[0]);
                    findAllByPage(page, limit, resp);
                } else {
                    findAll(resp);
                }

            } else if (pathInfo.matches(ORDER_QUEUE)) { // regex match "/queue" or "/queue/"
                getQueue(resp);

            } else if (pathInfo.matches(SPECIFIED_ORDER_REGEX)) {//regex match "/[цифры]/" or "/[цифры]"
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                findById(id, resp);

            } else {
                String message = String.format(BAD_PATH, pathInfo);
                LOGGER.severe(message);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            }
        } catch (NoValidPageException | NoValidLimitException | NullParamException | NoValidIdException |
                 NumberFormatException e) {
            String message = String.format(BAD_PARAMS, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);

        } catch (OrderNotFoundException e) {
            String message = String.format(NOT_FOUND, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, message);

        } catch (DataBaseException e) {
            String message = String.format(SOME_DATA_BASE_EXCEPTION, e.getMessage());
            LOGGER.severe(message);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);

        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void findAll(HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        List<OrderPublicDTO> orderList = orderService.findAll().stream()
                .map(OrderPublicDTO::new)
                .toList();

        String json = arrToJson(orderList, mapper);
        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    private void findAllByPage(int page, int limit, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        List<OrderPublicDTO> orderList = orderService.findAllByPage(page, limit).stream()
                .map(OrderPublicDTO::new)
                .toList();

        String json = arrToJson(orderList, mapper);
        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    private void getQueue(HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        List<OrderPublicDTO> orderDtoList = orderService.getOrderQueue().stream()
                .map(OrderPublicDTO::new)
                .toList();
        String json = arrToJson(orderDtoList, mapper);
        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    private void findById(Long id, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        Order order = orderService.findById(id);
        String json = mapper.toJson(new OrderPublicDTO(order));
        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            req.setCharacterEncoding("UTF-8");

            if (pathInfo == null || pathInfo.matches("/?")) {//regex путь соответствующий "/[цифры]/" или "/[цифры]"
                create(req, resp);

            } else {
                String message = String.format(BAD_PATH, pathInfo);
                LOGGER.info(message);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            }
        } catch (NoValidIdException | NoValidPriceException | NoValidNameException | NullParamException |
                 JsonMappingException | JsonSyntaxException | CoffeeNotFoundException |
                 BaristaNotFoundException e) {
            String message = String.format(BAD_PARAMS, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);

        } catch (DataBaseException e) {
            String message = String.format(SOME_DATA_BASE_EXCEPTION, e.getMessage());
            LOGGER.severe(message);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);

        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        OrderCreateDTO orderDTO = mapper.fromJson(request.getReader(), OrderCreateDTO.class);

        Order order = orderService.create(orderDTO);

        String json = mapper.toJson(new OrderPublicDTO(order));
        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.flushBuffer();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            req.setCharacterEncoding("UTF-8");

            if (pathInfo == null || pathInfo.equals("/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, String.format(BAD_PATH, pathInfo));

            } else if (pathInfo.matches(SPECIFIED_ORDER_REGEX)) {//regex match "/[цифры]/" or "/[цифры]"
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                update(id, req);
                resp.setStatus(HttpServletResponse.SC_OK);

            } else if (pathInfo.matches(SPECIFIED_ORDER_COMPLETE_REGEX)) { //regex match "/[digits]/complete" or "/[digits]/complete/"
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                complete(id);
                resp.setStatus(HttpServletResponse.SC_OK);

            } else {
                String message = String.format(BAD_PATH, pathInfo);
                LOGGER.severe(message);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            }
        } catch (NoValidIdException | NoValidPriceException | NoValidNameException | NullParamException |
                 JsonMappingException | NumberFormatException | JsonSyntaxException | BaristaNotFoundException |
                 CoffeeNotFoundException | DateTimeParseException e) {
            String message = String.format(BAD_PARAMS, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);

        } catch (OrderNotFoundException e) {
            String message = String.format(NOT_FOUND, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, message);

        } catch (OrderAlreadyCompletedException e) {
            String message = String.format(ALREADY_EXIST, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, message);

        } catch (DataBaseException e) {
            String message = String.format(SOME_DATA_BASE_EXCEPTION, e.getMessage());
            LOGGER.severe(message);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);

        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void update(Long id, HttpServletRequest request) throws IOException {
        OrderUpdateDTO orderDTO = mapper.fromJson(request.getReader(), OrderUpdateDTO.class);
        OrderUpdateDTO orderNoRefDTO = new OrderUpdateDTO(
                id,
                orderDTO.baristaId(),
                orderDTO.created(),
                orderDTO.completed(),
                orderDTO.price(),
                orderDTO.coffeeIdList());

        orderService.update(orderNoRefDTO);
    }

    private void complete(Long id) {
        orderService.completeOrder(id);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();

            if (pathInfo != null && pathInfo.matches(SPECIFIED_ORDER_REGEX)) {//regex путь соответствующий "/[цифры]/" или "/[цифры]"
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                delete(id);
                resp.setStatus(HttpServletResponse.SC_OK);

            } else {
                String message = String.format(BAD_PATH, pathInfo);
                LOGGER.info(message);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            }
        } catch (NullParamException | NoValidIdException | NumberFormatException e) {
            String message = String.format(BAD_PARAMS, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, message);

        } catch (OrderNotFoundException e) {
            String message = String.format(NOT_FOUND, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, message);

        } catch (OrderHasReferencesException e) {
            String message = String.format(HAS_REF, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);

        } catch (DataBaseException e) {
            String message = String.format(SOME_DATA_BASE_EXCEPTION, e.getMessage());
            LOGGER.severe(message);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);

        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }

    private void delete(Long id) {
        orderService.delete(id);
    }

}

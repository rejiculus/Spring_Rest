package com.example.rest.servlet;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.rest.entity.Barista;
import com.example.rest.entity.exception.*;
import com.example.rest.repository.exception.DataBaseException;
import com.example.rest.repository.exception.NoValidLimitException;
import com.example.rest.repository.exception.NoValidPageException;
import com.example.rest.service.IBaristaService;
import com.example.rest.servlet.adapter.LocalDateTimeAdapter;
import com.example.rest.servlet.dto.BaristaCreateDTO;
import com.example.rest.servlet.dto.BaristaPublicDTO;
import com.example.rest.servlet.dto.BaristaUpdateDTO;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class BaristaServlet extends SimpleServlet {
    private static final Logger LOGGER = Logger.getLogger(BaristaServlet.class.getName());
    private final transient IBaristaService baristaService;
    private final transient Gson mapper = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private static final String BAD_PATH = "Bad path! Path '%s' is not processing!";
    private static final String NOT_FOUND = "Not found: %s";
    private static final String BAD_PARAMS = "Bad params: %s";
    private static final String SOME_DATA_BASE_EXCEPTION = "Some database error: %s";

    private static final String SPECIFIED_BARISTA_REGEX = "/\\d+/?"; //regex путь соответствующий "/[цифры]/" или "/[цифры]"

    public BaristaServlet(IBaristaService baristaService) {
        if (baristaService == null)
            throw new NullParamException();

        this.baristaService = baristaService;
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

            } else if (pathInfo.matches(SPECIFIED_BARISTA_REGEX)) {//regex путь соответствующий "/[цифры]/" или "/[цифры]"
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                findById(id, resp);

            } else {
                String message = String.format(BAD_PATH, pathInfo);
                LOGGER.info(message);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            }

        } catch (NoValidPageException | NoValidLimitException | NullParamException | NoValidIdException |
                 NumberFormatException e) {
            String message = String.format(BAD_PARAMS, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);

        } catch (BaristaNotFoundException e) {
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

    /**
     * Send to response all barista objects using the service.
     * Set status OK.
     *
     * @param response used to send response.
     */
    private void findAll(HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        List<BaristaPublicDTO> baristaDtoList = baristaService.findAll().stream()
                .map(BaristaPublicDTO::new)
                .toList();

        String json = arrToJson(baristaDtoList, mapper);

        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    /**
     * Send to response all barista objects using the service grouped by page.
     * Set status OK.
     *
     * @param page     number of page.
     * @param limit    maximum objects in page.
     * @param response used to send response.
     */
    private void findAllByPage(int page, int limit, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        List<BaristaPublicDTO> baristaDtoList = baristaService.findAllByPage(page, limit)
                .stream()
                .map(BaristaPublicDTO::new)
                .toList();

        String json = arrToJson(baristaDtoList, mapper);

        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    /**
     * Send to response barista object with specified id using the service grouped by page.
     * Set status OK.
     *
     * @param id       searched barista's id.
     * @param response used to send response.
     */
    private void findById(Long id, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        Barista barista = baristaService.findById(id);
        String json = mapper.toJson(new BaristaPublicDTO(barista));

        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_OK);
        response.flushBuffer();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            req.setCharacterEncoding("UTF-8");

            if (pathInfo == null || pathInfo.matches("/")) {//regex путь соответствующий "/" или ""
                create(req, resp);
            } else {
                String message = String.format(BAD_PATH, pathInfo);
                LOGGER.info(message);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            }

        } catch (NoValidIdException | NoValidTipSizeException | NoValidNameException |
                 NullParamException | JsonMappingException | NumberFormatException | JsonSyntaxException e) {
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

    /**
     * Create barista using the service and send it back with defined id.
     *
     * @param request  income data with BaristaCreateDTO json.
     * @param response outgoing data.
     */
    private void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter printWriter = response.getWriter();
        BaristaCreateDTO baristaDTO = mapper.fromJson(request.getReader(), BaristaCreateDTO.class);
        Barista barista = baristaService.create(baristaDTO);

        String json = mapper.toJson(new BaristaPublicDTO(barista));
        printWriter.write(json);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.flushBuffer();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && pathInfo.matches(SPECIFIED_BARISTA_REGEX)) {//regex путь соответствующий "/[цифры]/" или "/[цифры]"
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                update(id, req);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.flushBuffer();

            } else {
                String message = String.format(BAD_PATH, pathInfo);
                LOGGER.info(message);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            }

        } catch (NoValidIdException | NoValidTipSizeException | NoValidNameException |
                 NullParamException | NumberFormatException | JsonMappingException |
                 JsonSyntaxException | OrderNotFoundException e) {
            String message = String.format(BAD_PARAMS, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);

        } catch (BaristaNotFoundException e) {
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

    /**
     * Update barista using the service.
     *
     * @param id      updated barista id from url.
     * @param request income data with BaristaUpdateDTO json.
     */
    private void update(Long id, HttpServletRequest request) throws IOException {
        BaristaUpdateDTO barista = mapper.fromJson(request.getReader(), BaristaUpdateDTO.class);
        BaristaUpdateDTO baristaDTO = new BaristaUpdateDTO(id, barista.fullName(), barista.tipSize(), barista.orderIdList());
        baristaService.update(baristaDTO);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo != null && pathInfo.matches(SPECIFIED_BARISTA_REGEX)) {//regex путь соответствующий "/[цифры]/" или "/[цифры]"
                Long id = Long.parseLong(pathInfo.split("/")[1]);
                delete(id);
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.flushBuffer();

            } else {
                String message = String.format(BAD_PATH, pathInfo);
                LOGGER.info(message);
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
            }
        } catch (NumberFormatException | NullParamException | NoValidIdException e) {
            String message = String.format(BAD_PARAMS, e.getMessage());
            LOGGER.info(message);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, message);

        } catch (BaristaNotFoundException e) {
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

    /**
     * Delete barista with specified id using the service.
     *
     * @param id id of the barista to be deleted
     */
    private void delete(Long id) {
        baristaService.delete(id);
    }

}

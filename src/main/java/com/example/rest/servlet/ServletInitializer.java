package com.example.rest.servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import com.example.rest.db.ConfigLoader;
import com.example.rest.db.ConnectionManager;
import com.example.rest.db.ConnectionManagerImp;
import com.example.rest.repository.BaristaRepository;
import com.example.rest.repository.CoffeeRepository;
import com.example.rest.repository.OrderRepository;
import com.example.rest.repository.exception.DataBaseException;
import com.example.rest.repository.imp.BaristaRepositoryImp;
import com.example.rest.repository.imp.CoffeeRepositoryImp;
import com.example.rest.repository.imp.OrderRepositoryImp;
import com.example.rest.service.imp.BaristaService;
import com.example.rest.service.imp.CoffeeService;
import com.example.rest.service.imp.OrderService;

import java.util.logging.Logger;

@WebListener
public class ServletInitializer implements ServletContextListener {
    private static final Logger LOGGER = Logger.getLogger(ServletInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ConfigLoader configLoader = new ConfigLoader("/db.properties");
        ConnectionManager connectionManager = new ConnectionManagerImp(configLoader);
        try {
            BaristaRepository baristaRepository = new BaristaRepositoryImp(connectionManager);
            CoffeeRepository coffeeRepository = new CoffeeRepositoryImp(connectionManager);
            OrderRepository orderRepository = new OrderRepositoryImp(connectionManager);

            BaristaService baristaService = new BaristaService(baristaRepository, orderRepository);
            CoffeeService coffeeService = new CoffeeService(orderRepository, coffeeRepository);
            OrderService orderService = new OrderService(baristaRepository, coffeeRepository, orderRepository);

            BaristaServlet baristaServlet = new BaristaServlet(baristaService);
            CoffeeServlet coffeeServlet = new CoffeeServlet(coffeeService);
            OrderServlet orderServlet = new OrderServlet(orderService);

            sce.getServletContext().addServlet("BaristaServlet", baristaServlet).addMapping("/barista/*");
            sce.getServletContext().addServlet("CoffeeServlet", coffeeServlet).addMapping("/coffee/*");
            sce.getServletContext().addServlet("OrderServlet", orderServlet).addMapping("/orders/*");

        } catch (DataBaseException e) {
            LOGGER.severe(e.getMessage());
        }


    }

}

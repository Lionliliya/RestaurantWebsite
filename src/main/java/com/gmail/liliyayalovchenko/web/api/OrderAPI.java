package com.gmail.liliyayalovchenko.web.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.liliyayalovchenko.domain.Order;
import com.gmail.liliyayalovchenko.jsonViews.Views;
import com.gmail.liliyayalovchenko.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class OrderAPI {

    @Autowired
    private OrderService orderService;
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderAPI.class);

    @Autowired
    ObjectMapper objectMapper;

    /**
     * Method returns a list of all the orders
     * with only employee firstName and secondName
     *
     * @author Liliya Yalovchenko
     **/
    @RequestMapping(value = "/order", method = RequestMethod.GET)
    @JsonView(Views.Private.class)
    public ResponseEntity<List<Order>> allOrders() {
        LOGGER.info("Try to get all orders");
        List<Order> orders;
        try {
            orders = orderService.getAllOrders();
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOGGER.info("All orders are got");
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.Private.class));
        if(orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


    /**
     * Method returns a list of all the orders
     * with only employee firstName and secondName
     *
     * @author Liliya Yalovchenko
     **/
    @RequestMapping(value = "/order/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonView(Views.Private.class)
    public ResponseEntity<Order> orderById(@PathVariable int id) {
        LOGGER.info("Try to get order by id");
        Order order;
        try {
            order = orderService.getOrderById(id);
            LOGGER.info("Order by id is got");
            objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.Private.class));
        } catch (Exception ex) {
            LOGGER.error("Error while getting order by id " + Arrays.toString(ex.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (order == null) {
            System.out.println("Order with id " + id + " has no content");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Method returns a list of all the orders
     * with only employee firstName and secondName
     *
     * @author Liliya Yalovchenko
     **/
    @RequestMapping(value = "/order/status/opened", method = RequestMethod.GET)
    @JsonView(Views.Private.class)
    public ResponseEntity<List<Order>> allOpenOrders() {
        LOGGER.info("Try to get all open orders");
        List<Order> orders;

        try {
            orders = orderService.getAllOpenOrders();
        } catch (Exception ex) {
            LOGGER.error("Error wile getting open orders " + Arrays.toString(ex.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        LOGGER.info("All open orders are got");
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.Private.class));

        if(orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Method returns a list of all the orders
     * with only employee firstName and secondName
     *
     * @author Liliya Yalovchenko
     **/
    @RequestMapping(value = "/order/status/closed", method = RequestMethod.GET)
    @JsonView(Views.Private.class)
    public ResponseEntity<List<Order>> allClosedOrders() {
        LOGGER.info("Try to get all closed orders");
        List<Order> orders;

        try {
            orders = orderService.getAllClosedOrders();
            LOGGER.info("All closed orders are got");
            objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.Private.class));
        } catch (Exception ex) {
            LOGGER.error("Error while getting closed orders " + Arrays.toString(ex.getStackTrace()));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(orders.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

}

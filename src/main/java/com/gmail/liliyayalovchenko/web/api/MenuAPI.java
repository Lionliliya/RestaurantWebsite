package com.gmail.liliyayalovchenko.web.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.liliyayalovchenko.domain.Menu;
import com.gmail.liliyayalovchenko.jsonViews.Views;
import com.gmail.liliyayalovchenko.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class MenuAPI {

    @Autowired
    private MenuService menuService;
    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuAPI.class);

    /**
     * Method returns a list of all the menu
     * (the menu names only, without the dishes in them)
     *
     * @author Liliya Yalovchenko
     * **/
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Menu>> menu() {
        LOGGER.info("Try to get all menu names");
        List<Menu> menuNames;
        try {
            menuNames = menuService.getAllMenus();
        } catch (Exception ex) {
            LOGGER.error("Error while getting all menu " + Arrays.toString(ex.getStackTrace()));
            menuNames = null;
        }
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.Public.class));
        LOGGER.info("All menu names are got");
        if(menuNames == null || menuNames.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(menuNames, HttpStatus.OK);
    }

    /**
     * Method that returns a menu by id with
     * a list of dishes in it
     *
     * @author Liliya Yalovchenko
     * **/
    @RequestMapping(value = "/menu/{id}", method = RequestMethod.GET)
    @JsonView(Views.Internal.class)
    public ResponseEntity<Menu> menu(@PathVariable int id) {
        LOGGER.info("Try to get menu by id with dishes in it");
        Menu menu;
        try {
            menu = menuService.getMenuById(id);
            LOGGER.info("Menu by id is got");
            objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.Internal.class));
            objectMapper.setVisibility(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.NONE));
        } catch (Exception ex) {
            LOGGER.error("Error while getting menu by id " + Arrays.toString(ex.getStackTrace()));
            menu = null;
        }

        if(menu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(menu, HttpStatus.OK);
    }


    /**
     * Method that returns a menu by name
     * full menu instance(with dish list, name and id)
     *
     * @author Liliya Yalovchenko
     * **/
    @RequestMapping(value = "/menu/name/{name}", method = RequestMethod.GET)
    @JsonView(Views.Internal.class)
    public ResponseEntity<Menu> menu(@PathVariable String name) {
        LOGGER.info("Try to get menu by name " + name + " with dishes in it");
        Menu menu;

        try {
            menu = menuService.getMenuByName(name);
        } catch (Exception ex) {
            LOGGER.error("Error while getting menu by name " + Arrays.toString(ex.getStackTrace()));
            menu = null;
        }

        LOGGER.info("Menu by name " + name + " is got");
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.Internal.class));
        objectMapper.setVisibility(objectMapper.getVisibilityChecker().withFieldVisibility(JsonAutoDetect.Visibility.NONE));

        if(menu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(menu, HttpStatus.OK);
    }

}

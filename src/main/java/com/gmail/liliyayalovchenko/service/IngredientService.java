package com.gmail.liliyayalovchenko.service;

import com.gmail.liliyayalovchenko.dao.IngredientDAO;
import com.gmail.liliyayalovchenko.domain.Ingredient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class IngredientService {

    @Autowired
    private IngredientDAO ingredientDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(IngredientService.class);

    @Transactional
    public List<Ingredient> getAllIngredients() {
        LOGGER.info("Try to get all ingredients");
        List<Ingredient> ingredients = ingredientDAO.getAllIngredients();
        LOGGER.info("All ingredients are got.");
        return ingredients;
    }

    @Transactional
    public Ingredient getIngredient(String name) {
        LOGGER.info("Try to get ingredient by name");
        Ingredient ingredient = ingredientDAO.getIngredientByName(name);
        LOGGER.info("Ingredient is got.");
        return ingredient;
    }

    @Transactional
    public boolean ifExist(String ingredient) {
        return ingredientDAO.exist(ingredient);
    }

    @Transactional
    public void save(Ingredient ingredient) {
        ingredientDAO.addIngredient(ingredient);
    }
}

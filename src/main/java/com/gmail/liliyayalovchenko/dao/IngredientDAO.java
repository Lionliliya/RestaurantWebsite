package com.gmail.liliyayalovchenko.dao;


import com.gmail.liliyayalovchenko.domain.Ingredient;

import java.util.List;

public interface IngredientDAO {

    Ingredient getIngredientById(int id);

    List<Ingredient> getAllIngredients();

    Ingredient getIngredientByName(String name);

    boolean exist(String ingredientName);

    void addIngredient(Ingredient ingredient);
}

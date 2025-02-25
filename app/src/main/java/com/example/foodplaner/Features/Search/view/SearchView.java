package com.example.foodplaner.Features.Search.view;

import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.Ingredient;
import com.example.foodplaner.model.MealCountry;

import java.util.List;

public interface SearchView {
    void getCategories(List<Category> categoryList);
    void getCountries(List<MealCountry> countryModel);
    void getIngredients(List<Ingredient> countryModel);
    void showError(String errorMsg);

}

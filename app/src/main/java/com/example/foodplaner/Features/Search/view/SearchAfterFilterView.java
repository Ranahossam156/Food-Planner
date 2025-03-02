package com.example.foodplaner.Features.Search.view;

import com.example.foodplaner.model.FilteredMeal;
import com.example.foodplaner.model.MealElement;

import java.util.List;

public interface SearchAfterFilterView {
    void getSpecificMealsByCategories(List<FilteredMeal> meal);
    void getSpecificMealsyCountries(List<FilteredMeal> meal);
    void getSpecificMealsyIngredients(List<FilteredMeal> meal);
    void getMealDetailsByID(List<MealElement> meal);

    void showError(String errorMsg);

    void showFavoriteAddedSuccess();
}

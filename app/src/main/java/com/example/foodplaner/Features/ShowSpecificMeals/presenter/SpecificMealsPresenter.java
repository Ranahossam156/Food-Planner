package com.example.foodplaner.Features.ShowSpecificMeals.presenter;

public interface SpecificMealsPresenter {
    void getSpecificMealsByCategories(String categoryName);
    void getSpecificMealsByCountries(String countryName);
    void getSpecificMealsByIngredients(String ingredientName);
    void getMealDetailsById(String id);
    void onFavoriteClick(String id);

}

package com.example.foodplaner.Features.Search.presenter;

public interface SearchAfterFilterPresenter {
    void getSpecificMealsByCategories(String categoryName);
    void getSpecificMealsByCountries(String countryName);
    void getSpecificMealsByIngredients(String ingredientName);
    void getMealDetailsById(String id);
    void onFavoriteClick(String id);

}

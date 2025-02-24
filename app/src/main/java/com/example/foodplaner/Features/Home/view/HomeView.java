package com.example.foodplaner.Features.Home.view;

import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealCountry;
import com.example.foodplaner.model.MealElement;

import java.util.List;

public interface HomeView {
    void onGetMealOfTheDay(List<MealElement> meal);
    void getCategories(List<Category> categoryList);
    void getCountries(List<MealCountry> countryModel);
    void showError(String errorMsg);
}

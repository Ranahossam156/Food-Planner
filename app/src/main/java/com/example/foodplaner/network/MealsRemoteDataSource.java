package com.example.foodplaner.network;

import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.FilteredMeals;
import com.example.foodplaner.model.Meal;

import io.reactivex.rxjava3.core.Single;

public interface MealsRemoteDataSource {
    Single<Categories> getCategories();
//    void searchMeal(String name, NetworkCallBack networkCallback);
    Single<Meal> mealOfTheDay();
    Single<CountryModel> getMealCountries();
    Single<FilteredMeals>getMealFilteredByCategory(String categoryName);
    Single<FilteredMeals>getMealFilteredByCountry(String countryName);
    Single<Meal> getMealDetailsById(String id);
}

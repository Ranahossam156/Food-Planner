package com.example.foodplaner.model;

import io.reactivex.rxjava3.core.Single;

public interface MealRepository {
    Single<Meal> getMealOfTheDay();
    Single<Categories> getCategories();
    Single<CountryModel> getMealsCountries();
    Single<FilteredMeals> getMealsFilteredByCategories(String categoryName);
    Single<FilteredMeals> getMealsFilteredByCountry(String countryName);
    Single<Meal> getMealDetailsById(String id);

}

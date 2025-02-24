package com.example.foodplaner.model;

import com.example.foodplaner.network.NetworkCallBack;

import io.reactivex.rxjava3.core.Single;

public interface MealRepository {
    Single<Meal> getMealOfTheDay();
    Single<Categories> getCategories();
    Single<CountryModel> getMealsCountries();
}

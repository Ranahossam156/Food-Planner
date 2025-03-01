package com.example.foodplaner.Database;

import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealsLocalDataSource {
    public Single<List<MealElement>> getAllFavouriteMeals() ;
    public Completable insertMealToFavorite(MealElement mealStorage);

    public Completable deleteMealFromFavorite(MealElement mealStorage);

    public Completable removeAllData();

    Single<List<PlannedMeal>> getAllPlannedMeals();
    Completable insertPlannedMeal(PlannedMeal plannedMeal);
    Completable deletePlannedMeal(PlannedMeal plannedMeal);
    Completable removeAllPlannedMeals();


    Completable insertAllFavorites(List<MealElement> meals);
    Completable insertAllPlannedMeals(List<PlannedMeal> meals);
}

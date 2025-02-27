package com.example.foodplaner.Database;

import com.example.foodplaner.model.MealElement;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MealsLocalDataSource {
    public Single<List<MealElement>> getAllFavouriteMeals() ;
    public Single<List<MealElement>> getAllPlannedMeals();
    public Completable insertMealToFavorite(MealElement mealStorage);

    public Completable deleteMealFromFavorite(MealElement mealStorage);

    public Completable removeAllData();
}

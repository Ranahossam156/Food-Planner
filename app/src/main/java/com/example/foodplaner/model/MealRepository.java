package com.example.foodplaner.model;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface MealRepository {
    Single<Meal> getMealOfTheDay();
    Single<Categories> getCategories();
    Single<CountryModel> getMealsCountries();
    Single<Ingredients> getMealsIngredients();
    Single<FilteredMeals> getMealsFilteredByCategories(String categoryName);
    Single<FilteredMeals> getMealsFilteredByCountry(String countryName);
    Single<FilteredMeals> getMealsFilteredByIngredients(String ingredientName);
    Single<Meal> getMealDetailsById(String id);
    public Completable insertMealToFavorite(MealElement mealElement);

    public Single<List<MealElement>> getAllFavouriteMeals();

    public Completable removeMealFromFavorite(MealElement mealElement);
    public Completable removeAllFavoriteMeals();
    Single<List<PlannedMeal>> getAllPlannedMeals();
    Completable insertPlannedMeal(PlannedMeal plannedMeal);
    Completable removePlannedMeal(PlannedMeal plannedMeal);
    Completable removeAllPlannedMeals();


    Completable insertAllFavorites(List<MealElement> favorites);
    Completable insertAllPlannedMeals(List<PlannedMeal> plannedMeals);
    public Completable backupDataToFirestore(String userId, FirebaseFirestore firestore);
    public Completable restoreDataFromFirestore(String userId, FirebaseFirestore firestore) ;



    }

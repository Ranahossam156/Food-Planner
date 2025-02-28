package com.example.foodplaner.model;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImplementation implements MealRepository {
    MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion;
    MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation;
    public static MealRepositoryImplementation repo=null;

    public MealRepositoryImplementation(MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation,MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion) {
        this.mealsLocalDataSourceImplementation=mealsLocalDataSourceImplementation;
        this.mealsRemoteDataSourceImplementaion = mealsRemoteDataSourceImplementaion;
    }

    public static MealRepositoryImplementation getInstance(MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation,MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion){
        if (repo==null)
        {
            repo =new MealRepositoryImplementation(mealsLocalDataSourceImplementation,mealsRemoteDataSourceImplementaion);
        }
        return repo;
    }

    @Override
    public Single<Meal> getMealOfTheDay() {
        return mealsRemoteDataSourceImplementaion.mealOfTheDay();
    }

    @Override
    public Single<Categories> getCategories() {
        return mealsRemoteDataSourceImplementaion.getCategories();
    }

    @Override
    public Single<CountryModel> getMealsCountries() {
        return mealsRemoteDataSourceImplementaion.getMealCountries();
    }

    @Override
    public Single<Ingredients> getMealsIngredients() {
        return mealsRemoteDataSourceImplementaion.getIngredients();
    }

    @Override
    public Single<FilteredMeals> getMealsFilteredByCategories(String categoryName) {
        return mealsRemoteDataSourceImplementaion.getMealFilteredByCategory(categoryName);
    }
    @Override
    public Single<FilteredMeals> getMealsFilteredByCountry(String countryName) {
        return mealsRemoteDataSourceImplementaion.getMealFilteredByCountry(countryName);
    }

    @Override
    public Single<FilteredMeals> getMealsFilteredByIngredients(String ingredientName) {
        return mealsRemoteDataSourceImplementaion.getMealFilteredByIngredient(ingredientName);
    }


    @Override
    public Single<Meal> getMealDetailsById(String id) {
        return mealsRemoteDataSourceImplementaion.getMealDetailsById(id);
    }

    @Override
    public Completable insertMealToFavorite(MealElement mealElement) {
        return mealsLocalDataSourceImplementation.insertMealToFavorite(mealElement);
    }

    @Override
    public Single<List<MealElement>> getAllFavouriteMeals() {
        return mealsLocalDataSourceImplementation.getAllFavouriteMeals();
    }


    @Override
    public Completable removeMealFromFavorite(MealElement mealElement) {
        return mealsLocalDataSourceImplementation.deleteMealFromFavorite(mealElement);
    }

    @Override
    public Completable removeAllFavoriteMeals() {
        return mealsLocalDataSourceImplementation.removeAllData();
    }
    @Override
    public Single<List<PlannedMeal>> getAllPlannedMeals() {
        return mealsLocalDataSourceImplementation.getAllPlannedMeals();
    }

    @Override
    public Completable insertPlannedMeal(PlannedMeal plannedMeal) {
        return mealsLocalDataSourceImplementation.insertPlannedMeal(plannedMeal);
    }

    @Override
    public Completable removePlannedMeal(PlannedMeal plannedMeal) {
        return mealsLocalDataSourceImplementation.deletePlannedMeal(plannedMeal);
    }

    @Override
    public Completable removeAllPlannedMeals() {
        return mealsLocalDataSourceImplementation.removeAllPlannedMeals();
    }
}

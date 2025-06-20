package com.example.foodplaner.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsLocalDataSourceImplementation implements MealsLocalDataSource {
    private static MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation;
    private MealDAO mealDAO;
    private PlanDAO planDAO;
    Context context;
    private MealsDataBase mealsDataBase;
    private Single<List<MealElement>> storedFavorites;
    MealsLocalDataSourceImplementation(Context context){
        this.context=context;
        mealsDataBase=MealsDataBase.getDataBaseInstance(context.getApplicationContext());
        mealDAO=mealsDataBase.getMealDao();
        storedFavorites=mealDAO.getFavoriteMeals();
        planDAO=mealsDataBase.getPlannedDAO();
    }
    public static MealsLocalDataSourceImplementation getInstance(Context context) {
        if (mealsLocalDataSourceImplementation == null) {
            mealsLocalDataSourceImplementation = new MealsLocalDataSourceImplementation(context);
        }
        return mealsLocalDataSourceImplementation;
    }
    @Override
    public Single<List<MealElement>> getAllFavouriteMeals() {
        return mealDAO.getFavoriteMeals();
    }


    @Override
    public Completable insertMealToFavorite(MealElement meal) {
        return mealDAO.insertMealToFavorites(meal)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deleteMealFromFavorite(MealElement meal) {
        return mealDAO.deleteMealFromFavorites(meal).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeAllData() {
        return mealDAO.removeAllFavoriteMeals().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    @Override
    public Single<List<PlannedMeal>> getAllPlannedMeals() {
        return planDAO.getAllPlannedMeals()
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insertPlannedMeal(PlannedMeal plannedMeal) {
        return planDAO.insertPlannedMeal(plannedMeal)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deletePlannedMeal(PlannedMeal plannedMeal) {
        return planDAO.deletePlannedMeal(plannedMeal)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable removeAllPlannedMeals() {
        return planDAO.deleteAllRecords()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());    }


    @Override
    public Completable insertAllFavorites(List<MealElement> meals) {
        return mealDAO.insertAllFavorites(meals).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insertAllPlannedMeals(List<PlannedMeal> meals) {
        return planDAO.insertAllPlannedMeals(meals).subscribeOn(Schedulers.io());
    }
    @Override
    public boolean isUserGuest() {
        SharedPreferences sharedPrefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean("isGuest", false);
    }

}

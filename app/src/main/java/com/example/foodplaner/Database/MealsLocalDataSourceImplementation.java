package com.example.foodplaner.Database;

import android.content.Context;

import com.example.foodplaner.model.MealElement;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealsLocalDataSourceImplementation implements MealsLocalDataSource {
    private static MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation;
    private MealDAO mealDAO;
    Context context;
    private MealsDataBase mealsDataBase;
    private Single<List<MealElement>> storedFavorites;
    MealsLocalDataSourceImplementation(Context context){
        this.context=context;
        mealsDataBase=MealsDataBase.getDataBaseInstance(context.getApplicationContext());
        mealDAO=mealsDataBase.getMealDao();
        storedFavorites=mealDAO.getFavoriteMeals();
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
    public Single<List<MealElement>> getAllPlannedMeals() {
        return null;
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
        return mealDAO.removeAllMeals();
    }
}

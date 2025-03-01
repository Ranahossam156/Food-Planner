package com.example.foodplaner.Features.Authentication.presenter;

import android.content.Context;

import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthPresenter {
//     Completable backupFavorites();
   //  Completable  backupPlannedMeals();
//    public Completable logoutAndBackupAndClear(Context context);
//     public Completable restorePlannedMeals();
//    public Completable restoreFavorites();
//    public Completable restoreData();
    public Completable uploadFavorites(List<MealElement> favorites);
    public Single<List<MealElement>> downloadFavorites();
    public Single<List<PlannedMeal>> downloadPlannedMeals();
    public Completable uploadPlannedMeals(List<PlannedMeal> plannedMeals);
}

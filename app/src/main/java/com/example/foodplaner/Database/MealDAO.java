package com.example.foodplaner.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplaner.model.MealElement;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
@Dao
public interface MealDAO {
    @Query("SELECT * FROM favorite_meals_table")
    Single<List<MealElement>> getFavoriteMeals();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insertMealToFavorites(MealElement meal);

    @Delete
    Completable deleteMealFromFavorites(MealElement meal);

    @Query("DELETE FROM favorite_meals_table")
    Completable removeAllMeals();
}

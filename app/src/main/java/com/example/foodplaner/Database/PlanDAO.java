package com.example.foodplaner.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.foodplaner.model.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface PlanDAO {
    @Query("SELECT * FROM planned_meals_table")
    Single<List<PlannedMeal>> getAllPlannedMeals();
    @Query("SELECT * FROM planned_meals_table where dayOfWeek = :day")
    Single<List<PlannedMeal>> getMealsByDay(String day);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlannedMeal(PlannedMeal meal);

    @Delete
    Completable deletePlannedMeal(PlannedMeal meal);

    @Query("DELETE FROM planned_meals_table")
    Completable deleteAllRecords();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAllPlannedMeals(List<PlannedMeal> meals);
}

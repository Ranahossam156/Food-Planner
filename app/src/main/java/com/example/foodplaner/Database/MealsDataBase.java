package com.example.foodplaner.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;


@Database(entities = {MealElement.class, PlannedMeal.class},version =1 )
public abstract class MealsDataBase extends RoomDatabase {
    public abstract MealDAO getMealDao();
    public abstract PlanDAO getPlannedDAO();
    public static MealsDataBase getDataBaseInstance(Context ctx) {
        return Room.databaseBuilder(ctx, MealsDataBase.class, "mealsdb").build();
    }
}

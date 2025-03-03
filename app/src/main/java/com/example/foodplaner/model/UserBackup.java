package com.example.foodplaner.model;

import java.util.List;

public class UserBackup {
    private List<MealElement> favorites;
    private List<PlannedMeal> plannedMeals;

    public UserBackup() {}

    public UserBackup(List<MealElement> favorites, List<PlannedMeal> planned) {
        this.favorites=favorites;
        this.plannedMeals=planned;
    }

    public List<MealElement> getFavorites() { return favorites; }
    public void setFavorites(List<MealElement> favorites) { this.favorites = favorites; }

    public List<PlannedMeal> getPlannedMeals() { return plannedMeals; }
    public void setPlannedMeals(List<PlannedMeal> plannedMeals) { this.plannedMeals = plannedMeals; }
}


package com.example.foodplaner.Features.Meal_Details.presenter;

import com.example.foodplaner.model.PlannedMeal;

public interface MealDetailsPresenter {
    void OnFavoriteClicked(String id);
    void OnPlanClicked(PlannedMeal plannedMeal);
}

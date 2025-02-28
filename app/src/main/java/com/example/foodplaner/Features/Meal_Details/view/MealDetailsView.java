package com.example.foodplaner.Features.Meal_Details.view;

public interface MealDetailsView {
    void onFavoriteClick();
    void onPlanClick();
    void showError(String errorMsg);
}

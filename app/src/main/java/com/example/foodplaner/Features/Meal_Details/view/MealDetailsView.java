package com.example.foodplaner.Features.Meal_Details.view;

public interface MealDetailsView {
    void onFavoriteClick(String id);
    void onPlanClick();
    void showError(String errorMsg);
    void showFavoriteAddedSuccess();
}

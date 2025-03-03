package com.example.foodplaner.Features.PlanMeals.presenter;

import com.example.foodplaner.model.PlannedMeal;

public interface PlanMealsPresenter {
    void getPlannedMeals();
    void onPlannedeRemoved(PlannedMeal mealElement);
    void setGuest(boolean isGuest);
    Boolean isGuest();}

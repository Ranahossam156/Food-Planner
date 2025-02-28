package com.example.foodplaner.Features.Plan_Meals.presenter;

import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;

public interface PlanMealsPresenter {
    void getPlannedMeals();
    void onPlannedeRemoved(PlannedMeal mealElement);

}

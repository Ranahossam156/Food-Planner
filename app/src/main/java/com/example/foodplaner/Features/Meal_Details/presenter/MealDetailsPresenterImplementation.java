package com.example.foodplaner.Features.Meal_Details.presenter;

import com.example.foodplaner.Features.Meal_Details.view.MealDetailsView;
import com.example.foodplaner.model.MealRepository;
import com.example.foodplaner.model.PlannedMeal;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class MealDetailsPresenterImplementation implements MealDetailsPresenter{
    MealRepository mealRepository;
    MealDetailsView mealDetailsView;

    public MealDetailsPresenterImplementation(MealDetailsView mealDetailsView,MealRepository mealRepository) {
        this.mealRepository = mealRepository;
        this.mealDetailsView = mealDetailsView;
    }

    @Override
    public void OnFavoriteClicked(String id) {

    }

    @Override
    public void OnPlanClicked(PlannedMeal plannedMeal) {
        mealRepository.insertPlannedMeal(plannedMeal)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    mealDetailsView.onPlanClick();
                }, throwable -> {
                    mealDetailsView.showError("Failed to add to favorites: " + throwable.getMessage());
                });
    }
}

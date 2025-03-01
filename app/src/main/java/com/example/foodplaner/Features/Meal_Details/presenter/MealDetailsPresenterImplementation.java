package com.example.foodplaner.Features.Meal_Details.presenter;

import com.example.foodplaner.Features.Meal_Details.view.MealDetailsView;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepository;
import com.example.foodplaner.model.PlannedMeal;

import java.util.List;

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
        mealRepository.getMealDetailsById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meal -> {
                    List<MealElement> mealElements = meal.getMeals();
                    if (mealElements != null && !mealElements.isEmpty()) {
                        MealElement mealElement = mealElements.get(0);
                        mealRepository.insertMealToFavorite(mealElement)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    mealDetailsView.showFavoriteAddedSuccess();
                                }, throwable -> {
                                    mealDetailsView.showError("Failed to add to favorites: " + throwable.getMessage());
                                });
                    } else {
                        mealDetailsView.showError("Meal details not found.");
                    }
                }, throwable -> {
                    mealDetailsView.showError("Error fetching meal details: " + throwable.getMessage());
                });
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

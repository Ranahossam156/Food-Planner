package com.example.foodplaner.Features.ShowSpecificMeals.presenter;

import com.example.foodplaner.Features.ShowSpecificMeals.view.SpecificMealsView;
import com.example.foodplaner.model.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SpecificMealsPresenterImplementation implements SpecificMealsPresenter {
    SpecificMealsView specificMealsView;
    MealRepository mealRepository;

    public SpecificMealsPresenterImplementation(SpecificMealsView specificMealsView, MealRepository mealRepository) {
        this.specificMealsView = specificMealsView;
        this.mealRepository = mealRepository;
    }

    @Override
    public void getSpecificMealsByCategories(String categoryName) {
        mealRepository.getMealsFilteredByCategories(categoryName).map(
                        meal->meal.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal->{
                            specificMealsView.getSpecificMealsByCategories(meal);
                        },
                        error->{
                            specificMealsView.showError(error.getMessage());
                        }
                );

    }

    @Override
    public void getSpecificMealsByCountries(String countryName) {
        mealRepository.getMealsFilteredByCountry(countryName).map(
                        meal->meal.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal->{
                            specificMealsView.getSpecificMealsyCountries(meal);
                        },
                        error->{
                            specificMealsView.showError(error.getMessage());
                        }
                );
    }

    @Override
    public void getMealDetailsById(String id) {
        mealRepository.getMealDetailsById(id).map(
                        meal->meal.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal->{
                            specificMealsView.getMealDetailsByID(meal);
                        },
                        error->{
                            specificMealsView.showError(error.getMessage());
                        }
                );
    }

}

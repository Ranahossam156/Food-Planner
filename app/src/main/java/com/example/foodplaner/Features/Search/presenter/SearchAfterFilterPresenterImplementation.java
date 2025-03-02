package com.example.foodplaner.Features.Search.presenter;

import com.example.foodplaner.Features.Search.view.SearchAfterFilterView;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepository;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchAfterFilterPresenterImplementation implements SearchAfterFilterPresenter {
    SearchAfterFilterView searchAfterFilterView;
    MealRepository mealRepository;

    public SearchAfterFilterPresenterImplementation(SearchAfterFilterView searchAfterFilterView, MealRepository mealRepository) {
        this.searchAfterFilterView = searchAfterFilterView;
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
                            searchAfterFilterView.getSpecificMealsByCategories(meal);
                        },
                        error->{
                            searchAfterFilterView.showError(error.getMessage());
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
                            searchAfterFilterView.getSpecificMealsyCountries(meal);
                        },
                        error->{
                            searchAfterFilterView.showError(error.getMessage());
                        }
                );
    }

    @Override
    public void getSpecificMealsByIngredients(String ingredientName) {
        mealRepository.getMealsFilteredByIngredients(ingredientName).map(
                        meal->meal.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meal->{
                            searchAfterFilterView.getSpecificMealsyIngredients(meal);
                        },
                        error->{
                            searchAfterFilterView.showError(error.getMessage());
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
                            searchAfterFilterView.getMealDetailsByID(meal);
                        },
                        error->{
                            searchAfterFilterView.showError(error.getMessage());
                        }
                );
    }

    @Override
    public void onFavoriteClick(String id) {
        mealRepository.getMealDetailsById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(meal -> {
                    List<MealElement> mealElements = meal.getMeals();
                    if (mealElements != null && !mealElements.isEmpty()) {
                        MealElement mealElement = mealElements.get(0);
                        mealRepository.insertMealToFavorite(mealElement)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    searchAfterFilterView.showFavoriteAddedSuccess();
                                }, throwable -> {
                                    searchAfterFilterView.showError("Failed to add to favorites: " + throwable.getMessage());
                                });
                    } else {
                        searchAfterFilterView.showError("Meal details not found.");
                    }
                }, throwable -> {
                    searchAfterFilterView.showError("Error fetching meal details: " + throwable.getMessage());
                });
    }


}

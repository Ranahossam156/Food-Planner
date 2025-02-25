package com.example.foodplaner.Features.Search.presenter;

import com.example.foodplaner.Features.Home.view.HomeView;
import com.example.foodplaner.Features.Search.view.SearchView;
import com.example.foodplaner.model.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchPresenterImplementation implements SearchPresenter{
    SearchView searchView;
    MealRepository mealRepository;

    public SearchPresenterImplementation(SearchView searchView, MealRepository mealRepository) {
        this.searchView = searchView;
        this.mealRepository = mealRepository;
    }

    @Override
    public void getCategories() {
        mealRepository.getCategories()
                .map(categories->categories.getCategories())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories->{
                            searchView.getCategories(categories);
                        },
                        error->{
                            searchView.showError(error.getMessage());
                        }
                );
    }

    @Override
    public void getCountries() {
        mealRepository.getMealsCountries()
                .map(countries->countries.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countries->{
                            searchView.getCountries(countries);
                        },
                        error->{
                            searchView.showError(error.getMessage());
                        }
                );
        //mealRepository.getMealsCountries();
    }

    @Override
    public void getIngredients() {
        mealRepository.getMealsIngredients()
                .map(ingredients->ingredients.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        ingredients->{
                            searchView.getIngredients(ingredients);
                        },
                        error->{
                            searchView.showError(error.getMessage());
                        }
                );
    }
}

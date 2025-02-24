package com.example.foodplaner.Features.Home.presenter;

import com.example.foodplaner.Features.Home.view.HomeView;
import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepository;
import com.example.foodplaner.network.NetworkCallBack;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HomePresenterImplementation implements HomePresenter {
    HomeView homeView;
    MealRepository mealRepository;

    public HomePresenterImplementation(HomeView homeView, MealRepository mealRepository) {
        this.homeView = homeView;
        this.mealRepository = mealRepository;
    }


    @Override
    public void getRandomMeal() {
        mealRepository.getMealOfTheDay().map(
                meal->meal.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                meal->{
                    homeView.onGetMealOfTheDay(meal);
                },
                error->{
                    homeView.showError(error.getMessage());
                }
        );

    }

    @Override
    public void getCategories() {
        mealRepository.getCategories()
                .map(categories->categories.getCategories())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories->{
                            homeView.getCategories(categories);
                        },
                        error->{
                            homeView.showError(error.getMessage());
                        }
                );
        //mealRepository.getCategories();
    }

    @Override
    public void getCountries() {
        mealRepository.getMealsCountries()
                .map(countries->countries.getMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        countries->{
                            homeView.getCountries(countries);
                        },
                        error->{
                            homeView.showError(error.getMessage());
                        }
                );
        //mealRepository.getMealsCountries();
    }

//    @Override
//    public void onSuccessResult(List<MealElement> meals) {
//        if (meals != null && !meals.isEmpty()) {
//            Meal meal = new Meal();
//            meal.setMeals(meals);
//            homeView.onGetMealOfTheDay(meal);
//        }
//    }

//    @Override
//    public void onSuccessResult(Object response) {
//        if (response instanceof Categories) {
//            homeView.getCategories((Categories) response);
//        } else if (response instanceof Meal) {
//            // Pass the Meal object to the view
//            homeView.onGetMealOfTheDay((Meal) response);
//        } else if (response instanceof CountryModel) {
//            homeView.getCountries((CountryModel) response);
//        }
//    }
//
//    @Override
//    public void onFailureResult(String errorMsg) {
//
//    }
}

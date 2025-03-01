package com.example.foodplaner.Features.Home.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodplaner.Features.Home.view.HomeView;
import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepository;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Context context = ((androidx.fragment.app.Fragment) homeView).getContext();
        SharedPreferences prefs = context.getSharedPreferences("RandomMealPrefs", Context.MODE_PRIVATE);

        String storedDate = prefs.getString("randomMealDate", "");
        if (today.equals(storedDate)) {
            String mealJson = prefs.getString("randomMeal", null);
            if (mealJson != null) {
                MealElement cachedMeal = new Gson().fromJson(mealJson, MealElement.class);
                homeView.onGetMealOfTheDay(Collections.singletonList(cachedMeal));
                return;
            }
        }

        mealRepository.getMealOfTheDay()
                .map(mealResponse -> {
                    List<MealElement> meals = mealResponse.getMeals();
                    if (meals != null && !meals.isEmpty()) {
                        return meals.get(0);
                    } else {
                        throw new Exception("No meal available");
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        randomMeal -> {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("randomMealDate", today);
                            editor.putString("randomMeal", new Gson().toJson(randomMeal));
                            editor.apply();
                            homeView.onGetMealOfTheDay(Collections.singletonList(randomMeal));
                        },
                        error -> homeView.showError(error.getMessage())
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
    }
}

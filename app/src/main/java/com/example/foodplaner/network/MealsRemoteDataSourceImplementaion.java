package com.example.foodplaner.network;

import android.util.Log;

import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.FilteredMeals;
import com.example.foodplaner.model.Ingredients;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealElement;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealsRemoteDataSourceImplementaion implements MealsRemoteDataSource {
    private static final String BASE_URL="https://www.themealdb.com/api/json/v1/1/";
    public static final String TAG = "Remote_Data_Source";

    private MealsServices mealsServices;
    private static MealsRemoteDataSourceImplementaion instance=null;
    private MealsRemoteDataSourceImplementaion() {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJava3CallAdapterFactory.create()).baseUrl(BASE_URL).build();
        mealsServices=retrofit.create(MealsServices.class);
    }
    public static MealsRemoteDataSourceImplementaion getInstance()
    {
        if (instance == null) {
            instance = new MealsRemoteDataSourceImplementaion();
        }
        return instance;
    }

    @Override
    public Single<Categories> getCategories() {
        return mealsServices.getCategories();
    }

    @Override
    public Single<Ingredients> getIngredients() {
        return mealsServices.getIngredients();
    }

    @Override
    public Single<FilteredMeals> getMealFilteredByIngredient(String ingredientName) {
        return mealsServices.getMealFilteredByIngredient(ingredientName);
    }

    @Override
    public Single<Meal> mealOfTheDay() {
        return mealsServices.getRandomMeal();
    }

    @Override
    public Single<CountryModel> getMealCountries() {
        return mealsServices.getCountries();
    }

    @Override
    public Single<FilteredMeals> getMealFilteredByCategory(String categoryName) {
        return mealsServices.getMealFilteredByCategory(categoryName);
    }

    @Override
    public Single<FilteredMeals> getMealFilteredByCountry(String countryName) {
        return mealsServices.getMealFilteredByCountry(countryName);
    }

    @Override
    public Single<Meal> getMealDetailsById(String id) {
        return mealsServices.getMealDetailsById(id);
    }
}

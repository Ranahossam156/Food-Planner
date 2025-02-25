package com.example.foodplaner.network;

import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.FilteredMeals;
import com.example.foodplaner.model.Ingredients;
import com.example.foodplaner.model.Meal;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealsServices {
    @GET("random.php")
    Single<Meal> getRandomMeal();
    @GET("categories.php")
    Single<Categories> getCategories();
    @GET("list.php?a=list")
    Single<CountryModel>getCountries();
    @GET("list.php?i=list")
    Single<Ingredients> getIngredients();
    @GET("filter.php")
    Single<FilteredMeals> getMealFilteredByIngredient(@Query("i") String category);
    @GET("filter.php")
    Single<FilteredMeals> getMealFilteredByCategory(@Query("c") String category);
    @GET("filter.php")
    Single<FilteredMeals> getMealFilteredByCountry(@Query("a") String country);
    @GET("lookup.php")
    Single<Meal> getMealDetailsById(@Query("i") String mealId);


}

package com.example.foodplaner.network;

import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.Meal;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MealsServices {
    @GET("random.php")
    Single<Meal> getRandomMeal();
    @GET("categories.php")
    Single<Categories> getCategories();
    @GET("list.php?a=list")
    Single<CountryModel>getCountries();

}

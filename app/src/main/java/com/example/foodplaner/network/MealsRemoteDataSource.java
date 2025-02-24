package com.example.foodplaner.network;

import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.Meal;

import io.reactivex.rxjava3.core.Single;

public interface MealsRemoteDataSource {
    Single<Categories> getCategories();
//    void searchMeal(String name, NetworkCallBack networkCallback);
    Single<Meal> mealOfTheDay();
    Single<CountryModel> getMealCountries();

//    void getIngredients(NetworkCallBack networkCallback);
//    void getMealsByArea(String area, NetworkCallBack networkCallback);
//    void getMealsByCategory(String category, NetworkCallBack networkCallback);
//    void getMealsByIngredient(String ingredient, NetworkCallBack networkCallback);
//
//    void getMealById(String mealId, NetworkCallBack networkCallback);
//
//    // Firebase Firestore
//    void getFavMeals(NetworkCallBack networkCallback);
//    void addMealToFav(Meal meal, ConnectivityManager.NetworkCallback networkCallback);
//    void removeMealFromFav(Meal meal, ConnectivityManager.NetworkCallback networkCallback);
}

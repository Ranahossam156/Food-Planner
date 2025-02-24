package com.example.foodplaner.network;

import android.util.Log;

import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.CountryModel;
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
    public Single<Meal> mealOfTheDay() {
        return mealsServices.getRandomMeal();
    }

    @Override
    public Single<CountryModel> getMealCountries() {
        return mealsServices.getCountries();
    }

//    @Override
//    public void getCategories(NetworkCallBack networkCallback) {
//        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
//        mealsServices=retrofit.create(MealsServices.class);
//        Call<Categories> call=mealsServices.getCategories();
//        call.enqueue(new Callback<Categories>() {
//            @Override
//            public void onResponse(Call<Categories> call, Response<Categories> response) {
//                if(response.isSuccessful()){
//                    networkCallback.onSuccessResult(response.body().getCategories());
//                    Log.i(TAG, "onResponse: "+response.body().getCategories());
//                   Categories categories=response.body();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Categories> call, Throwable throwable) {
//                networkCallback.onFailureResult(throwable.getMessage());
//                Log.i(TAG, "onFailure: "+throwable.getMessage());
//            }
//
//        });
//
//    }
//@Override
//public void getCategories(NetworkCallBack networkCallback) {
//    Call<Categories> call = mealsServices.getCategories();
//    call.enqueue(new Callback<Categories>() {
//        @Override
//        public void onResponse(Call<Categories> call, Response<Categories> response) {
//            if (response.isSuccessful()) {
//                networkCallback.onSuccessResult(response.body());
//                Log.i(TAG, "onResponse: " + response.body().getCategories());
//            }
//        }
//
//        @Override
//        public void onFailure(Call<Categories> call, Throwable throwable) {
//            networkCallback.onFailureResult(throwable.getMessage());
//            Log.i(TAG, "onFailure: " + throwable.getMessage());
//        }
//    });
//}

    //    @Override
//    public void makeNetworkCall(NetworkCallBack networkcallback){
//        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
//        MealsServices mealsServices=retrofit.create(MealsServices.class);
//        Call<Products> call=productsService.getAllProducts();
//        call.enqueue(new Callback<Products>() {
//            @Override
//            public void onResponse(Call<Products> call, Response<Products> response) {
//                if(response.isSuccessful()){
//                    networkcallback.onSuccessResult(response.body().getProducts());
//                    Log.i(TAG, "onResponse: "+response.body().getProducts());
//                    productList=response.body().getProducts();
//                    Log.i("TAG", productList.toString());
//
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Products> call, Throwable throwable) {
//                networkcallback.onFailureResult(throwable.getMessage());
//                Log.i(TAG, "onFailure: "+throwable.getMessage());
//            }
//        });
//    }
//    @Override
//    public void mealOfTheDay(NetworkCallBack networkCallback) {
//        Call<Meal> call = mealsServices.getRandomMeal();
//        call.enqueue(new Callback<Meal>() {
//            @Override
//            public void onResponse(Call<Meal> call, Response<Meal> response) {
//                if (response.isSuccessful()) {
//                    networkCallback.onSuccessResult(response.body());
//                    Log.i(TAG, "onResponse: " + response.body().getMeals());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Meal> call, Throwable throwable) {
//                networkCallback.onFailureResult(throwable.getMessage());
//                Log.i(TAG, "onFailure: " + throwable.getMessage());
//            }
//        });
//    }
//
//    @Override
//    public void getMealCountries(NetworkCallBack networkCallBack) {
//        Call<CountryModel> call = mealsServices.getCountries();
//        call.enqueue(new Callback<CountryModel>() {
//            @Override
//            public void onResponse(Call<CountryModel> call, Response<CountryModel> response) {
//                if (response.isSuccessful()) {
//                    networkCallBack.onSuccessResult(response.body());
//                    Log.i(TAG, "onResponse: " + response.body().getMeals());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<CountryModel> call, Throwable throwable) {
//                networkCallBack.onFailureResult(throwable.getMessage());
//                Log.i(TAG, "onFailure: " + throwable.getMessage());
//            }
//        });
//    }
}

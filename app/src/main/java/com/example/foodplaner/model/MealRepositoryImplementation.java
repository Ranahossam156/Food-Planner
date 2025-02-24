package com.example.foodplaner.model;

import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;
import com.example.foodplaner.network.NetworkCallBack;

import io.reactivex.rxjava3.core.Single;

public class MealRepositoryImplementation implements MealRepository {
    MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion;
    public static MealRepositoryImplementation repo=null;

    public MealRepositoryImplementation(MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion) {
        this.mealsRemoteDataSourceImplementaion = mealsRemoteDataSourceImplementaion;
    }

    public static MealRepositoryImplementation getInstance(MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion){
        if (repo==null)
        {
            repo =new MealRepositoryImplementation(mealsRemoteDataSourceImplementaion);
        }
        return repo;
    }

    @Override
    public Single<Meal> getMealOfTheDay() {
        return mealsRemoteDataSourceImplementaion.mealOfTheDay();
    }

    @Override
    public Single<Categories> getCategories() {
        return mealsRemoteDataSourceImplementaion.getCategories();
    }

    @Override
    public Single<CountryModel> getMealsCountries() {
        return mealsRemoteDataSourceImplementaion.getMealCountries();
    }
}

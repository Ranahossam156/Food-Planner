package com.example.foodplaner.model;

import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;

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

    @Override
    public Single<FilteredMeals> getMealsFilteredByCategories(String categoryName) {
        return mealsRemoteDataSourceImplementaion.getMealFilteredByCategory(categoryName);
    }
    @Override
    public Single<FilteredMeals> getMealsFilteredByCountry(String countryName) {
        return mealsRemoteDataSourceImplementaion.getMealFilteredByCountry(countryName);
    }

    @Override
    public Single<Meal> getMealDetailsById(String id) {
        return mealsRemoteDataSourceImplementaion.getMealDetailsById(id);
    }
}

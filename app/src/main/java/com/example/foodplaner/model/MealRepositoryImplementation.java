package com.example.foodplaner.model;

import androidx.annotation.MainThread;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Database.SharedPrefrencesDataSource;
import com.example.foodplaner.Database.SharedPrefrencesDataSourceImplementation;
import com.example.foodplaner.Features.Authentication.view.AuthView;
import com.example.foodplaner.network.FirebaseDataSource;
import com.example.foodplaner.network.FirebaseDataSourceImpl;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MealRepositoryImplementation implements MealRepository {
    MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion;
    MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation;
    FirebaseDataSourceImpl firebaseDataSource;
    public static MealRepositoryImplementation repo=null;
    private final FirebaseAuth auth;
    private FirebaseFirestore firestore;
    SharedPrefrencesDataSourceImplementation sharedPrefrencesDataSource;



    public MealRepositoryImplementation(MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation, MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion, FirebaseDataSourceImpl firebaseDataSource, SharedPrefrencesDataSourceImplementation sharedPrefrencesDataSourceImplementation) {
        this.mealsLocalDataSourceImplementation=mealsLocalDataSourceImplementation;
        this.mealsRemoteDataSourceImplementaion = mealsRemoteDataSourceImplementaion;
        this.auth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
        this.firebaseDataSource=firebaseDataSource;
        this.sharedPrefrencesDataSource=sharedPrefrencesDataSourceImplementation;
    }

    public static MealRepositoryImplementation getInstance(MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation,MealsRemoteDataSourceImplementaion mealsRemoteDataSourceImplementaion,FirebaseDataSourceImpl firebaseDataSource,SharedPrefrencesDataSourceImplementation sharedPrefrencesDataSourceImplementation){
        if (repo==null)
        {
            repo =new MealRepositoryImplementation(mealsLocalDataSourceImplementation,mealsRemoteDataSourceImplementaion,firebaseDataSource,sharedPrefrencesDataSourceImplementation);
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
    public boolean isUserGuest() {
        return sharedPrefrencesDataSource.isGuest();
    }

    @Override
    public void setIsGuest(boolean isGuest) {
        sharedPrefrencesDataSource.setIsGuest(isGuest);
    }


    @Override
    public Single<CountryModel> getMealsCountries() {
        return mealsRemoteDataSourceImplementaion.getMealCountries();
    }

    @Override
    public Single<Ingredients> getMealsIngredients() {
        return mealsRemoteDataSourceImplementaion.getIngredients();
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
    public Single<FilteredMeals> getMealsFilteredByIngredients(String ingredientName) {
        return mealsRemoteDataSourceImplementaion.getMealFilteredByIngredient(ingredientName);
    }


    @Override
    public Single<Meal> getMealDetailsById(String id) {
        return mealsRemoteDataSourceImplementaion.getMealDetailsById(id);
    }

    @Override
    public Completable insertMealToFavorite(MealElement mealElement) {
        return mealsLocalDataSourceImplementation.insertMealToFavorite(mealElement);
    }

    @Override
    public Single<List<MealElement>> getAllFavouriteMeals() {
        return mealsLocalDataSourceImplementation.getAllFavouriteMeals();
    }


    @Override
    public Completable removeMealFromFavorite(MealElement mealElement) {
        return mealsLocalDataSourceImplementation.deleteMealFromFavorite(mealElement);
    }

    @Override
    public Completable removeAllFavoriteMeals() {
        return mealsLocalDataSourceImplementation.removeAllData();
    }
    @Override
    public Single<List<PlannedMeal>> getAllPlannedMeals() {
        return mealsLocalDataSourceImplementation.getAllPlannedMeals();
    }

    @Override
    public Completable insertPlannedMeal(PlannedMeal plannedMeal) {
        return mealsLocalDataSourceImplementation.insertPlannedMeal(plannedMeal);
    }

    @Override
    public Completable removePlannedMeal(PlannedMeal plannedMeal) {
        return mealsLocalDataSourceImplementation.deletePlannedMeal(plannedMeal);
    }

    @Override
    public Completable removeAllPlannedMeals() {
        return mealsLocalDataSourceImplementation.removeAllPlannedMeals();
    }



    @Override
    public Completable insertAllFavorites(List<MealElement> favorites) {
        return mealsLocalDataSourceImplementation.insertAllFavorites(favorites);
    }

    @Override
    public Completable insertAllPlannedMeals(List<PlannedMeal> plannedMeals) {
        return mealsLocalDataSourceImplementation.insertAllPlannedMeals(plannedMeals);
    }
    public Completable backupDataToFirestore(String userId, FirebaseFirestore firestore) {
        return firebaseDataSource.backupDataToFirestore(userId,firestore);
    }

    public Completable restoreDataFromFirestore(String userId, FirebaseFirestore firestore) {
        return firebaseDataSource.restoreDataFromFirestore(userId, firestore);
    }

    @Override
    public void signInWithEmail(String email, String password, AuthView authView) {
         firebaseDataSource.signInWithEmail(email, password, authView);
    }

    @Override
    public void signInWithGoogle(String idToken, AuthView authView) {
         firebaseDataSource.signInWithGoogle(idToken, authView);
    }

    @Override
    public Single<FirebaseUser> signUpWithEmail(String email, String password) {
        return firebaseDataSource.signUpWithEmail(email, password);
    }


}

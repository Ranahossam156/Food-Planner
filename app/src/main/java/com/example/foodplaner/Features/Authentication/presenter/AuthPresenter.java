package com.example.foodplaner.Features.Authentication.presenter;

import android.content.Context;

import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface AuthPresenter {
    public Single<List<MealElement>> downloadFavorites();

    public Single<List<PlannedMeal>> downloadPlannedMeals();

    public void backupDataOnLogout(String userId);

    public void restoreDataOnLogin(String userId);
    public void signInWithEmail(String email, String password);
    public void signInWithGoogle(String idToken);
    public void signUpWithEmail(String email, String password);
    void setGuest(boolean isGuest);
    Boolean isGuest();
}

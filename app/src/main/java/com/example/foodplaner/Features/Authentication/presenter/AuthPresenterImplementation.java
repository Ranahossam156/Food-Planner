package com.example.foodplaner.Features.Authentication.presenter;

import android.content.Context;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Database.SharedPrefrencesDataSourceImplementation;
import com.example.foodplaner.Features.Authentication.view.AuthView;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepository;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.model.PlannedMeal;
import com.example.foodplaner.network.FirebaseDataSourceImpl;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AuthPresenterImplementation implements AuthPresenter {
    private FirebaseFirestore firestore;
    MealRepository mealRepository;
    private Context context;
    AuthView authView;

    public AuthPresenterImplementation(Context context,AuthView authView) {

        this.firestore = FirebaseFirestore.getInstance();
        this.context=context;
        this.authView=authView;
        mealRepository= MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(context.getApplicationContext()), MealsRemoteDataSourceImplementaion.getInstance(), FirebaseDataSourceImpl.getInstance(context), SharedPrefrencesDataSourceImplementation.getInstance(context));
    }


    @Override
    public Single<List<MealElement>> downloadFavorites() {
        return null;
    }

    @Override
    public Single<List<PlannedMeal>> downloadPlannedMeals() {
        return null;
    }

    @Override
    public void backupDataOnLogout(String userId) {
        mealRepository.backupDataToFirestore(userId, firestore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    authView.onBackupSuccess();
                }, throwable -> {
                    String errorMessage = "Backup failed: " + throwable.getMessage();
                    if (throwable.getMessage().contains("Failed to delete previous backup")) {
                        errorMessage += "\nPlease check your internet connection";
                    }
                    authView.onBackupError(errorMessage);
                });
    }


    @Override
    public void restoreDataOnLogin(String userId) {
        mealRepository.restoreDataFromFirestore(userId, firestore)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    authView.onRestoreSuccess();
                }, throwable -> {
                    authView.onRestoreError(throwable.getMessage());
                });
    }
    @Override
    public void signInWithEmail(String email, String password) {
        mealRepository.signInWithEmail(email, password, authView);
    }

    @Override
    public void signInWithGoogle(String idToken) {
        mealRepository.signInWithGoogle(idToken, authView);
    }
    @Override
    public void signUpWithEmail(String email, String password) {
        mealRepository.signUpWithEmail(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        user -> authView.onAuthSuccess(user),
                        error -> authView.onAuthFailure(error.getMessage())
                );
    }
    @Override
    public void setGuest(boolean isGuest) {
        mealRepository.setIsGuest(isGuest);
    }

    @Override
    public Boolean isGuest() {
        return mealRepository.isUserGuest();
    }

}

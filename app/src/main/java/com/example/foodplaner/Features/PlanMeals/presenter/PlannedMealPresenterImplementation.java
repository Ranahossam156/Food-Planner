package com.example.foodplaner.Features.PlanMeals.presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.foodplaner.Features.PlanMeals.view.PlannedView;
import com.example.foodplaner.model.MealRepository;
import com.example.foodplaner.model.PlannedMeal;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PlannedMealPresenterImplementation implements PlanMealsPresenter{
    private PlannedView view;
    private MealRepository repo;

    public PlannedMealPresenterImplementation(PlannedView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void getPlannedMeals() {
        repo.getAllPlannedMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<PlannedMeal>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onSuccess(@NonNull List<PlannedMeal> plannedMeals) {
                        Log.d("PlannedMealPresenter", "Received " + plannedMeals.size() + " planned meals");
                        if (view != null) {
                            view.getPlanned(plannedMeals);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("PlannedMealPresenter", "Error fetching planned meals", e);
                        if (view != null) {
                            view.getPlanned(Collections.emptyList());
                        }
                    }
                });
    }

    @Override
    public void onPlannedeRemoved(PlannedMeal mealElement) {
        repo.removePlannedMeal(mealElement)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            getPlannedMeals();
                        }
                );
    }
}

package com.example.foodplaner.Features.Favorites.presenter;

import android.util.Log;

import com.example.foodplaner.Features.Favorites.view.FavoriteView;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepository;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FavoritePresenterImplementation implements FavoritePresenter{
    private FavoriteView view;
    private MealRepository repo;

    public FavoritePresenterImplementation(FavoriteView view, MealRepository repo) {
        this.view = view;
        this.repo = repo;
    }

    @Override
    public void getFavoriteMeals() {
        repo.getAllFavouriteMeals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        meals -> {
                            Log.d("FavoritePresenter", "Number of favorites:" +
                                    " " + meals.size());
                            if (view != null) {
                                view.getFavorites(meals);
                            }
                        }
                );
    }

    @Override
    public void onFavoriteRemoved(MealElement meal) {
        repo.removeMealFromFavorite(meal)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> {
                            getFavoriteMeals();
                        }
                );
    }
}

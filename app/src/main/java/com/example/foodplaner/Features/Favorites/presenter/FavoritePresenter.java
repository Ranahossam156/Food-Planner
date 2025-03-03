package com.example.foodplaner.Features.Favorites.presenter;

import com.example.foodplaner.model.MealElement;

public interface FavoritePresenter {
    void getFavoriteMeals();
    void onFavoriteRemoved(MealElement mealElement);
    void setGuest(boolean isGuest);
    Boolean isGuest();

}

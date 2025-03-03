package com.example.foodplaner.Features.Favorites.view;

import com.example.foodplaner.model.MealElement;

import java.util.List;

public interface FavoriteView {
    void getFavorites(List<MealElement> meals);
    void showGuestMessage(boolean isGuest);

}

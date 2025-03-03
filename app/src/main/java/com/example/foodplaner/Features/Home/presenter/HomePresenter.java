package com.example.foodplaner.Features.Home.presenter;

import com.example.foodplaner.model.Categories;

public interface HomePresenter {
    void getRandomMeal();
    void getCategories();
    void getCountries();
    void setGuest(boolean isGuest);
    Boolean isGuest();
}

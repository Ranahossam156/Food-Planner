package com.example.foodplaner.network;


import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealElement;

import java.util.List;

public interface NetworkCallBack <T>{
    public void  onSuccessResult(T response);
    public void onFailureResult(String errorMsg);
}

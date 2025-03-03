package com.example.foodplaner.Database;

public interface SharedPrefrencesDataSource {
    void setIsGuest(boolean isGuest);
    boolean isGuest();
}

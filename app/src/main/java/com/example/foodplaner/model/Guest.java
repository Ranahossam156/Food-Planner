package com.example.foodplaner.model;

import android.content.Context;
import android.content.SharedPreferences;

public class Guest {
    private SharedPreferences sharedPreferences;

    public Guest(Context context) {
        sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
    }

    public void setGuestUser(boolean isGuest) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_guest", isGuest);
        editor.apply();
    }

    public boolean isGuestUser() {
        return sharedPreferences.getBoolean("is_guest", false);
    }

    public void clearSession() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
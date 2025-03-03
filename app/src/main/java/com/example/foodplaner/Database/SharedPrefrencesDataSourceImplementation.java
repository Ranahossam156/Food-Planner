package com.example.foodplaner.Database;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrencesDataSourceImplementation implements SharedPrefrencesDataSource{
    private static final String PREF_NAME = "food_planner_prefs";
    private static final String KEY_IS_GUEST = "is_guest";
    private final SharedPreferences sharedPreferences;
    static SharedPrefrencesDataSourceImplementation sharedPrefrencesDataSourceImplementation;

    public SharedPrefrencesDataSourceImplementation(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    public static SharedPrefrencesDataSourceImplementation getInstance(Context context) {
        if (sharedPrefrencesDataSourceImplementation == null) {
            sharedPrefrencesDataSourceImplementation = new SharedPrefrencesDataSourceImplementation(context);
        }
        return sharedPrefrencesDataSourceImplementation;
    }


    @Override
    public void setIsGuest(boolean isGuest) {
        sharedPreferences.edit().putBoolean(KEY_IS_GUEST, isGuest).apply();
    }

    @Override
    public boolean isGuest() {
        return sharedPreferences.getBoolean(KEY_IS_GUEST, false);
    }
}

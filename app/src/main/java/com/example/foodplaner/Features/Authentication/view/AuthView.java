package com.example.foodplaner.Features.Authentication.view;

public interface AuthView {
    void onBackupSuccess();
    void onRestoreSuccess();

    void onBackupError(String message);

    void onRestoreError(String message);

}

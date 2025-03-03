package com.example.foodplaner.Features.Authentication.view;

import com.google.firebase.auth.FirebaseUser;

public interface AuthView {
    void onBackupSuccess();
    void onRestoreSuccess();

    void onBackupError(String message);

    void onRestoreError(String message);
    void onAuthSuccess(FirebaseUser user);
    void onAuthFailure(String message);

}

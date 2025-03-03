// AuthDataSource.java
package com.example.foodplaner.network;

import com.example.foodplaner.Features.Authentication.view.AuthView;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;
import com.example.foodplaner.model.UserBackup;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface FirebaseDataSource {
    public Completable backupDataToFirestore(String userId, FirebaseFirestore firestore);
    public Completable restoreDataFromFirestore(String userId, FirebaseFirestore firestore);
    public void signInWithEmail(String email, String password, AuthView authView);
    public void signInWithGoogle(String idToken, AuthView authView);
    public Single<FirebaseUser> signUpWithEmail(String email, String password);
}

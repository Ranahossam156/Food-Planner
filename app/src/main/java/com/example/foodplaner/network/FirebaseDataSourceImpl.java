package com.example.foodplaner.network;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Features.Authentication.view.AuthView;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;
import com.example.foodplaner.model.UserBackup;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FirebaseDataSourceImpl implements FirebaseDataSource {
    private static FirebaseDataSourceImpl firebaseDataSourceImpl;

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;
    MealsLocalDataSourceImplementation mealsLocalDataSourceImplementation;
    Context context;

    public FirebaseDataSourceImpl(Context context) {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        this.context=context;
        mealsLocalDataSourceImplementation=MealsLocalDataSourceImplementation.getInstance(context);
    }
    public static FirebaseDataSourceImpl getInstance(Context context) {
        if (firebaseDataSourceImpl == null) {
            firebaseDataSourceImpl = new FirebaseDataSourceImpl(context);
        }
        return firebaseDataSourceImpl;
    }
    public Completable backupDataToFirestore(String userId, FirebaseFirestore firestore) {
        return Completable.create(emitter -> {
            firestore.collection("users").document(userId)
                    .delete()
                    .addOnSuccessListener(__ -> {
                        Single.zip(
                                        mealsLocalDataSourceImplementation .getAllFavouriteMeals(),
                                        mealsLocalDataSourceImplementation .getAllPlannedMeals(),
                                        (favorites, planned) -> {
                                            UserBackup backup = new UserBackup();
                                            backup.setFavorites(favorites);
                                            backup.setPlannedMeals(planned);
                                            return backup;
                                        }
                                )
                                .flatMapCompletable(backup ->
                                        Completable.create(emitter2 -> {
                                            firestore.collection("users")
                                                    .document(userId)
                                                    .set(backup)
                                                    .addOnSuccessListener(__2 -> {
                                                        mealsLocalDataSourceImplementation.removeAllData()
                                                                .andThen(mealsLocalDataSourceImplementation.removeAllPlannedMeals())
                                                                .subscribe(emitter2::onComplete, emitter2::onError);
                                                    })
                                                    .addOnFailureListener(emitter2::onError);
                                        })
                                ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(emitter::onComplete, emitter::onError);
                    })
                    .addOnFailureListener(e -> {
                        emitter.onError(new Exception("Failed to delete previous backup: " + e.getMessage()));
                    });
        });
    }

    public Completable restoreDataFromFirestore(String userId, FirebaseFirestore firestore) {
        return Completable.create(emitter -> {
            firestore.collection("users")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            UserBackup backup = documentSnapshot.toObject(UserBackup.class);
                            if (backup != null) {
                                mealsLocalDataSourceImplementation.removeAllData().andThen(mealsLocalDataSourceImplementation.removeAllPlannedMeals())
                                        .andThen(mealsLocalDataSourceImplementation.insertAllFavorites(backup.getFavorites()))
                                        .andThen(mealsLocalDataSourceImplementation.insertAllPlannedMeals(backup.getPlannedMeals()))
                                        .subscribe(() -> emitter.onComplete(), emitter::onError);
                            } else {
                                emitter.onError(new Exception("Invalid backup format"));
                            }
                        } else {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public void signInWithEmail(String email, String password, AuthView authView) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            restoreDataFromFirestore(user.getUid(), firestore)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> authView.onRestoreSuccess(),
                                            error -> authView.onRestoreError("Restore failed: " + error.getMessage())
                                    );
                        } else {
                            authView.onRestoreError("User is null after login");
                        }
                    } else {
                        authView.onRestoreError("Authentication failed");
                    }
                });
    }

    @Override
    public void signInWithGoogle(String idToken, AuthView authView) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            restoreDataFromFirestore(user.getUid(), firestore)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            () -> authView.onRestoreSuccess(),
                                            error -> authView.onRestoreError("Restore failed: " + error.getMessage())
                                    );
                        } else {
                            authView.onRestoreError("User is null after login");
                        }
                    } else {
                        authView.onRestoreError("Google sign-in failed");
                    }
                });
    }

    @Override
    public Single<FirebaseUser> signUpWithEmail(String email, String password) {
        return Single.create(emitter ->
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                emitter.onSuccess(auth.getCurrentUser());
                            } else {
                                emitter.onError(task.getException());
                            }
                        })
        );
    }
}
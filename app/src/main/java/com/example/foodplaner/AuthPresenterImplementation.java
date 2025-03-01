package com.example.foodplaner;

import android.content.Context;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import com.example.foodplaner.Features.Authentication.presenter.AuthPresenter;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.PlannedMeal;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;

import java.util.List;
import java.util.ArrayList;

public class AuthPresenterImplementation implements AuthPresenter {
    private FirebaseFirestore db;
    private String userId;
    private Context context;

    public AuthPresenterImplementation(String userId) {
        this.db = FirebaseFirestore.getInstance();
        this.userId = userId;
    }

//    public Completable backupData() {
//        return backupFavorites()
//                .onErrorComplete(e -> {
//                    Log.e("Backup", "Failed to backup favorites", e);
//                    return true;
//                })
//                .andThen(backupPlannedMeals()
//                        .onErrorComplete(e -> {
//                            Log.e("Backup", "Failed to backup planned meals", e);
//                            return true;
//                        })
//                );
//    }

//    public Completable backupFavorites() {
//        return Completable.create(emitter -> {
//            // Get favorites from LOCAL DATABASE, not Firestore
//            MealsLocalDataSourceImplementation.getInstance(context)
//                    .getAllFavouriteMeals()
//                    .subscribe(favorites -> {
//                        List<Completable> tasks = new ArrayList<>();
//                        for (MealElement meal : favorites) {
//                            Completable task = Completable.create(e -> {
//                                firestore.collection("users").document(userId)
//                                        .collection("favorites")
//                                        .document(meal.getIdMeal())
//                                        .set(meal)
//                                        .addOnSuccessListener(aVoid -> e.onComplete())
//                                        .addOnFailureListener(e::onError);
//                            });
//                            tasks.add(task);
//                        }
//                        Completable.merge(tasks)
//                                .subscribe(() -> emitter.onComplete(), emitter::onError);
//                    }, emitter::onError);
//        });
//    }

//    public Completable backupPlannedMeals() {
//        return Completable.create(emitter -> {
//            // Get planned meals from LOCAL DATABASE
//            MealsLocalDataSourceImplementation.getInstance(context)
//                    .getAllPlannedMeals()
//                    .subscribe(plannedMeals -> {
//                        List<Completable> tasks = new ArrayList<>();
//
//                        for (PlannedMeal meal : plannedMeals) {
//                            Completable task = Completable.create(e -> {
//                                firestore.collection("users").document(userId)
//                                        .collection("plannedMeals")
//                                        .document(meal.getIdMeal())
//                                        .set(meal)
//                                        .addOnSuccessListener(aVoid -> e.onComplete())
//                                        .addOnFailureListener(e::onError);
//                            });
//                            tasks.add(task);
//                        }
//
//                        Completable.merge(tasks)
//                                .subscribe(() -> emitter.onComplete(), emitter::onError);
//                    }, emitter::onError);
//        });
//    }
//
//    @Override
//    public Completable logoutAndBackupAndClear(Context context) {
//        return backupData()
//                .onErrorComplete() // Continue even if backup fails
//                .andThen(Completable.fromAction(() -> {
//                    MealsLocalDataSourceImplementation.getInstance(context).removeAllData();
//                    MealsLocalDataSourceImplementation.getInstance(context).removeAllPlannedMeals();
//                    FirebaseAuth.getInstance().signOut();
//                }))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    @Override
//    public Completable restoreData() {
//        return Completable.concatArray(restoreFavorites(), restorePlannedMeals());
//    }

    public Completable uploadFavorites(List<MealElement> favorites) {
        return Completable.create(emitter -> {
            WriteBatch batch = db.batch();
            CollectionReference favRef = db.collection("users").document(userId).collection("favorites");

            // Delete existing favorites
            favRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        batch.delete(doc.getReference());
                    }
                    // Add new favorites
                    for (MealElement meal : favorites) {
                        DocumentReference docRef = favRef.document(meal.getIdMeal());
                        batch.set(docRef, meal);
                    }
                    batch.commit()
                            .addOnSuccessListener(aVoid -> emitter.onComplete())
                            .addOnFailureListener(e -> emitter.onError(e));
                } else {
                    emitter.onError(task.getException());
                }
            });
        });
    }

    // Download Favorites
    public Single<List<MealElement>> downloadFavorites() {
        return Single.create(emitter -> {
            db.collection("users").document(userId).collection("favorites")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<MealElement> meals = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            MealElement meal = doc.toObject(MealElement.class);
                            meals.add(meal);
                        }
                        emitter.onSuccess(meals);
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }


//    @Override
//    public Completable restoreFavorites() {
//        return Completable.create(emitter -> {
//            firestore.collection("users")
//                    .document(userId)
//                    .collection("favorites")
//                    .get()
//                    .addOnSuccessListener(querySnapshot -> {
//                        List<MealElement> favorites = querySnapshot.toObjects(MealElement.class);
//                        MealsLocalDataSourceImplementation localDS = MealsLocalDataSourceImplementation.getInstance(context);
//                        // Merge all insert operations into a single Completable.
//                        Completable.merge(favorites.stream()
//                                        .map(localDS::insertMealToFavorite)
//                                        .collect(java.util.stream.Collectors.toList()))
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(() -> emitter.onComplete(), emitter::onError);
//                    })
//                    .addOnFailureListener(emitter::onError);
//        });
//    }

//    @Override
//    public Completable restorePlannedMeals() {
//        return Completable.create(emitter -> {
//            firestore.collection("users")
//                    .document(userId)
//                    .collection("plannedMeals")
//                    .get()
//                    .addOnSuccessListener(querySnapshot -> {
//                        List<PlannedMeal> plannedMeals = querySnapshot.toObjects(PlannedMeal.class);
//                        MealsLocalDataSourceImplementation localDS = MealsLocalDataSourceImplementation.getInstance(context);
//                        Completable.merge(plannedMeals.stream()
//                                        .map(localDS::insertPlannedMeal)
//                                        .collect(java.util.stream.Collectors.toList()))
//                                .subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(() -> emitter.onComplete(), emitter::onError);
//                    })
//                    .addOnFailureListener(emitter::onError);
//        });
//    }
// Upload Planned Meals
public Completable uploadPlannedMeals(List<PlannedMeal> plannedMeals) {
    return Completable.create(emitter -> {
        CollectionReference plannedRef = db.collection("users")
                .document(userId)
                .collection("plannedMeals");

        plannedRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                WriteBatch batch = db.batch();
                // Delete existing planned meals
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    batch.delete(doc.getReference());
                }

                // Add new planned meals
                for (PlannedMeal meal : plannedMeals) {
                    DocumentReference docRef = plannedRef.document(meal.getIdMeal());
                    batch.set(docRef, meal);
                }

                batch.commit()
                        .addOnSuccessListener(aVoid -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError);
            } else {
                emitter.onError(task.getException());
            }
        });
    });
}

    // Download Planned Meals
    public Single<List<PlannedMeal>> downloadPlannedMeals() {
        return Single.create(emitter -> {
            db.collection("users")
                    .document(userId)
                    .collection("plannedMeals")
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        List<PlannedMeal> meals = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            PlannedMeal meal = doc.toObject(PlannedMeal.class);
                            meals.add(meal);
                        }
                        emitter.onSuccess(meals);
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}

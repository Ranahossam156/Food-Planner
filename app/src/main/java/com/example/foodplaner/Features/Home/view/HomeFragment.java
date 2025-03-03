package com.example.foodplaner.Features.Home.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Database.SharedPrefrencesDataSourceImplementation;
import com.example.foodplaner.Features.Authentication.presenter.AuthPresenter;
import com.example.foodplaner.Features.Authentication.presenter.AuthPresenterImplementation;
import com.example.foodplaner.Features.Authentication.view.AuthView;
import com.example.foodplaner.Features.Home.presenter.HomePresenter;
import com.example.foodplaner.Features.Home.presenter.HomePresenterImplementation;
import com.example.foodplaner.Utils.DialogUtils;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.R;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealCountry;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.FirebaseDataSourceImpl;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements HomeView, OnCategoryClickListener, OnCountryClickListener, AuthView {
    CategoriesAdapter categoriesAdapter;
    RecyclerView CategoriesrecyclerView, countriesrecyclerView;
    ImageView mealOfTheDayImage;
    TextView mealOfTheDayName;
    private Meal randomMeal;
    HomePresenter homePresenter;
    CountriesAdapter countriesAdapter;
    CardView mealCard;
    MealElement mealElement;
    ImageView logout;
    AuthPresenter authPresenter;
    NavController navController;
    FirebaseAuth auth;
    FirebaseUser user;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CategoriesrecyclerView = view.findViewById(R.id.CategoriesRecyclerView);
        CategoriesrecyclerView.setHasFixedSize(true);
        mealCard = view.findViewById(R.id.mealCard);
        homePresenter = new HomePresenterImplementation(this, MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(this.getContext()), MealsRemoteDataSourceImplementaion.getInstance(), FirebaseDataSourceImpl.getInstance(getContext()), SharedPrefrencesDataSourceImplementation.getInstance(getContext())));
        countriesrecyclerView = view.findViewById(R.id.CountriesRecyclerView);
        countriesrecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager countrieslayoutManager = new LinearLayoutManager(this.getContext());
        countrieslayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        CategoriesrecyclerView.setLayoutManager(layoutManager);
        countriesrecyclerView.setLayoutManager(countrieslayoutManager);
        categoriesAdapter = new CategoriesAdapter(getContext(), this);
        countriesAdapter = new CountriesAdapter(getContext(), this);
        countriesrecyclerView.setAdapter(countriesAdapter);
        CategoriesrecyclerView.setAdapter(categoriesAdapter);
        List<Category> categories = new ArrayList<>();
        mealOfTheDayImage = view.findViewById(R.id.MealOfTheDayImage);
        mealOfTheDayName = view.findViewById(R.id.MealOfTheDayName);
        logout = view.findViewById(R.id.logouticon);
        homePresenter.getRandomMeal();
        homePresenter.getCategories();
        homePresenter.getCountries();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        authPresenter = new AuthPresenterImplementation(getContext(), this);
        navController = NavHostFragment.findNavController(this);

        mealCard.setOnClickListener(v -> {
            if (mealElement != null) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealElement);
                navController.navigate(action);
            }
        });
        boolean isGuest = homePresenter.isGuest();

        if (isGuest) {
            logout.setImageResource(R.drawable.login);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavDirections action = HomeFragmentDirections.actionHomeFragmentToSigninFragment();
                    Navigation.findNavController(requireView()).navigate(action);
                }
            });
        }
        else{
            logout.setVisibility(view.VISIBLE);
            logout.setOnClickListener(view2 -> {
                showLogoutConfirmationDialog();
            });
        }


    }

    private void showLogoutConfirmationDialog() {
        DialogUtils.showConfirmationDialog(requireContext(),
                "Are you sure you want to logout?",
                (dialog, which) -> {
                    if (user != null) {
                        authPresenter.backupDataOnLogout(user.getUid());
                        homePresenter.setGuest(true);
                    } else {
                        Toast.makeText(getContext(), "No user logged in!", Toast.LENGTH_SHORT).show();
                    }

                });
    }

    @Override
    public void onGetMealOfTheDay(List<MealElement> meal) {
        mealElement = meal.get(0);
        mealOfTheDayName.setText(mealElement.getStrMeal());
        Glide.with(requireContext())
                .load(mealElement.getStrMealThumb())
                .into(mealOfTheDayImage);
    }

    @Override
    public void getCategories(List<Category> categoryList) {
        categoriesAdapter.setCategoryList(categoryList);
        categoriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void getCountries(List<MealCountry> countryList) {
        countriesAdapter.setCountryList(countryList);
        countriesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showError(String errorMsg) {
        Log.i("Home Fragment", "showError: " + errorMsg);
    }

    @Override
    public void onCategoryClick(Category category) {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(category.getStrCategory());
        navController.navigate(action);
    }

    @Override
    public void onCountryClick(MealCountry mealCountry) {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(mealCountry.getStrArea());
        navController.navigate(action);
    }

    @Override
    public void onBackupSuccess() {
        FirebaseAuth.getInstance().signOut();
        if (isAdded()) {
            NavController navController = NavHostFragment.findNavController(this);
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToSigninFragment());
        }
    }

    @Override
    public void onBackupError(String message) {
        Toast.makeText(getContext(), "Backup failed: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRestoreSuccess() {

    }

    @Override
    public void onRestoreError(String message) {

    }

    @Override
    public void onAuthSuccess(FirebaseUser user) {

    }

    @Override
    public void onAuthFailure(String message) {

    }

}
package com.example.foodplaner.Features.Home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Features.Authentication.presenter.AuthPresenter;
import com.example.foodplaner.AuthPresenterImplementation;
import com.example.foodplaner.Features.Home.presenter.HomePresenter;
import com.example.foodplaner.Features.Home.presenter.HomePresenterImplementation;
import com.example.foodplaner.Utils.DialogUtils;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.R;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealCountry;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepository;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HomeFragment extends Fragment implements HomeView, OnCategoryClickListener,OnCountryClickListener{
    CategoriesAdapter categoriesAdapter;
    RecyclerView CategoriesrecyclerView,countriesrecyclerView;
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
        mealCard=view.findViewById(R.id.mealCard);
        homePresenter = new HomePresenterImplementation(this, MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(this.getContext()),MealsRemoteDataSourceImplementaion.getInstance()));
        countriesrecyclerView=view.findViewById(R.id.CountriesRecyclerView);
        countriesrecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        LinearLayoutManager countrieslayoutManager = new LinearLayoutManager(this.getContext());
        countrieslayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        CategoriesrecyclerView.setLayoutManager(layoutManager);
        countriesrecyclerView.setLayoutManager(countrieslayoutManager);
        categoriesAdapter = new CategoriesAdapter(getContext(),this);
        countriesAdapter=new CountriesAdapter(getContext(),this);
        countriesrecyclerView.setAdapter(countriesAdapter);
        CategoriesrecyclerView.setAdapter(categoriesAdapter);
        List<Category> categories = new ArrayList<>();
        mealOfTheDayImage=view.findViewById(R.id.MealOfTheDayImage);
        mealOfTheDayName=view.findViewById(R.id.MealOfTheDayName);
        logout=view.findViewById(R.id.logouticon);
        homePresenter.getRandomMeal();
        homePresenter.getCategories();
        homePresenter.getCountries();
        navController = NavHostFragment.findNavController(this);

        mealCard.setOnClickListener(v -> {
            if (mealElement != null) {
                NavDirections action = HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealElement);
                navController.navigate(action);
            }
        });
        logout.setOnClickListener(view2 -> {
        showLogoutConfirmationDialog();
                });


        //presenter.subscribeFavorites();
    }
    private void showLogoutConfirmationDialog() {
        DialogUtils.showConfirmationDialog(requireContext(),
                "Are you sure you want to logout?",
                (dialog, which) -> logout());
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
        Log.i("Home Fragment", "showError: "+errorMsg);
    }

    @Override
    public void onCategoryClick(Category category) {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(category.getStrCategory());
        // Remove this line:
        // NavController navController = NavHostFragment.findNavController(HomeFragment.this);
        navController.navigate(action);
    }

    @Override
    public void onCountryClick(MealCountry mealCountry) {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(mealCountry.getStrArea());
        // Remove this line:
        // NavController navController = NavHostFragment.findNavController(HomeFragment.this);
        navController.navigate(action);
    }



    public void logout() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MealRepository mealRepository=MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(this.getContext()),MealsRemoteDataSourceImplementaion.getInstance());
        if (user == null) return;

        AuthPresenter firestoreHelper = new AuthPresenterImplementation(user.getUid());
        mealRepository.getAllFavouriteMeals()
                .zipWith(mealRepository.getAllPlannedMeals(), (favorites, planned) -> Pair.create(favorites, planned))
                .flatMapCompletable(pair ->
                        firestoreHelper.uploadFavorites(pair.first)
                                .andThen(firestoreHelper.uploadPlannedMeals(pair.second))
                )
                .andThen(mealRepository.removeAllFavoriteMeals())
                .andThen(mealRepository.removeAllPlannedMeals())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    FirebaseAuth.getInstance().signOut();
                    NavDirections action = HomeFragmentDirections.actionHomeFragmentToSigninFragment();
                //    NavController navController = NavHostFragment.findNavController(HomeFragment.this);
                    navController.navigate(action);
                }, error -> {
                    // Handle Error
                });
    }
}
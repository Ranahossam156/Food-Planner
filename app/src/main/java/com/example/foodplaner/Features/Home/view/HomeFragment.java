package com.example.foodplaner.Features.Home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.Features.Home.presenter.HomePresenter;
import com.example.foodplaner.Features.Home.presenter.HomePresenterImplementation;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.R;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements HomeView{
    HomeAdapter homeAdapter;
    RecyclerView recyclerView;
    ImageView mealOfTheDayImage;
    TextView mealOfTheDayName;
    private Meal randomMeal;
    HomePresenter homePresenter;



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        homePresenter = new HomePresenterImplementation(this, MealRepositoryImplementation.getInstance(MealsRemoteDataSourceImplementaion.getInstance()));
        homePresenter.getRandomMeal();

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
        recyclerView = view.findViewById(R.id.CategoriesRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(getContext());
        recyclerView.setAdapter(homeAdapter);
        List<Category> categories = new ArrayList<>();
        mealOfTheDayImage=view.findViewById(R.id.MealOfTheDayImage);
        mealOfTheDayName=view.findViewById(R.id.MealOfTheDayName);

        categories.add(new Category("Beef", "Beef Desc", "1","https://www.themealdb.com/images/category/beef.png"));
        categories.add(new Category("Chicken", "Chicken desc", "2","https://www.themealdb.com/images/category/chicken.png"));
        // Add more categories...

        homeAdapter.setProductList(categories);
        homePresenter.getRandomMeal();
        //presenter.subscribeFavorites();
    }

    @Override
    public void onGetMealOfTheDay(Meal meal) {
        if (meal != null && meal.getMeals() != null && !meal.getMeals().isEmpty()) {
            MealElement mealElement = meal.getMeals().get(0);
            // Update the meal name
            mealOfTheDayName.setText(mealElement.getStrMeal());
            // Load the meal image using Glide
            Glide.with(requireContext())
                    .load(mealElement.getStrMealThumb())
                    .into(mealOfTheDayImage);
        }
    }
}
package com.example.foodplaner.Features.Home.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodplaner.Features.Home.presenter.HomePresenter;
import com.example.foodplaner.Features.Home.presenter.HomePresenterImplementation;
import com.example.foodplaner.model.Categories;
import com.example.foodplaner.model.Category;
import com.example.foodplaner.R;
import com.example.foodplaner.model.CountryModel;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealCountry;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;

import java.util.ArrayList;
import java.util.List;


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
        CategoriesrecyclerView = view.findViewById(R.id.CategoriesRecyclerView);
        CategoriesrecyclerView.setHasFixedSize(true);
        mealCard=view.findViewById(R.id.mealCard);
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

//        categories.add(new Category("Beef", "Beef Desc", "1","https://www.themealdb.com/images/category/beef.png"));
//        categories.add(new Category("Chicken", "Chicken desc", "2","https://www.themealdb.com/images/category/chicken.png"));
//
//        categoriesAdapter.setCategoryList(categories);
        homePresenter.getRandomMeal();
        homePresenter.getCategories();
        homePresenter.getCountries();

        mealCard.setOnClickListener(v -> {
            if (mealElement != null) {
                NavController navController = Navigation.findNavController(view);
                navController.navigate(
                        HomeFragmentDirections.actionHomeFragmentToMealDetailsFragment(mealElement)
                );
            }
        });

        //presenter.subscribeFavorites();
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
        Navigation.findNavController(requireView()).navigate(action);
    }

    @Override
    public void onCountryClick(MealCountry mealCountry) {
        NavDirections action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(mealCountry.getStrArea());
        Navigation.findNavController(requireView()).navigate(action);
    }
}
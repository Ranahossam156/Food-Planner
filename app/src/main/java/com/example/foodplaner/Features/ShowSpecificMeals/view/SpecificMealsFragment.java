package com.example.foodplaner.Features.ShowSpecificMeals.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.foodplaner.Database.MealsLocalDataSourceImplementation;
import com.example.foodplaner.Database.SharedPrefrencesDataSourceImplementation;
import com.example.foodplaner.Features.Home.view.HomeFragmentDirections;
import com.example.foodplaner.Features.ShowSpecificMeals.presenter.SpecificMealsPresenterImplementation;
import com.example.foodplaner.Features.ShowSpecificMeals.presenter.SpecificMealsPresenter;
import com.example.foodplaner.R;
import com.example.foodplaner.model.FilteredMeal;
import com.example.foodplaner.model.Meal;
import com.example.foodplaner.model.MealElement;
import com.example.foodplaner.model.MealRepositoryImplementation;
import com.example.foodplaner.network.FirebaseDataSourceImpl;
import com.example.foodplaner.network.MealsRemoteDataSourceImplementaion;

import java.util.List;

public class SpecificMealsFragment extends Fragment implements SpecificMealsView, OnMealClickListener,OnFavoriteClickListener {
    SpecificMealsPresenter specificMealsPresenter;
    String filterName;
    MealsGridAdapter categoriesGridAdapter;


    public SpecificMealsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            SpecificMealsFragmentArgs args = SpecificMealsFragmentArgs.fromBundle(getArguments());
            filterName = args.getSpecificMealName();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = view.findViewById(R.id.categoriesgridRecyclerView);
        categoriesGridAdapter = new MealsGridAdapter(getContext(),this,this);
        gridView.setAdapter(categoriesGridAdapter);
        specificMealsPresenter =new SpecificMealsPresenterImplementation(this, MealRepositoryImplementation.getInstance(MealsLocalDataSourceImplementation.getInstance(getContext()),MealsRemoteDataSourceImplementaion.getInstance(),FirebaseDataSourceImpl.getInstance(getContext()), SharedPrefrencesDataSourceImplementation.getInstance(getContext())));
        specificMealsPresenter.getSpecificMealsByCategories(filterName);
        specificMealsPresenter.getSpecificMealsByCountries(filterName);
        specificMealsPresenter.getSpecificMealsByIngredients(filterName);
        specificMealsPresenter.isGuest();

    }

    @Override
    public void getSpecificMealsByCategories(List<FilteredMeal> meal) {
        categoriesGridAdapter.setCategoryList(meal);
        categoriesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSpecificMealsyCountries(List<FilteredMeal> meal) {
        categoriesGridAdapter.setCategoryList(meal);
        categoriesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void getSpecificMealsyIngredients(List<FilteredMeal> meal) {
        categoriesGridAdapter.setCategoryList(meal);
        categoriesGridAdapter.notifyDataSetChanged();
    }


    @Override
    public void showError(String errorMsg) {

    }

    @Override
    public void showFavoriteAddedSuccess() {
    }

    @Override
    public void showGuestMessage(boolean isGuest) {
        categoriesGridAdapter.setGuestMode(isGuest);
    }


    @Override
    public void onMealClick(String id) {
        specificMealsPresenter.getMealDetailsById(id);
    }

    @Override
    public void getMealDetailsByID(List<MealElement> meal) {
        if (meal != null && !meal.isEmpty()) {
            MealElement mealElement = meal.get(0);
            NavDirections action = SpecificMealsFragmentDirections
                    .actionSpecificMealsFragmentToMealDetailsFragment(mealElement);
            Navigation.findNavController(requireView()).navigate(action);
        }
    }

    @Override
    public void onFavProductClick(String id) {
        specificMealsPresenter.onFavoriteClick(id);
        Toast.makeText(getContext(), "AddedSuccessfully", Toast.LENGTH_SHORT).show();

    }
}